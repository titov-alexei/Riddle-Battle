package com.example.riddlebattleoftheteam.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.riddlebattleoftheteam.domain.model.Riddle
import com.example.riddlebattleoftheteam.domain.model.Team
import com.example.riddlebattleoftheteam.domain.model.TeamResult
import com.example.riddlebattleoftheteam.domain.repository.RiddleRepository
import com.example.riddlebattleoftheteam.domain.repository.TeamRepository
import com.example.riddlebattleoftheteam.presentation.state.GameState
import com.example.riddlebattleoftheteam.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val teamRepository: TeamRepository,
    private val riddleRepository: RiddleRepository
) : ViewModel() {

    // Состояния
    private val _gameState = MutableStateFlow<GameState>(GameState.Preparation)
    val gameState: StateFlow<GameState> = _gameState

    private val _currentTeam = MutableStateFlow<Team?>(null)
    val currentTeam: StateFlow<Team?> = _currentTeam

    private val _currentRiddle = MutableStateFlow<Riddle?>(null)
    val currentRiddle: StateFlow<Riddle?> = _currentRiddle

    private val _timeLeft = MutableStateFlow(Constants.TIMER_TIME)
    val timeLeft: StateFlow<Int> = _timeLeft

    private val _questionNumber = MutableStateFlow(1)
    val questionNumber: StateFlow<Int> = _questionNumber

    private val _teamsState = MutableStateFlow<List<Team>>(emptyList())
    val teams: StateFlow<List<Team>> = _teamsState

    private val _currentRound = MutableStateFlow(0)
    val currentRound: StateFlow<Int> = _currentRound

    private val _isWaitingForNextTeam = MutableStateFlow(false)
    val isWaitingForNextTeam: StateFlow<Boolean> = _isWaitingForNextTeam

    private val _teamResults = MutableStateFlow<List<TeamResult>>(emptyList())
    val teamResults: StateFlow<List<TeamResult>> = _teamResults
    // Приватные переменные
    private var timerJob: Job? = null
    private var currentTeamIndex = 0
    private val teamAnswers = mutableMapOf<Int, MutableList<Boolean>>()

    fun loadGameData() {
        viewModelScope.launch {
            loadTeams()
            startNewRound()
        }
    }

    fun loadTeams() {
        viewModelScope.launch {
            val loadedTeams = teamRepository.getTeams()
            if (loadedTeams.isEmpty()) return@launch

            _teamsState.value = loadedTeams
            teamAnswers.clear()
            loadedTeams.forEach { team -> teamAnswers[team.id] = mutableListOf() }

            startNewGame()
        }
    }

    private suspend fun startNewGame() {
        currentTeamIndex = 0
        _currentRound.value = 0
        _currentTeam.value = _teamsState.value.first()
        _gameState.value = GameState.Active
        loadNewRiddle()
        startTimer()
    }

    private suspend fun loadNewRiddle() {
        try {
            _currentRiddle.value = riddleRepository.getRandomRiddle()
        } catch (e: Exception) {
            Log.e("GameViewModel", "Error loading riddle", e)
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        _timeLeft.value = Constants.TIMER_TIME
        _isWaitingForNextTeam.value = false

        timerJob = viewModelScope.launch {
            while (_timeLeft.value > 0) {
                delay(1000)
                _timeLeft.value -= 1
            }

            // Таймер закончился — ждём нажатия кнопки
            _isWaitingForNextTeam.value = true
        }
    }

    fun proceedToNextTeam() {
        viewModelScope.launch {
            _isWaitingForNextTeam.value = false
            moveToNextTeamOrRound()
        }
    }

    fun submitAnswer(isCorrect: Boolean) {
        viewModelScope.launch {
            // Сохраняем ответ
            currentTeam.value?.id?.let { teamId ->
                teamAnswers[teamId]?.add(isCorrect)
            }

            // Обновляем счет если ответ верный
            if (isCorrect) {
                updateTeamScore()
            }

            _questionNumber.value += 1
            loadNewRiddle()
        }
    }

    private suspend fun updateTeamScore() {
        val updatedTeams = _teamsState.value.toMutableList().apply {
            this[currentTeamIndex] = this[currentTeamIndex].copy(
                score = this[currentTeamIndex].score + 1
            )
        }
        _teamsState.value = updatedTeams
        teamRepository.updateTeam(updatedTeams[currentTeamIndex])
    }

    suspend fun moveToNextTeamOrRound() {
        if (isLastTeamInRound()) {
            finishGame()
        } else {
            nextTeam()
        }
    }

    fun isLastTeamInRound() = currentTeamIndex == _teamsState.value.size - 1

    private suspend fun startNewRound() {
        _currentRound.value++
        currentTeamIndex = 0
        _currentTeam.value = _teamsState.value[currentTeamIndex]
        _questionNumber.value = 1
        loadNewRiddle()
        startTimer()
    }

    suspend fun nextTeam() {
        currentTeamIndex = (currentTeamIndex + 1) % _teamsState.value.size
        _currentTeam.value = _teamsState.value[currentTeamIndex]
        _questionNumber.value = 1
        loadNewRiddle()
        startTimer()
    }

    fun finishGame() {
        calculateResults()
        _gameState.value = GameState.Finished(
            teamAnswers = teamAnswers.mapValues { it.value.toList() }
        )
    }

    private fun calculateResults() {
        val results = _teamsState.value.map { team ->
            val answers = teamAnswers[team.id] ?: emptyList()
            val correctCount = answers.count { it }

            TeamResult(
                teamName = team.name,
                correctAnswers = correctCount
            )
        }.sortedByDescending { it.correctAnswers }

        _teamResults.value = results
    }
}

