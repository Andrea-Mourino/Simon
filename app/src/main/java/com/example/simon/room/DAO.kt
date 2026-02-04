package com.example.simon.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface Dao {
    // ---------- USERS ----------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun

            insertUser(user:UserEntity)

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun

            getUser(userId:Int):UserEntity?


    // ---------- SCORES ----------

    @Insert
    suspend fun

            insertScore(score:ScoreEntity)

    @Query("""
                SELECT * FROM score
                WHERE userId = :userId
                ORDER BY score DESC
               LIMIT 1
            """)
    suspend fun

            getBestScoreForUser(userId:Int):ScoreEntity?

    @Query("""
                SELECT * FROM score
                ORDER BY score DESC
                LIMIT 1
            """)
    suspend fun

            getBestGlobalScore():ScoreEntity?


}