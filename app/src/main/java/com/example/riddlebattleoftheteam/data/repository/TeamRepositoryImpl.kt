package com.example.riddlebattleoftheteam.data.repository

import com.example.riddlebattleoftheteam.data.db.AppDatabase
import com.example.riddlebattleoftheteam.data.db.TeamEntity
import com.example.riddlebattleoftheteam.domain.model.Team
import com.example.riddlebattleoftheteam.domain.repository.TeamRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.map

@Singleton
class TeamRepositoryImpl @Inject constructor(
    private val db: AppDatabase
) : TeamRepository {

    override suspend fun saveTeams(teams: List<Team>) {
        db.teamDao().insertAll(teams.map { team ->
            TeamEntity(
                id = team.id,
                name = team.name,
                score = team.score
            )
        })
    }

    override suspend fun getTeams(): List<Team> {
        return db.teamDao().getAll().map { entity ->
            Team(
                id = entity.id,
                name = entity.name,
                score = entity.score
            )
        }
    }

    override suspend fun updateTeam(team: Team) {
        db.teamDao().update(
            TeamEntity(
                id = team.id,
                name = team.name,
                score = team.score
            )
        )
    }

    override suspend fun clearTeams() {
        db.teamDao().clearTeams()
    }
}