package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.sessionDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_session")

class SessionManager(private val context: Context) {

    companion object {
        val KEY_IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val KEY_CURRENT_ACCOUNT_ID = longPreferencesKey("current_account_id")
        val KEY_CURRENT_ACCOUNT_EMAIL = stringPreferencesKey("current_account_email")
        val KEY_CURRENT_DISPLAY_NAME = stringPreferencesKey("current_display_name")
        val KEY_APP_LANGUAGE = stringPreferencesKey("app_language")
        val KEY_LAST_MICRO_CHALLENGE_DATE = stringPreferencesKey("last_micro_challenge_date")
        private const val SYNC_PREFS_NAME = "codeacademy_sync_prefs"
    }

    private val syncPrefs by lazy {
        context.getSharedPreferences(SYNC_PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getCachedLanguageCode(): String {
        return syncPrefs.getString("app_language", "id") ?: "id"
    }

    val appLanguageCode: Flow<String> = context.sessionDataStore.data.map { prefs ->
        val code = prefs[KEY_APP_LANGUAGE] ?: getCachedLanguageCode()
        if (syncPrefs.getString("app_language", null) != code) {
            syncPrefs.edit().putString("app_language", code).apply()
        }
        code
    }

    suspend fun saveAppLanguage(code: String) {
        syncPrefs.edit().putString("app_language", code).apply()
        context.sessionDataStore.edit { prefs ->
            prefs[KEY_APP_LANGUAGE] = code
        }
    }

    val lastMicroChallengeDate: Flow<String> = context.sessionDataStore.data.map { prefs ->
        prefs[KEY_LAST_MICRO_CHALLENGE_DATE] ?: ""
    }

    suspend fun markMicroChallengeCompleted(dateStr: String) {
        context.sessionDataStore.edit { prefs ->
            prefs[KEY_LAST_MICRO_CHALLENGE_DATE] = dateStr
        }
    }

    val isLoggedIn: Flow<Boolean> = context.sessionDataStore.data.map { prefs ->
        prefs[KEY_IS_LOGGED_IN] ?: false
    }

    val currentAccountId: Flow<Long> = context.sessionDataStore.data.map { prefs ->
        prefs[KEY_CURRENT_ACCOUNT_ID] ?: -1L
    }

    val accountId: Flow<Long?> = context.sessionDataStore.data.map { prefs ->
        val id = prefs[KEY_CURRENT_ACCOUNT_ID] ?: -1L
        if (id != -1L) id else null
    }

    val currentAccountEmail: Flow<String> = context.sessionDataStore.data.map { prefs ->
        prefs[KEY_CURRENT_ACCOUNT_EMAIL] ?: ""
    }

    val currentDisplayName: Flow<String> = context.sessionDataStore.data.map { prefs ->
        prefs[KEY_CURRENT_DISPLAY_NAME] ?: ""
    }

    suspend fun saveSession(accountId: Long, email: String, displayName: String) {
        context.sessionDataStore.edit { prefs ->
            prefs[KEY_IS_LOGGED_IN] = true
            prefs[KEY_CURRENT_ACCOUNT_ID] = accountId
            prefs[KEY_CURRENT_ACCOUNT_EMAIL] = email
            prefs[KEY_CURRENT_DISPLAY_NAME] = displayName
        }
    }

    suspend fun clearSession() {
        context.sessionDataStore.edit { prefs ->
            prefs[KEY_IS_LOGGED_IN] = false
            prefs[KEY_CURRENT_ACCOUNT_ID] = -1L
            prefs[KEY_CURRENT_ACCOUNT_EMAIL] = ""
            prefs[KEY_CURRENT_DISPLAY_NAME] = ""
        }
    }
}
