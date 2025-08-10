package com.example.riddlebattleoftheteam.presentation.state

import com.example.riddlebattleoftheteam.domain.model.Riddle
import com.example.riddlebattleoftheteam.domain.model.Team

sealed class GameState {
    object Preparation : GameState()
    object Active : GameState()
    data class Finished(
        val teamAnswers: Map<Int, List<Boolean>>,
        val winner: Team? = null
    ) : GameState()
}