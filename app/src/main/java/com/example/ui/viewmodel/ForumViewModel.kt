package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.CourseEntity
import com.example.data.model.ForumReplyEntity
import com.example.data.model.ForumThreadEntity
import com.example.data.repository.CodeAcademyRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ForumViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CodeAcademyRepository.getInstance(application)

    init {
        repository.acquireRealtime("forum")
    }

    override fun onCleared() {
        super.onCleared()
        repository.releaseRealtime("forum")
    }

    val currentUserId: StateFlow<String?> = repository.authService.currentUserFlow
        .map { it?.id }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.authService.currentUserId)

    val forumThreads: StateFlow<List<ForumThreadEntity>> = repository.getAllForumThreadsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val courses: StateFlow<List<CourseEntity>> = repository.getAllCoursesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun getForumThreadById(threadId: String): Flow<ForumThreadEntity?> =
        repository.getForumThreadByIdFlow(threadId)

    fun getForumReplies(threadId: String): Flow<List<ForumReplyEntity>> =
        repository.getForumRepliesFlow(threadId)

    fun toggleUpvoteThread(thread: ForumThreadEntity) {
        viewModelScope.launch {
            repository.toggleUpvoteThread(thread)
        }
    }

    fun createForumThread(courseId: String?, courseTitle: String?, title: String, body: String) {
        val uid = currentUserId.value ?: return
        viewModelScope.launch {
            val author = repository.getProfile()?.displayName ?: repository.authService.currentUserFlow.value?.displayName ?: "Siswa Dev"
            repository.createForumThread(uid, author, courseId, courseTitle, title, body)
        }
    }

    fun addForumReply(threadId: String, body: String) {
        val uid = currentUserId.value ?: return
        viewModelScope.launch {
            val author = repository.getProfile()?.displayName ?: repository.authService.currentUserFlow.value?.displayName ?: "Siswa Dev"
            repository.addForumReply(threadId, uid, author, body)
        }
    }
}
