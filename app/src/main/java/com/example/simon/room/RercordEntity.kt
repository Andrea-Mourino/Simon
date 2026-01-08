package com.example.simon.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "records")
data class RercordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val record:Int,
    val fecha: Long
)