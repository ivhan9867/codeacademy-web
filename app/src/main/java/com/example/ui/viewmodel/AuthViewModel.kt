package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.ProfileEntity
import com.example.data.model.TrackEntity
import com.example.data.remote.supabase.SupabaseSession
import com.example.data.remote.supabase.SupabaseUser
import com.example.data.repository.CodeAcademyRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CodeAcademyRepository.getInstance(application)

    val authSession: StateFlow<SupabaseSession?> = repository.authService.sessionFlow
    val currentUser: StateFlow<SupabaseUser?> = repository.authService.currentUserFlow

    val currentUserId: StateFlow<String?> = currentUser.map { it?.id }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.authService.currentUserId)

    val profile: StateFlow<ProfileEntity?> = repository.getProfileFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val tracks: StateFlow<List<TrackEntity>> = repository.getAllTracksFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun signInWithPassword(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val result = repository.authService.signInWithPassword(email, password)
            _isLoading.value = false
            if (result.isSuccess) {
                val user = result.getOrNull()?.user
                if (user != null) {
                    repository.syncUserData(user.id)
                }
                onResult(true, null)
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "Login gagal. Periksa kembali email dan sandi."
                _errorMessage.value = errorMsg
                onResult(false, errorMsg)
            }
        }
    }

    fun signUp(email: String, password: String, displayName: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val result = repository.authService.signUp(email, password, displayName)
            _isLoading.value = false
            if (result.isSuccess) {
                val user = result.getOrNull()
                if (user != null) {
                    repository.syncUserData(user.id)
                }
                onResult(true, null)
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "Pendaftaran gagal. Silakan coba lagi."
                _errorMessage.value = errorMsg
                onResult(false, errorMsg)
            }
        }
    }

    fun signOut(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.authService.signOut()
            onComplete()
        }
    }

    fun completeOnboarding(selectedTracks: List<String>, goal: String) {
        viewModelScope.launch {
            repository.completeOnboarding(selectedTracks, goal)
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
