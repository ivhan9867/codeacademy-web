package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.CertificateEntity
import com.example.data.model.CourseEntity
import com.example.data.model.QuizQuestionEntity
import com.example.data.repository.CodeAcademyRepository
import kotlinx.coroutines.flow.*

class QuizViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CodeAcademyRepository.getInstance(application)

    val currentUserId: StateFlow<String?> = repository.authService.currentUserFlow
        .map { it?.id }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.authService.currentUserId)

    val courses: StateFlow<List<CourseEntity>> = repository.getAllCoursesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    suspend fun getCourse(courseId: String): CourseEntity? {
        return courses.value.find { it.id == courseId } ?: repository.getAllCoursesFlow().first().find { it.id == courseId }
    }

    suspend fun getQuizQuestions(courseId: String): List<QuizQuestionEntity> =
        repository.getQuizQuestions(courseId)

    suspend fun submitQuiz(courseId: String, answers: Map<Int, Int>): Triple<Int, Boolean, CertificateEntity?> {
        val uid = currentUserId.value ?: "guest_user"
        return repository.submitQuiz(uid, courseId, answers)
    }
}
