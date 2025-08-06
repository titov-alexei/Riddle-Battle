package com.example.riddlebattleoftheteam.domain.model

data class Team(
    val id: Int,
    val name: String,
    var score: Int = 0
)
