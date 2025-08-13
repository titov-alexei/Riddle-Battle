package com.example.riddlebattleoftheteam.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.riddlebattleoftheteam.data.dao.RiddleDao
import com.example.riddlebattleoftheteam.data.dao.TeamDao

@Database(
    entities = [RiddleEntity::class, TeamEntity::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun riddleDao(): RiddleDao
    abstract fun teamDao(): TeamDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1,2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE riddles ADO COLUMN is_used INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}