package com.example.riddlebattleoftheteam.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.riddlebattleoftheteam.R
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val teamRepository: TeamRepository,
    private val riddleRepository: RiddleRepository
) : ViewModel() {

    // Состояния игры
    private val _gameState = MutableStateFlow<GameState>(GameState.Preparation)
    val gameState: StateFlow<GameState> = _gameState

    //Текущая команда
    private val _currentTeam = MutableStateFlow<Team?>(null)
    val currentTeam: StateFlow<Team?> = _currentTeam

    //Текущая загадка
    private val _currentRiddle = MutableStateFlow<Riddle?>(null)
    val currentRiddle: StateFlow<Riddle?> = _currentRiddle

    //Оставшееся время
    private val _timeLeft = MutableStateFlow(Constants.TIMER_TIME)
    val timeLeft: StateFlow<Int> = _timeLeft

    //Номер вопроса
    private val _questionNumber = MutableStateFlow(1)
    val questionNumber: StateFlow<Int> = _questionNumber

    //Результаты
    private val _teamResults = MutableStateFlow<List<TeamResult>>(emptyList())
    val teamResults: StateFlow<List<TeamResult>> = _teamResults

    //Загрузка
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val teams = mutableListOf<Team>()

    private var currentTeamIndex = 0
    private val teamAnswers = mutableMapOf<Int, MutableList<Boolean>>()
    private var timerJob: Job? = null

    private var preloadRiddlesJob: Job? = null

    init {
        startPreloadingRiddles()
    }

    private fun startPreloadingRiddles() {
        preloadRiddlesJob = viewModelScope.launch {
            while (true) {
                try {
                    riddleRepository.getRandomRiddles(30).collect { riddles ->
                        if (riddles.isEmpty()) {
                            riddleRepository.fetchRiddles()
                        }
                    }
                } catch (e: Exception) {
                    Log.e("MYLOG", "${R.string.failed_load_riddles}", e)
                }
                delay(10000)
            }
        }
    }

    fun loadGameData() {
        viewModelScope.launch {
            try {
                // Загрузка команд
                teams.clear()
                teams.addAll(teamRepository.getTeams())
                if (teams.isEmpty()) {
                    _gameState.value = GameState.Error(message = "${R.string.no_teams}")
                    return@launch
                }

                // Инициализация ответов
                teamAnswers.clear()
                teams.forEach { team ->
                    teamAnswers[team.id] = mutableListOf()
                }

                // Проверка загадок
                ensureEnoughRiddles()

                // Подготовка к старту
                _currentTeam.value = teams.first()
                _gameState.value = GameState.ReadyToStart
            } catch (e: Exception) {
                _gameState.value = GameState.Error("${R.string.loading_error} ${e.message}")
            }
        }
    }

    private suspend fun ensureEnoughRiddles() {
        try {
            val riddles = riddleRepository.getRandomRiddles(1).first()
            if (riddles.isEmpty()) {
                riddleRepository.fetchRiddles()
            }
        } catch (e: Exception) {
            Log.e("MYLOG", "Error checking riddles", e)
        }
    }

    fun startGame() {
        viewModelScope.launch {
            try {
                // Проверяем наличие загадок
                val hasRiddles = riddleRepository.hasAnyRiddles()
                if (!hasRiddles) {
                    riddleRepository.fetchRiddles()
                }

                _gameState.value = GameState.Active
                loadNewRiddle()
                startTimer()
            } catch (e: Exception) {
                _gameState.value = GameState.Error("${R.string.loading_error}")
            }
        }
    }
    private suspend fun loadNewRiddle() {
        try {
            _currentRiddle.value = riddleRepository.getRandomRiddle()
        } catch (e: NoSuchElementException) {
            riddleRepository.fetchRiddles()
            _currentRiddle.value = riddleRepository.getRandomRiddle()
        } catch (e: Exception) {
            Log.e("MYLOG", "${R.string.failed_load_riddles}", e)
            _currentRiddle.value = null
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        _timeLeft.value = Constants.TIMER_TIME
        timerJob = viewModelScope.launch {
            while (_timeLeft.value > 0) {
                delay(1000)
                _timeLeft.value -= 1
            }
            submitAnswer(false)
        }
    }

    fun isLastTeam() = currentTeamIndex == teams.size - 1

    fun submitAnswer(isCorrect: Boolean) {
        viewModelScope.launch {
            currentTeam.value?.id?.let { teamId ->
                teamAnswers[teamId]?.add(isCorrect)
            }

            if (isCorrect) {
                teams[currentTeamIndex].score += 1
                teamRepository.updateTeam(teams[currentTeamIndex])
            }

            _questionNumber.value += 1
            loadNewRiddle()
        }
    }

    fun nextTeam() {
        viewModelScope.launch {
            if (currentTeamIndex < teams.size - 1) {
                currentTeamIndex++
                _currentTeam.value = teams[currentTeamIndex]
                _questionNumber.value = 1
                loadNewRiddle()
                startTimer()
            } else {
                finishGame()
            }
        }
    }

    private fun finishGame() {
        calculateResults()
        _gameState.value = GameState.Finished
    }

    private fun calculateResults() {
        val results = teams.map { team ->
            val answers = teamAnswers[team.id] ?: emptyList()
            TeamResult(
                teamName = team.name,
                correctAnswers = answers.count { it },
                totalQuestions = answers.size
            )
        }.sortedByDescending { it.correctAnswers }

        _teamResults.value = results
    }

    fun resetGame() {
        viewModelScope.launch {
            // Сброс состояния загадок в репозитории
            riddleRepository.resetUsedRiddles()

            // Очистка данных игры
            timerJob?.cancel()
            teams.clear()
            teamAnswers.clear()
            _currentTeam.value = null
            _currentRiddle.value = null
            _timeLeft.value = 120
            _questionNumber.value = 1
            _gameState.value = GameState.Preparation
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        preloadRiddlesJob?.cancel()
    }
}

