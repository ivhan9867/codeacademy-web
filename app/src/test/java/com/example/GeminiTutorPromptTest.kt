package com.example

import com.example.ai.GeminiTutorService
import org.junit.Assert.assertTrue
import org.junit.Test

class GeminiTutorPromptTest {

    private val service = GeminiTutorService()

    @Test
    fun buildSystemPrompt_indonesian_containsTargetLanguage() {
        val prompt = GeminiTutorService.buildSystemPrompt("HTML", "Pengenalan HTML", language = "id")
        assertTrue(prompt.contains("Bahasa Indonesia"))
        assertTrue(prompt.contains("Anda adalah AI Tutor resmi"))
    }

    @Test
    fun buildSystemPrompt_english_containsTargetLanguage() {
        val prompt = GeminiTutorService.buildSystemPrompt("HTML", "Pengenalan HTML", language = "en")
        assertTrue(prompt.contains("English"))
    }

    @Test
    fun buildSystemPrompt_japanese_containsTargetLanguage() {
        val prompt = GeminiTutorService.buildSystemPrompt("HTML", "Pengenalan HTML", language = "ja")
        assertTrue(prompt.contains("Japanese"))
    }

    @Test
    fun buildSystemPrompt_chinese_containsTargetLanguage() {
        val prompt = GeminiTutorService.buildSystemPrompt("HTML", "Pengenalan HTML", language = "zh")
        assertTrue(prompt.contains("Chinese"))
    }

    @Test
    fun buildSystemPrompt_unknownLanguage_defaultsToIndonesian() {
        val prompt = GeminiTutorService.buildSystemPrompt("HTML", "Pengenalan HTML", language = "fr")
        assertTrue(prompt.contains("Bahasa Indonesia"))
    }
}
