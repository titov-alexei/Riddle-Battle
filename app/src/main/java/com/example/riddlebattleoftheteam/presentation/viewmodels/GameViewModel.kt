package com.example.riddlebattleoftheteam.presentation.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.riddlebattleoftheteam.domain.repository.RiddleRepository
import com.example.riddlebattleoftheteam.domain.repository.TeamRepository
import com.example.riddlebattleoftheteam.presentation.state.GameState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val riddleRepository: RiddleRepository,
    private val teamRepository: TeamRepository
) : ViewModel() {

    private val _gameState = MutableStateFlow<GameState>(GameState.Preparation(emptyList()))
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    init {
        loadTeams()
        loadRiddles()
    }

    private fun loadTeams() {
        viewModelScope.launch {
            teamRepository.getTeams().collect { teams ->
                _gameState.value = GameState.Preparation(teams)
            }
        }
    }

    private fun loadRiddles() {
        viewModelScope.launch {
            val result = riddleRepository.fetchRiddles()
            result.onFailure { e ->
                println("Failed to load riddles: ${e.message}")
            }
        }
    }

    fun startGame() {
        viewModelScope.launch {
            val teams = (_gameState.value as? GameState.Preparation)?.teams ?: return@launch
            if (teams.isEmpty()) return@launch

            val riddle = try {
                riddleRepository.getRandomRiddle()
            } catch (e: Exception) {
                println("Error getting riddle: ${e.message}")
                return@launch
            }

            _gameState.value = GameState.Active(
                currentTeam = teams.first(),
                currentRiddle = riddle,
                timeLeft = 120,
                teams = teams
            )

            startTimer()
        }
    }

    private fun startTimer() {
        viewModelScope.launch {
            while (((_gameState.value as? GameState.Active)?.timeLeft ?: 0) > 0) {
                delay(1000)
                _gameState.value = (_gameState.value as GameState.Active).copy(
                    timeLeft = (_gameState.value as GameState.Active).timeLeft - 1
                )
            }
            submitAnswer(false) // Автоматически неверный ответ при таймауте
        }
    }

    fun submitAnswer(isCorrect: Boolean) {
        // Обновляем счет и переходим к следующей команде/загадке
    }

    fun resetGame() {
        loadTeams()
    }
}