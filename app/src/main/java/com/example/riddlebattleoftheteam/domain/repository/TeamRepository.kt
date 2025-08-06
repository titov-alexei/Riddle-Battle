package com.example.riddlebattleoftheteam.domain.repository

import com.example.riddlebattleoftheteam.domain.model.Team
import kotlinx.coroutines.flow.Flow

interface TeamRepository {
    suspend fun saveTeams(teams: List<Team>)
    fun getTeams(): Flow<List<Team>>
}