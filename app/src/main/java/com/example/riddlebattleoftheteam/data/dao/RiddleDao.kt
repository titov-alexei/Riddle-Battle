package com.example.riddlebattleoftheteam.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.riddlebattleoftheteam.data.db.RiddleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RiddleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(riddles: List<RiddleEntity>)

    @Query("SELECT * FROM riddles WHERE isUsed = 0 ORDER BY RANDOM() LIMIT :limit")
    fun getRandomUnusedRiddles(limit: Int): Flow<List<RiddleEntity>>

    @Query("SELECT * FROM riddles WHERE isUsed = 0 ORDER BY RANDOM() LIMIT 1")
    fun getRandomUnusedRiddle(): Flow<RiddleEntity?>

    @Query("UPDATE riddles SET isUsed = 1 WHERE id = :riddleId")
    suspend fun markAsUsed(riddleId: Int)

    @Query("UPDATE riddles SET isUsed = 0")
    suspend fun resetUsedRiddles()

    @Query("SELECT COUNT(*) FROM riddles")
    suspend fun getRiddlesCount(): Int
}