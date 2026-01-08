package com.example.simon.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RercordEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recordDao(): RecordDao
}