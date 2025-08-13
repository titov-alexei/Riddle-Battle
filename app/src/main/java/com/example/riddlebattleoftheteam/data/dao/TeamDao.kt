package com.example.riddlebattleoftheteam.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.riddlebattleoftheteam.data.db.TeamEntity

@Dao
interface TeamDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(teams: List<TeamEntity>)

    @Query("SELECT * FROM teams")
    suspend fun getAll(): List<TeamEntity>

    @Query("SELECT COUNT(*) FROM teams")
    suspend fun getTeamsCount(): Int

    @Update
    suspend fun update(team: TeamEntity)

    @Query("DELETE FROM teams")
    suspend fun clearTeams()
}