package com.example.riddlebattleoftheteam.presentation.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.riddlebattleoftheteam.R
import com.example.riddlebattleoftheteam.presentation.components.CustomButton
import com.example.riddlebattleoftheteam.presentation.components.CustomViewQuestion
import com.example.riddlebattleoftheteam.presentation.navigation.Screen
import com.example.riddlebattleoftheteam.presentation.state.GameState
import com.example.riddlebattleoftheteam.presentation.viewmodels.GameViewModel
import com.example.riddlebattleoftheteam.ui.theme.Dimens


@Composable
fun GameScreen(
    navController: NavHostController,
    viewModel: GameViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.gameState.collectAsStateWithLifecycle()
    val currentTeam by viewModel.currentTeam.collectAsStateWithLifecycle()
    val currentRiddle by viewModel.currentRiddle.collectAsStateWithLifecycle()
    val timeLeft by viewModel.timeLeft.collectAsStateWithLifecycle()
    val questionNumber by viewModel.questionNumber.collectAsStateWithLifecycle()
    var showStartButton by remember { mutableStateOf(true) }
    val isWaiting by viewModel.isWaitingForNextTeam.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadTeams() // Загружаем команды сразу при входе на экран
    }

    Column(
        modifier = Modifier
            .padding(
                top = Dimens.CustomBoxPadding,
            )
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.StandartPadding)
    ) {
        if (showStartButton) {
            // Показываем кнопку старта и приветствие
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    currentTeam?.let { team ->
                        Text("${stringResource(R.string.the_teams_start)} ${team.name}")
                    }

                    Text(
                        text = stringResource(R.string.are_you_ready),
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                CustomButton(
                    text = stringResource(R.string.start),
                    onClick = {
                        showStartButton = false
                        viewModel.loadGameData()
                    }
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    currentTeam?.let { team ->
                        Text(
                            text = team.name,
                        )
                    }

                    currentRiddle?.let { riddle ->
                        CustomViewQuestion(
                            question = riddle.question,
                            answer = riddle.answer,
                            numberQuestion = questionNumber,
                            modifier = Modifier.padding(Dimens.StandartPadding)
                        )
                    } ?: Text(text = stringResource(R.string.loading_riddle))
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Dimens.StandartPadding)
                ) {
                    // Таймер
                    Text(
                        text = "%02d:%02d".format(timeLeft / 60, timeLeft % 60),
                        //style = MaterialTheme.typography.headlineMedium,
                        color = if (timeLeft < 10) Color.Red else Color.White
                    )

                    if (isWaiting) {
                        // Показываем кнопку перехода к следующей команде
                        CustomButton(
                            text =
                                if (!viewModel.isLastTeamInRound())
                                    stringResource(R.string.next_team)
                                else stringResource(R.string.results),
                            onClick = {
                                if (!viewModel.isLastTeamInRound())
                                    viewModel.proceedToNextTeam()
                                else viewModel.finishGame()
                                //viewModel.proceedToNextTeam()
                            }
                        )
                    } else {
                        // Кнопки ответа доступны только во время таймера
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(Dimens.StandartPadding)
                        ) {
                            CustomButton(
                                text = stringResource(R.string.yes),
                                onClick = { viewModel.submitAnswer(true) },
                                color = Color.Green
                            )
                            CustomButton(
                                text = stringResource(R.string.no),
                                onClick = { viewModel.submitAnswer(false) },
                                color = Color.Red
                            )
                        }
                    }
                }
            }

        }

        // Обработка завершения игры
        LaunchedEffect(state) {
            if (state is GameState.Finished) {
                Toast.makeText(context, "Игра завершена!", Toast.LENGTH_SHORT).show()
                navController.navigate(Screen.GameResultScreen.route)
            }
        }
    }
}