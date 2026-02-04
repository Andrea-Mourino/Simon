package com.example.simon.room;

public class Entity_Relacion_ej {
    /**
     * @Entity(tableName = "users")
     * data class UserEntity(
     *         @PrimaryKey val id: Int,
     *         val name: String
     * )
     */

    /**
     * @Entity(
     *     tableName = "score",
     *     foreignKeys = [
     *         ForeignKey(
     *             entity = UserEntity::class,
     *             parentColumns = ["id"],
     *             childColumns = ["userId"],
     *             onDelete = ForeignKey.CASCADE
     *         )
     *     ]
     * )
     * data class ScoreEntity(
     *     @PrimaryKey(autoGenerate = true)
     *     val id: Int = 0,
     *     val score: Int,           // Nivel alcanzado
     *     val time: String,         // Tiempo formateado
     *     val timestamp: Long,      // Para ordenar cronológicamente
     *     val userId: Int? = null   // Relación con UserEntity (nullable para scores sin usuario)
     * )
     */
}
