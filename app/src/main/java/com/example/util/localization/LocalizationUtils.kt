package com.example.util.localization

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
/ * Converts AppLanguage enum to Java Locale.
 */
fun AppLanguage.toLocale(): Locale = when (this) {
    AppLanguage.INDONESIAN -> Locale("id", "ID")
    AppLanguage.ENGLISH -> Locale("en", "US")
    AppLanguage.JAPANESE -> Locale("ja", "JP")
    AppLanguage.CHINESE -> Locale("zh", "CN")
}

/**
 * Formats timestamp (or Date) based on active AppLanguage.
 */
fun formatDate(timestampMs: Long, pattern: String, language: AppLanguage = AppLanguage.INDONESIAN): String {
    val sdf = SimpleDateFormat(pattern, language.toLocale())
    return sdf.format(Date(timestampMs))
}

fun formatDate(date: Date, pattern: String, language: AppLanguage = AppLanguage.INDONESIAN): String {
    val sdf = SimpleDateFormat(pattern, language.toLocale())
    return sdf.format(date)
}

/**
 * Formats a number according to active AppLanguage locale.
 */
fun formatNumber(number: Number, language: AppLanguage = AppLanguage.INDONESIAN): String {
    val nf = NumberFormat.getInstance(language.toLocale())
    return nf.format(number)
}
