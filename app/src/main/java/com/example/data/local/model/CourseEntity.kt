package com.example.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val category: String, // e.g., "Kotlin", "Android", "Python", "Web", "AI/ML"
    val level: String, // "Beginner", "Intermediate", "Advanced"
    val durationMinutes: Int,
    val lessonCount: Int,
    val rating: Float,
    val enrolledCount: Int,
    val accentColorHex: String,
    val iconName: String,
    val isBookmarked: Boolean = false,
    val progressPercent: Int = 0,
    val completedLessons: Int = 0,
    val lastAccessedTimestamp: Long = 0L
)
