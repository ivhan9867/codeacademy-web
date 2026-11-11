package com.example.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_stats")
data class UserStatsEntity(
    @PrimaryKey
    val id: Int = 1,
    val userName: String = "Viper Developer",
    val currentStreak: Int = 5,
    val bestStreak: Int = 14,
    val totalXp: Int = 1250,
    val coursesCompleted: Int = 2,
    val lessonsCompleted: Int = 18,
    val weeklyActivityMask: Int = 0b0111110, // bitmask for days Mon-Sun
    val lastActiveTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val iconName: String,
    val isUnlocked: Boolean = false,
    val progress: Float = 0f,
    val xpBonus: Int = 50
)
