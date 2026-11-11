package com.example.util.localization

enum class AppLanguage(
    val code: String,
    val displayName: String,
    val nativeName: String,
    val subLabel: String,
    val flagEmoji: String
) {
    INDONESIAN(
        code = "id",
        displayName = "Bahasa Indonesia",
        nativeName = "Bahasa Indonesia",
        subLabel = "Bahasa Utama (Standar)",
        flagEmoji = "🇮🇩"
    ),
    ENGLISH(
        code = "en",
        displayName = "English",
        nativeName = "English",
        subLabel = "International",
        flagEmoji = "🇬🇧"
    ),
    JAPANESE(
        code = "ja",
        displayName = "Jepang",
        nativeName = "日本語",
        subLabel = "Japanese (にほんご)",
        flagEmoji = "🇯🇵"
    ),
    CHINESE(
        code = "zh",
        displayName = "China",
        nativeName = "中文",
        subLabel = "Chinese (简体中文)",
        flagEmoji = "🇨🇳"
    );

    companion object {
        val DEFAULT = INDONESIAN

        fun fromCode(code: String?): AppLanguage {
            return entries.firstOrNull { it.code.equals(code, ignoreCase = true) } ?: DEFAULT
        }
    }
}
