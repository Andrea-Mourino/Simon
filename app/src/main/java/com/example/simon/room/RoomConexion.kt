package com.example.simon.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecordDao {
    @Query("SELECT * FROM records ORDER BY id DESC LIMIT 1")
    fun getRecord(): RercordEntity?
    @Insert
    fun insert(record: RercordEntity)

}