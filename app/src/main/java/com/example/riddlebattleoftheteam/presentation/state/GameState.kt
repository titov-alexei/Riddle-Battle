package com.example.riddlebattleoftheteam.presentation.state

sealed class GameState {
    object Preparation : GameState()

    object ReadyToStart : GameState()
    object Active : GameState()
    object Finished : GameState()

    data class Error(val message: String) : GameState()
}