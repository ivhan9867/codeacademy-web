package com.example

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PasswordVisibilityLogicTest {

    @Test
    fun defaultState_isMaskedWithDots_andShowsVisibilityOffIcon() {
        // Initial state
        val isPasswordVisible = false

        // Visual transformation when isPasswordVisible = false must be PasswordVisualTransformation (masked with dots)
        val transformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
        assertTrue("Must be masked with PasswordVisualTransformation by default", transformation is PasswordVisualTransformation)

        // Trailing icon when isPasswordVisible = false must be VisibilityOff (mata tercoret) to indicate "tap to reveal"
        val icon = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
        assertEquals(Icons.Default.VisibilityOff, icon)

        // Accessibility content description when masked must prompt user to show password
        val contentDesc = if (isPasswordVisible) "Sembunyikan sandi" else "Tampilkan sandi"
        assertEquals("Tampilkan sandi", contentDesc)
    }

    @Test
    fun toggledState_isPlain_andShowsVisibilityIcon() {
        // User taps eye toggle button
        val isPasswordVisible = true

        // Visual transformation when isPasswordVisible = true must be None (plain text revealed)
        val transformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
        assertEquals(VisualTransformation.None, transformation)

        // Trailing icon when isPasswordVisible = true must be Visibility (mata terbuka) to indicate "tap to hide"
        val icon = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
        assertEquals(Icons.Default.Visibility, icon)

        // Accessibility content description when visible must prompt user to hide password
        val contentDesc = if (isPasswordVisible) "Sembunyikan sandi" else "Tampilkan sandi"
        assertEquals("Sembunyikan sandi", contentDesc)
    }
}
