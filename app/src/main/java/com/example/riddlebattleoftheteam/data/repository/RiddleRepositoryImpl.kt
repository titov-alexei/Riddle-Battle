package com.example.riddlebattleoftheteam.data.repository

import android.util.Log
import com.example.riddlebattleoftheteam.R
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
                    // Помечаем все старые загадки как неиспользованные
                    db.riddleDao().resetUsedRiddles()
                    // Добавляем новые
                    db.riddleDao().insertAll(
                        riddles.map { apiResponse ->
                            RiddleEntity(
                                title = apiResponse.title,
                                question = apiResponse.question,
                                answer = apiResponse.answer,
                                isUsed = 0
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
        return try {
            // Сначала пробуем получить неиспользованную загадку
            db.riddleDao().getRandomUnusedRiddle().first()?.let { entity ->
                db.riddleDao().markAsUsed(entity.id)
                entity.toDomain()
            } ?: run {
                // Если нет неиспользованных, загружаем новые с API
                fetchRiddles().getOrThrow()
                // Повторяем попытку после загрузки
                db.riddleDao().getRandomUnusedRiddle().first()?.let { entity ->
                    db.riddleDao().markAsUsed(entity.id)
                    entity.toDomain()
                } ?: throw IllegalStateException("${R.string.failed_load_riddles}")
            }
        } catch (e: Exception) {
            Log.e("MYLOG", "${R.string.error_getting_riddle}", e)
            throw e
        }
    }

    override suspend fun resetUsedRiddles() {
        db.riddleDao().resetUsedRiddles()
    }

    override suspend fun hasAnyRiddles(): Boolean {
        return db.riddleDao().getRiddlesCount() > 0
    }
}