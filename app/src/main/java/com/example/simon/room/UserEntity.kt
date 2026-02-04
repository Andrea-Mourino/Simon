package com.example.simon.room

import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int,
    val name: String
)
