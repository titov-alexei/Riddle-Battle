package com.example.riddlebattleoftheteam.domain.model

data class TeamResult(
    val teamName: String,
    val correctAnswers: Int,
    val totalQuestions: Int
)