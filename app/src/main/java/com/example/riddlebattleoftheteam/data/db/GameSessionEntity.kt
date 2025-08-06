package com.example.riddlebattleoftheteam.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_sessions")
data class GameSessionEntity(
    @PrimaryKey val id: String,
    //остальные поля потом
)