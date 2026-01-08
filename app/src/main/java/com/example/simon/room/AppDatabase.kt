package com.example.simon.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RercordEntity::class], version = 1) // Lista de tablas y versión de la base de datos
abstract class AppDatabase : RoomDatabase() { // Clase abstracta que hereda de la estructura de Room
    abstract fun recordDao(): RecordDao // Función para que podamos pedirle el mensajero (DAO)
}