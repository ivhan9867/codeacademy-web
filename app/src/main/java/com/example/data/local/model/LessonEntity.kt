package com.example.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lessons")
data class LessonEntity(
    @PrimaryKey
    val id: String,
    val courseId: String,
    val orderIndex: Int,
    val title: String,
    val durationMinutes: Int,
    val isCompleted: Boolean = false,
    val codeSnippet: String,
    val expectedOutput: String,
    val explanation: String,
    val xpReward: Int = 25
)
