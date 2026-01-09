package com.example.simon.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface Dao {
    @Query("SELECT * FROM records ORDER BY puntuacion DESC LIMIT 1")
    suspend fun getBestRecord(): Entity?

    @Insert
    suspend fun insertRecord(record: Entity)
}