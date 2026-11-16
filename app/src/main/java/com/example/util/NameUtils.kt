package com.example.util

import java.util.Locale

object NameUtils {
    private val LEADING_NUMBER_OR_PUNCT_REGEX = Regex("^[0-9.\\-_)(#\\s]+")
    private val MULTI_SPACE_REGEX = Regex("\\s+")

    /**
     * Cleans a display name by removing leading numbering/punctuation (e.g. "11. Ivhan Afika P" -> "Ivhan Afika P")
     * and collapsing extra spaces.
     */
    fun sanitizeDisplayName(raw: String?, fallback: String = "Siswa"): String {
        if (raw.isNullOrBlank()) return fallback
        val stripped = raw.trim()
            .replace(LEADING_NUMBER_OR_PUNCT_REGEX, "")
            .replace(MULTI_SPACE_REGEX, " ")
            .trim()
        return stripped.ifBlank { fallback }
    }

    /**
     * Extracts a clean uppercase initial letter from a user's display name (never a digit).
     */
    fun extractInitial(rawName: String?, fallback: String = "S"): String {
        val clean = sanitizeDisplayName(rawName, fallback)
        val firstLetter = clean.firstOrNull { it.isLetter() } ?: clean.firstOrNull()
        return firstLetter?.uppercaseChar()?.toString() ?: fallback
    }

    /**
     * Validates a user-entered display name in Edit Profile / Registration.
     */
    fun isValidDisplayName(raw: String?): Boolean {
        if (raw.isNullOrBlank()) return false
        val trimmed = raw.trim()
        if (trimmed.length < 2) return false
        if (LEADING_NUMBER_OR_PUNCT_REGEX.containsMatchIn(trimmed)) return false
        return trimmed.any { it.isLetter() }
    }

    /**
     * Formats duration in minutes into a friendly localized string (e.g. 573 -> "9 jam 33 mnt").
     */
    fun formatDurationFriendly(minutes: Int, languageCode: String = Locale.getDefault().language): String {
        val safeMinutes = minutes.coerceAtLeast(0)
        val hours = safeMinutes / 60
        val remMinutes = safeMinutes % 60
        return when (languageCode.lowercase(Locale.ROOT)) {
            "en" -> when {
                hours > 0 && remMinutes > 0 -> "${hours}h ${remMinutes}m"
                hours > 0 -> "${hours}h"
                else -> "${remMinutes}m"
            }
            "ja" -> when {
                hours > 0 && remMinutes > 0 -> "${hours}時間${remMinutes}分"
                hours > 0 -> "${hours}時間"
                else -> "${remMinutes}分"
            }
            "zh" -> when {
                hours > 0 && remMinutes > 0 -> "${hours}小时${remMinutes}分钟"
                hours > 0 -> "${hours}小时"
                else -> "${remMinutes}分钟"
            }
            else -> when {
                hours > 0 && remMinutes > 0 -> "$hours jam $remMinutes mnt"
                hours > 0 -> "$hours jam"
                else -> "$remMinutes mnt"
            }
        }
    }
}
