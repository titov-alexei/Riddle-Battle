package com.example.riddlebattleoftheteam.domain.repository

import com.example.riddlebattleoftheteam.domain.model.Riddle
import kotlinx.coroutines.flow.Flow

interface RiddleRepository {
    suspend fun fetchRiddles(): Result<Unit>
    fun getRandomRiddles(limit: Int): Flow<List<Riddle>>
    suspend fun getRandomRiddle(): Riddle
    suspend fun resetUsedRiddles()
    suspend fun hasAnyRiddles(): Boolean

}