package com.example.riddlebattleoftheteam.domain.model

data class TeamAnswer(
    val teamId: Int,
    val riddleId: Int,
    val isCorrect: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)