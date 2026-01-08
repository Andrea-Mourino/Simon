package com.example.simon.room

import androidx.room.PrimaryKey

data class RercordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val record:Int,
    val fecha: Long
)