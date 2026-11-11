package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.ChatConversationEntity
import com.example.data.model.ChatMessageEntity
import com.example.data.repository.CodeAcademyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CodeAcademyRepository.getInstance(application)

    init {
        repository.acquireRealtime("chat")
    }

    override fun onCleared() {
        super.onCleared()
        repository.releaseRealtime("chat")
    }

    val currentUserId: StateFlow<String?> = repository.authService.currentUserFlow
        .map { it?.id }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.authService.currentUserId)

    val chatConversations: StateFlow<List<ChatConversationEntity>> = currentUserId.flatMapLatest { uid ->
        if (uid != null) repository.getChatConversationsFlow(uid) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun getChatMessages(convId: String): Flow<List<ChatMessageEntity>> =
        repository.getChatMessagesFlow(convId)

    fun sendChatMessage(convId: String, body: String) {
        val uid = currentUserId.value ?: return
        viewModelScope.launch {
            repository.sendChatMessage(convId, uid, body)
        }
    }
}
