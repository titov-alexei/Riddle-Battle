package com.example.riddlebattleoftheteam.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.riddlebattleoftheteam.domain.model.Riddle

@Entity(tableName = "riddles")
data class RiddleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val question: String,
    val answer: String,
    val isUsed: Int = 0
) {
    fun toDomain() = Riddle(id, question, answer)
}