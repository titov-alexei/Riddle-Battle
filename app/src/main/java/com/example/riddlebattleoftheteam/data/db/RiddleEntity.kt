package com.example.riddlebattleoftheteam.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.riddlebattleoftheteam.domain.model.Riddle
import java.util.UUID

@Entity(tableName = "riddles")
data class RiddleEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val question: String,
    val answer: String,
    val isUsed: Boolean = false
) {
    fun toDomain() = Riddle(id, question, answer)
}