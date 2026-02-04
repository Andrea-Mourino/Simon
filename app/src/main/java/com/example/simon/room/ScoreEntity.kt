package com.example.simon.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.coroutines.flow.MutableStateFlow

@Entity(
    tableName = "score",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ScoreEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val score: Int,           // Nivel alcanzado
    val time: String,         // Tiempo formateado
    val timestamp: Long,      // Para ordenar cronológicamente
    val userId: Int? = null   // Relación con UserEntity (nullable para scores sin usuario)
)
