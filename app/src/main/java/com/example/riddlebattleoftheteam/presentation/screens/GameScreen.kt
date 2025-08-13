package com.example.riddlebattleoftheteam.presentation.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.riddlebattleoftheteam.R
import com.example.riddlebattleoftheteam.domain.model.Riddle
import com.example.riddlebattleoftheteam.domain.model.Team
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
    val state by viewModel.gameState.collectAsStateWithLifecycle()
    val currentTeam by viewModel.currentTeam.collectAsStateWithLifecycle()
    val currentRiddle by viewModel.currentRiddle.collectAsStateWithLifecycle()
    val timeLeft by viewModel.timeLeft.collectAsStateWithLifecycle()
    val questionNumber by viewModel.questionNumber.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    // Загрузка данных при первом открытии экрана
    LaunchedEffect(Unit) {
        viewModel.loadGameData()
    }

    // Обработка перехода к результатам
    LaunchedEffect(state) {
        if (state is GameState.Finished) {
            navController.navigate(Screen.GameResultScreen.route) {
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    when {
        isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        state is GameState.ReadyToStart -> {
            StartGameView(
                currentTeam = currentTeam,
                onStartClick = { viewModel.startGame() }
            )
        }

        state is GameState.Active -> {
            ActiveGameView(
                currentTeam = currentTeam,
                currentRiddle = currentRiddle,
                questionNumber = questionNumber,
                timeLeft = timeLeft,
                onAnswerSubmit = { isCorrect -> viewModel.submitAnswer(isCorrect) },
                onNextTeam = { viewModel.nextTeam() },
                isLastTeam = { viewModel.isLastTeam() }
            )
        }

        state is GameState.Error -> {
            ErrorView(
                message = (state as GameState.Error).message,
                onRetry = { viewModel.loadGameData() }
            )
        }
    }
}

@Composable
private fun StartGameView(
    currentTeam: Team?,
    onStartClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(top = Dimens.CustomBoxPadding)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.StandartPadding)
    ) {
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
                    Text(
                        text = "${stringResource(R.string.the_teams_start)} ${team.name}",
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                Text(
                    text = stringResource(R.string.are_you_ready),
                    style = MaterialTheme.typography.bodyLarge
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
                onClick = onStartClick,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun ActiveGameView(
    currentTeam: Team?,
    currentRiddle: Riddle?,
    questionNumber: Int,
    timeLeft: Int,
    onAnswerSubmit: (Boolean) -> Unit,
    onNextTeam: () -> Unit,
    isLastTeam: () -> Boolean
) {
    val isWaiting = timeLeft <= 0

    Column(
        modifier = Modifier
            .padding(top = Dimens.CustomBoxPadding)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.StandartPadding)
    ) {
        // Область с вопросом
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                currentTeam?.let { team ->
                    Text(
                        text = "${stringResource(R.string.team)} ${team.name}",
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (isWaiting) {
                    Text(modifier = Modifier
                        .fillMaxWidth(),
                        text = stringResource(R.string.round_is_over),
                        textAlign = TextAlign.Center)

                } else {
                    currentRiddle?.let { riddle ->
                        CustomViewQuestion(
                            question = riddle.question,
                            answer = riddle.answer,
                            numberQuestion = questionNumber,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } ?: Text(
                        text = stringResource(R.string.loading_riddle),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        // Область с таймером и кнопками
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Таймер
                Text(
                    text = "%02d:%02d".format(timeLeft / 60, timeLeft % 60),
                    style = MaterialTheme.typography.titleLarge,
                    color = if (timeLeft < 10) Color.Red else Color.White
                )

                if (isWaiting) {
                    // Кнопка продолжения
                    CustomButton(
                        text = if (isLastTeam()) stringResource(R.string.results)
                        else stringResource(R.string.next_team),
                        onClick = onNextTeam,
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                } else {
                    // Кнопки ответа
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        CustomButton(
                            text = stringResource(R.string.yes),
                            onClick = { onAnswerSubmit(true) },
                            color = Color.Green,
                            modifier = Modifier.weight(1f)
                        )
                        CustomButton(
                            text = stringResource(R.string.no),
                            onClick = { onAnswerSubmit(false) },
                            color = Color.Red,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorView(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        CustomButton(
            text = stringResource(R.string.next),
            onClick = onRetry
        )
    }
}
