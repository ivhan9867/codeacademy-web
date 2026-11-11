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
class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CodeAcademyRepository.getInstance(application)

    val currentUserId: StateFlow<String?> = repository.authService.currentUserFlow
        .map { it?.id }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.authService.currentUserId)

    val profile: StateFlow<ProfileEntity?> = repository.getProfileFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allBadges: StateFlow<List<BadgeEntity>> = repository.getAllBadgesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userBadges: StateFlow<List<UserBadgeEntity>> = currentUserId.flatMapLatest { uid ->
        if (uid != null) repository.getUserBadgesFlow(uid) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val certificates: StateFlow<List<CertificateEntity>> = currentUserId.flatMapLatest { uid ->
        if (uid != null) repository.getCertificatesFlow(uid) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookmarks: StateFlow<List<BookmarkEntity>> = currentUserId.flatMapLatest { uid ->
        if (uid != null) repository.getBookmarksFlow(uid) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val playgroundDrafts: StateFlow<List<PlaygroundDraftEntity>> = currentUserId.flatMapLatest { uid ->
        if (uid != null) repository.getAllPlaygroundDraftsFlow(uid) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val courses: StateFlow<List<CourseEntity>> = repository.getAllCoursesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateProfile(name: String, bio: String) {
        viewModelScope.launch {
            val current = repository.getProfile() ?: return@launch
            repository.updateProfile(current.copy(displayName = name, bio = bio))
        }
    }

    fun signOut(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.authService.signOut()
            onComplete()
        }
    }
}
