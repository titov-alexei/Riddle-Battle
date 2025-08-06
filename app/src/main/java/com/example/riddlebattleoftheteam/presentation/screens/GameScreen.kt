package com.example.riddlebattleoftheteam.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.riddlebattleoftheteam.presentation.state.GameState
import com.example.riddlebattleoftheteam.presentation.viewmodels.GameViewModel


@Composable
fun GameScreen(
    navController: NavHostController,
    viewModel: GameViewModel = hiltViewModel()
) {
    val state by viewModel.gameState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.startGame()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (val currentState = state) {
            is GameState.Preparation -> {
                Text("Подготовка...", color = Color.White, fontSize = 24.sp)
            }
            is GameState.Active -> {
                Text("Команда: ${currentState.currentTeam.name}", color = Color.White)
                Text("Загадка: ${currentState.currentRiddle.question}", color = Color.White)
                Text("Осталось времени: ${currentState.timeLeft} сек", color = Color.White)

                Button(onClick = {
                    viewModel.submitAnswer(true)
                    println("Current game state: $currentState") // Логируем состояние
                }) {
                    Text("Правильный ответ")
                }

                Button(onClick = {
                    viewModel.submitAnswer(false)
                    println("Current game state: $currentState") // Логируем состояние
                }) {
                    Text("Неправильный ответ")
                }
            }
            is GameState.Finished -> {
                Text("Игра завершена!", color = Color.White)
                currentState.teams.forEach { team ->
                    Text("${team.name}: ${team.score} очков", color = Color.White)
                }
            }
        }

        Button(
            onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Выйти из игры")
        }
    }
}
