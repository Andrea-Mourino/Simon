package com.example.simon.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.coroutines.flow.MutableStateFlow

@Entity(tableName = "records")
data class Entity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "puntuacion") val record: Int,
    @ColumnInfo(name = "fecha") val fecha: Long,
    @ColumnInfo(name = "jugador") val jugador: String

)