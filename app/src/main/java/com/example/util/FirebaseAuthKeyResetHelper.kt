package com.example.util

import android.content.Context
import android.util.Log
import java.io.File

/**
 * Self-healing utility to clear corrupted Firebase Auth Tink encryption keysets
 * in SharedPreferences when Android Keystore fails to decrypt/generate public keys
 * (e.g., "Failed to generate/retrieve public encryption key" after APK reinstall).
 */
object FirebaseAuthKeyResetHelper {

    private const val TAG = "FirebaseAuthKeyReset"

    private val CRYPTO_PREF_PREFIXES = listOf(
        "com.google.firebase.auth.api.crypto",
        "com.google.firebase.auth.internal.ProcessDeathHelper"
    )

    fun isKeystoreOrPublicKeyError(message: String?): Boolean {
        if (message.isNullOrBlank()) return false
        return message.contains("generate/retrieve public", ignoreCase = true) ||
            message.contains("public encryption key", ignoreCase = true) ||
            message.contains("encryption key", ignoreCase = true) ||
            message.contains("KeyStore", ignoreCase = true) ||
            message.contains("AEADBadTagException", ignoreCase = true)
    }

    fun clearCorruptedAuthKeysets(context: Context): Int {
        var clearedCount = 0
        runCatching {
            val prefsDir = File(context.applicationInfo.dataDir, "shared_prefs")
            if (!prefsDir.exists() || !prefsDir.isDirectory) return@runCatching
            val files = prefsDir.listFiles() ?: return@runCatching
            for (file in files) {
                val fileName = file.name
                if (!fileName.endsWith(".xml")) continue
                val prefName = fileName.removeSuffix(".xml")
                val shouldClear = CRYPTO_PREF_PREFIXES.any { prefix ->
                    prefName.startsWith(prefix)
                }
                if (shouldClear) {
                    runCatching {
                        context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
                            .edit()
                            .clear()
                            .commit()
                    }
                    runCatching {
                        context.deleteSharedPreferences(prefName)
                    }
                    clearedCount++
                }
            }
        }.onFailure { err ->
            Log.w(TAG, "Failed to inspect/clear Firebase Auth crypto prefs", err)
        }
        Log.i(TAG, "Cleared $clearedCount corrupted Firebase Auth crypto preference files")
        return clearedCount
    }
}
