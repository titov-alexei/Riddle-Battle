package com.example.riddlebattleoftheteam.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.riddlebattleoftheteam.R
import com.example.riddlebattleoftheteam.domain.model.Team
import com.example.riddlebattleoftheteam.domain.repository.TeamRepository
import com.example.riddlebattleoftheteam.domain.usecases.LanguageUseCase
import com.example.riddlebattleoftheteam.presentation.navigation.Screen
import com.example.riddlebattleoftheteam.utils.showLocalizedToast
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class GamePreparationViewModel @Inject constructor(
    private val teamRepository: TeamRepository,
    private val languageUseCase: LanguageUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    companion object {
        const val COUNT_SELECTED_TEAM = 2
    }

    private val _selectedTeamCount = MutableStateFlow(COUNT_SELECTED_TEAM) // Начальное значение
    val selectedTeamCount: StateFlow<Int> = _selectedTeamCount

    private val _teamNames = MutableStateFlow(List(4) { "" }) // Максимум 4 команды
    val teamNames: StateFlow<List<String>> = _teamNames

    init {
        selectTeamCount(COUNT_SELECTED_TEAM)
    }

    fun selectTeamCount(count: Int) {
        _selectedTeamCount.value = count
        // Обновляем список имен, оставляя только нужное количество
        _teamNames.value = List(count) { index ->
            _teamNames.value.getOrElse(index) { "" }
        }
    }

    fun updateTeamName(index: Int, name: String) {
        val currentNames = _teamNames.value.toMutableList()
        if (index in currentNames.indices) {
            currentNames[index] = name
            _teamNames.value = currentNames
        }
    }

    fun startGame(navController: NavHostController) {
        viewModelScope.launch {
            // Проверка на пустые имена
            if (teamNames.value.any { it.isBlank() }) {
                context.showLocalizedToast(R.string.fill_all_name_teams, languageUseCase)
                return@launch
            }

            // Проверка на повторяющиеся имена (без учета регистра)
            val teamNamesList = teamNames.value.take(_selectedTeamCount.value)
                .map { it.trim().lowercase() }

            val uniqueNames = teamNamesList.distinct() //Убираем для нового листа

            if (uniqueNames.size < teamNamesList.size) {
                context.showLocalizedToast(R.string.teams_name_must_not_repeated, languageUseCase)
                return@launch
            }

            // Создаем только выбранное количество команд
            val teamsToCreate = _teamNames.value.take(_selectedTeamCount.value)
                .mapIndexed { index, name ->
                    Team(id = index, name = name, score = 0)
                }

            teamRepository.clearTeams()
            teamRepository.saveTeams(teamsToCreate)

            navController.navigate(Screen.GameScreen.route)
        }
    }
}


