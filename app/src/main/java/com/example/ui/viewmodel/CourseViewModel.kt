package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.CodeAcademyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class CourseViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CodeAcademyRepository.getInstance(application)

    val currentUserId: StateFlow<String?> = repository.authService.currentUserFlow
        .map { it?.id }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.authService.currentUserId)

    val tracks: StateFlow<List<TrackEntity>> = repository.getAllTracksFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val courses: StateFlow<List<CourseEntity>> = repository.getAllCoursesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userProgress: StateFlow<List<UserProgressEntity>> = currentUserId.flatMapLatest { uid ->
        if (uid != null) repository.getUserProgressFlow(uid) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookmarks: StateFlow<List<BookmarkEntity>> = currentUserId.flatMapLatest { uid ->
        if (uid != null) repository.getBookmarksFlow(uid) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Level-up celebratory dialog trigger
    private val _levelUpEvent = MutableStateFlow<Int?>(null)
    val levelUpEvent: StateFlow<Int?> = _levelUpEvent.asStateFlow()

    fun dismissLevelUpDialog() {
        _levelUpEvent.value = null
    }

    fun getModulesForCourse(courseId: String): Flow<List<ModuleEntity>> =
        repository.getModulesByCourseFlow(courseId)

    fun getModuleById(moduleId: String): Flow<ModuleEntity?> =
        repository.getModuleByIdFlow(moduleId)

    fun markModuleCompleted(courseId: String, moduleId: String) {
        val uid = currentUserId.value ?: return
        viewModelScope.launch {
            val (isLevelUp, newLevel) = repository.markModuleCompleted(uid, courseId, moduleId)
            if (isLevelUp) {
                _levelUpEvent.value = newLevel
            }
        }
    }

    fun toggleBookmark(courseId: String) {
        val uid = currentUserId.value ?: return
        viewModelScope.launch {
            repository.toggleBookmark(uid, "COURSE", courseId)
        }
    }

    suspend fun getArAssetForModule(moduleId: String): ArAssetEntity? =
        repository.getArAssetForModule(moduleId)
}
