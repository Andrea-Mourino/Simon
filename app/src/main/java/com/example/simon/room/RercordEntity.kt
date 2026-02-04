package com.example.simon.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "records") // Indica que esta clase será una tabla llamada "records" en la base de datos
data class RercordEntity(
    @PrimaryKey(autoGenerate = true) // El "id" será único y Room lo sumará solo (1, 2, 3...)
    val id: Int = 0,
    val record: Int, // Columna para guardar la puntuación (la ronda)
    val fecha: Long, // Guardamos la fecha como Long (milisegundos) porque SQLite no entiende objetos LocalDate
    val playerName: String = "Jugador" // Columna para guardar el nombre del jugador
)