package com.example.riddlebattleoftheteam.data.db

import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomDatabase
import com.example.riddlebattleoftheteam.data.dao.RiddleDao
import com.example.riddlebattleoftheteam.data.dao.TeamDao
import com.example.riddlebattleoftheteam.data.db.RiddleEntity
import com.example.riddlebattleoftheteam.data.db.TeamEntity

@Database(
    entities = [RiddleEntity::class, TeamEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun riddleDao(): RiddleDao
    abstract fun teamDao(): TeamDao
}