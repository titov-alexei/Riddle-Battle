package com.example.riddlebattleoftheteam.presentation.state

import com.example.riddlebattleoftheteam.domain.model.Riddle
import com.example.riddlebattleoftheteam.domain.model.Team

sealed class GameState {
    data class Preparation(val teams: List<Team>) : GameState()
    data class Active(
        val currentTeam: Team,
        val currentRiddle: Riddle,
        val timeLeft: Int,
        val teams: List<Team>
    ) : GameState()

    data class Finished(val teams: List<Team>) : GameState()
}