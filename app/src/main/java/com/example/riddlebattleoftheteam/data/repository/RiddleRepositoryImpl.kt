package com.example.riddlebattleoftheteam.data.repository

import com.example.riddlebattleoftheteam.data.api.RiddleApiService
import com.example.riddlebattleoftheteam.data.db.AppDatabase
import com.example.riddlebattleoftheteam.data.db.RiddleEntity
import com.example.riddlebattleoftheteam.domain.model.Riddle
import com.example.riddlebattleoftheteam.domain.repository.RiddleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RiddleRepositoryImpl @Inject constructor(
    private val api: RiddleApiService,
    private val db: AppDatabase
) : RiddleRepository {

    override suspend fun fetchRiddles(): Result<Unit> {
        return try {
            val response = api.getRiddles()
            if (response.isSuccessful) {
                response.body()?.let { riddles ->
                    db.riddleDao().insertAll(
                        riddles.map { apiResponse ->
                            RiddleEntity(
                                title = apiResponse.title,
                                question = apiResponse.question,
                                answer = apiResponse.answer
                            )
                        }
                    )
                }
                Result.success(Unit)
            } else {
                Result.failure(Exception("API error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getRandomRiddles(limit: Int): Flow<List<Riddle>> {
        return db.riddleDao()
            .getRandomUnusedRiddles(limit)
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun getRandomRiddle(): Riddle {
        return db.riddleDao().getRandomUnusedRiddle().first()?.toDomain()
            ?: throw NoSuchElementException("No unused riddles available")
    }
}