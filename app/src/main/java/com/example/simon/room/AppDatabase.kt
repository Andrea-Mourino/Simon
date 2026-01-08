package com.example.simon.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RercordEntity::class], version = 1) //Marcamos la clase como una base de datos de Room
abstract class AppDatabase : RoomDatabase() {
    abstract fun recordDao(): RecordDao
}