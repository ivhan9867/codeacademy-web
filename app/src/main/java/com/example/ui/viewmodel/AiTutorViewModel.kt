package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AiChatMessageEntity
import com.example.data.repository.CodeAcademyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class AiTutorViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CodeAcademyRepository.getInstance(application)

    private val _activeSessionId = MutableStateFlow("session_general")
    val activeSessionId: StateFlow<String> = _activeSessionId.asStateFlow()

    val aiMessages: StateFlow<List<AiChatMessageEntity>> = _activeSessionId.flatMapLatest { sessionId ->
        repository.getAiChatMessagesFlow(sessionId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isAiGenerating = MutableStateFlow(false)
    val isAiGenerating: StateFlow<Boolean> = _isAiGenerating.asStateFlow()

    fun setSession(sessionId: String) {
        _activeSessionId.value = sessionId
    }

    fun sendAiMessage(userMessage: String, codeSnippet: String?) {
        viewModelScope.launch {
            _isAiGenerating.value = true
            val history = aiMessages.value.map { Pair(it.message, it.isUser) }
            val langCode = repository.sessionManager.appLanguageCode.firstOrNull() ?: "id"
            repository.sendAiMessage(_activeSessionId.value, userMessage, codeSnippet, history, language = langCode)
            _isAiGenerating.value = false
        }
    }
}
