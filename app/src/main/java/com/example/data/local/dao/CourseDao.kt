package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.model.CourseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {
    @Query("SELECT * FROM courses ORDER BY lastAccessedTimestamp DESC, rating DESC")
    fun getAllCoursesFlow(): Flow<List<CourseEntity>>

    @Query("SELECT * FROM courses WHERE isBookmarked = 1 ORDER BY title ASC")
    fun getBookmarkedCoursesFlow(): Flow<List<CourseEntity>>

    @Query("SELECT * FROM courses WHERE progressPercent > 0 ORDER BY lastAccessedTimestamp DESC")
    fun getInProgressCoursesFlow(): Flow<List<CourseEntity>>

    @Query("SELECT * FROM courses WHERE id = :courseId LIMIT 1")
    fun getCourseByIdFlow(courseId: String): Flow<CourseEntity?>

    @Query("SELECT * FROM courses WHERE id = :courseId LIMIT 1")
    suspend fun getCourseById(courseId: String): CourseEntity?

    @Query("SELECT COUNT(*) FROM courses")
    suspend fun getCourseCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourses(courses: List<CourseEntity>)

    @Update
    suspend fun updateCourse(course: CourseEntity)

    @Query("UPDATE courses SET isBookmarked = :isBookmarked WHERE id = :courseId")
    suspend fun setBookmark(courseId: String, isBookmarked: Boolean)

    @Query("UPDATE courses SET progressPercent = :progress, completedLessons = :completed, lastAccessedTimestamp = :timestamp WHERE id = :courseId")
    suspend fun updateProgress(courseId: String, progress: Int, completed: Int, timestamp: Long)
}
