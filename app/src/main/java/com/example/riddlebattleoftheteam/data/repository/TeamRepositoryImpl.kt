package com.example.riddlebattleoftheteam.data.repository

import com.example.riddlebattleoftheteam.data.db.AppDatabase
import com.example.riddlebattleoftheteam.data.db.TeamEntity
import com.example.riddlebattleoftheteam.domain.model.Team
import com.example.riddlebattleoftheteam.domain.repository.TeamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    override fun getTeams(): Flow<List<Team>> {
        return db.teamDao().getAll().map { entities ->
            entities.map { entity ->
                Team(
                    id = entity.id,
                    name = entity.name,
                    score = entity.score
                )
            }
        }
    }
}