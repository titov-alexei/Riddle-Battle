package com.example.riddlebattleoftheteam.presentation.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.riddlebattleoftheteam.R
import com.example.riddlebattleoftheteam.domain.model.Team
import com.example.riddlebattleoftheteam.domain.repository.TeamRepository
import com.example.riddlebattleoftheteam.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class GamePreparationViewModel @Inject constructor(
    private val teamRepository: TeamRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _selectedTeamCount = MutableStateFlow(2) // Начальное значение
    val selectedTeamCount: StateFlow<Int> = _selectedTeamCount.asStateFlow()

    private val _teamNames = MutableStateFlow(listOf("", ""))
    val teamNames: StateFlow<List<String>> = _teamNames.asStateFlow()

    fun selectTeamCount(count: Int) {
        _selectedTeamCount.value = count // Обновляем значение
        _teamNames.value = List(count) { index ->
            _teamNames.value.getOrElse(index) { "" }
        }
    }

    fun updateTeamName(index: Int, name: String) {
        _teamNames.value = _teamNames.value.toMutableList().apply {
            set(index, name)
        }
    }

    fun startGame(navController: NavHostController) {

        viewModelScope.launch {
            // Проверка на пустые имена
            if (teamNames.value.any { it.isBlank() }) {
                Toast.makeText(context, R.string.fill_all_name_teams, Toast.LENGTH_SHORT).show()
                return@launch
            }

            // Сохраняем команды в БД
            val teams = teamNames.value.mapIndexed { index, name ->
                Team(id = index + 1, name = name)
            }
            teamRepository.saveTeams(teams)

            // Переход на экран игры
            navController.navigate(Screen.GameScreen.route)
        }
    }
}
