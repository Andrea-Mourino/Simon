package com.example.simon.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

/**
 *     entities = [
 *         Entity::class,
 *         Entity2::class
 *     ],
 *
 */
@Database(entities = [UserEntity::class, ScoreEntity::class], version = 1) // definismos la configuracion junto a sus entidades
abstract class AppDatabase : RoomDatabase() { // Clase abstracta que expone el DAO para las Entidades de la ddbb
    abstract fun Dao(): Dao
}
