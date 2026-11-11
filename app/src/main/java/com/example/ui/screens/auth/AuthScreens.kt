package com.example.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TrackEntity
import com.example.ui.theme.*

@Composable
fun LoginScreen(
    onPerformLogin: (email: String, password: String, onResult: (Boolean, String?) -> Unit) -> Unit,
    onLoginSuccess: (Boolean) -> Unit, // passes isOnboarded
    onNavigateToRegister: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    fun submitLogin() {
        errorMessage = null
        val cleanEmail = email.trim()
        val cleanPassword = password

        if (cleanEmail.isEmpty()) {
            errorMessage = "Silakan masukkan alamat email Anda."
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches()) {
            errorMessage = "Format email tidak valid (contoh: siswa@domain.com)."
            return
        }
        if (cleanPassword.isEmpty()) {
            errorMessage = "Silakan masukkan kata sandi akun Anda."
            return
        }
        if (cleanPassword.length < 6) {
            errorMessage = "Kata sandi minimal harus 6 karakter."
            return
        }

        isLoading = true
        onPerformLogin(cleanEmail, cleanPassword) { success, error ->
            isLoading = false
            if (success) {
                onLoginSuccess(true)
            } else {
                errorMessage = error ?: "Gagal masuk. Periksa kembali email dan kata sandi Anda."
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Branding Logo
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Brush.linearGradient(listOf(PrimaryPurple, SecondaryTeal))),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "</>", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "CodeAcademy",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
            )
            Text(
                text = "Platform Belajar Koding Terintegrasi Supabase",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondaryDark,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Error banner if any
            if (errorMessage != null) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = AccentError.copy(alpha = 0.15f),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(AccentError, AccentError))),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.ErrorOutline, contentDescription = null, tint = AccentError)
                        Text(text = errorMessage!!, color = AccentError, style = MaterialTheme.typography.bodyMedium)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Input Email (No default prefill)
            OutlinedTextField(
                value = email,
                onValueChange = { email = it; errorMessage = null },
                label = { Text("Email Siswa") },
                placeholder = { Text("contoh@email.com", color = TextSecondaryDark.copy(alpha = 0.5f)) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = SecondaryTeal) },
                singleLine = true,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryPurple,
                    unfocusedBorderColor = BorderDark,
                    focusedContainerColor = SurfaceDark,
                    unfocusedContainerColor = SurfaceDark
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input Password (No default prefill)
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; errorMessage = null },
                label = { Text("Kata Sandi") },
                placeholder = { Text("Minimal 6 karakter", color = TextSecondaryDark.copy(alpha = 0.5f)) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = SecondaryTeal) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Sembunyikan sandi" else "Tampilkan sandi",
                            tint = TextSecondaryDark
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryPurple,
                    unfocusedBorderColor = BorderDark,
                    focusedContainerColor = SurfaceDark,
                    unfocusedContainerColor = SurfaceDark
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Login Button
            Button(
                onClick = { submitLogin() },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                } else {
                    Text(
                        text = "Masuk ke Akun",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Belum punya akun? ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondaryDark
                )
                Text(
                    text = "Daftar Sekarang",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = SecondaryTeal,
                    modifier = Modifier.clickable { if (!isLoading) onNavigateToRegister() }
                )
            }
        }
    }
}

@Composable
fun RegisterScreen(
    onPerformRegister: (email: String, password: String, displayName: String, onResult: (Boolean, String?) -> Unit) -> Unit,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    fun submitRegister() {
        errorMessage = null
        val cleanName = name.trim()
        val cleanEmail = email.trim()
        val cleanPassword = password

        if (cleanName.isEmpty()) {
            errorMessage = "Nama lengkap tidak boleh kosong."
            return
        }
        if (cleanEmail.isEmpty()) {
            errorMessage = "Silakan masukkan alamat email Anda."
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches()) {
            errorMessage = "Format email tidak valid (contoh: siswa@domain.com)."
            return
        }
        if (cleanPassword.length < 6) {
            errorMessage = "Kata sandi minimal harus 6 karakter."
            return
        }

        isLoading = true
        onPerformRegister(cleanEmail, cleanPassword, cleanName) { success, error ->
            isLoading = false
            if (success) {
                onRegisterSuccess()
            } else {
                errorMessage = error ?: "Pendaftaran gagal. Silakan coba kembali."
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Logo
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Brush.linearGradient(listOf(SecondaryTeal, PrimaryPurple))),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🚀", fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Buat Akun Baru",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
            )
            Text(
                text = "Mulai belajar koding dan tersambung ke Supabase Cloud",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondaryDark,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (errorMessage != null) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = AccentError.copy(alpha = 0.15f),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(AccentError, AccentError))),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.ErrorOutline, contentDescription = null, tint = AccentError)
                        Text(text = errorMessage!!, color = AccentError, style = MaterialTheme.typography.bodyMedium)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Input Name
            OutlinedTextField(
                value = name,
                onValueChange = { name = it; errorMessage = null },
                label = { Text("Nama Lengkap") },
                placeholder = { Text("Budi Raharja", color = TextSecondaryDark.copy(alpha = 0.5f)) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = SecondaryTeal) },
                singleLine = true,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryPurple,
                    unfocusedBorderColor = BorderDark,
                    focusedContainerColor = SurfaceDark,
                    unfocusedContainerColor = SurfaceDark
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Input Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it; errorMessage = null },
                label = { Text("Email Siswa") },
                placeholder = { Text("siswa@domain.com", color = TextSecondaryDark.copy(alpha = 0.5f)) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = SecondaryTeal) },
                singleLine = true,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryPurple,
                    unfocusedBorderColor = BorderDark,
                    focusedContainerColor = SurfaceDark,
                    unfocusedContainerColor = SurfaceDark
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Input Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; errorMessage = null },
                label = { Text("Kata Sandi (Min 6 Karakter)") },
                placeholder = { Text("Minimal 6 karakter", color = TextSecondaryDark.copy(alpha = 0.5f)) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = SecondaryTeal) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Sembunyikan sandi" else "Tampilkan sandi",
                            tint = TextSecondaryDark
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryPurple,
                    unfocusedBorderColor = BorderDark,
                    focusedContainerColor = SurfaceDark,
                    unfocusedContainerColor = SurfaceDark
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { submitRegister() },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                } else {
                    Text(
                        text = "Daftar & Lanjut ke Onboarding",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Sudah punya akun? ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondaryDark
                )
                Text(
                    text = "Masuk di sini",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = SecondaryTeal,
                    modifier = Modifier.clickable { if (!isLoading) onNavigateToLogin() }
                )
            }
        }
    }
}

@Composable
fun OnboardingScreen(
    tracks: List<TrackEntity>,
    onCompleteOnboarding: (List<String>, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedTracks = remember { mutableStateListOf("front-end", "python") }
    var selectedGoal by remember { mutableStateOf("Persiapan Karier") }

    val goals = listOf(
        "Persiapan Karier Software Engineer 💼",
        "Upgrade Skill Pemrograman 🚀",
        "Tugas Kuliah / Proyek Sekolah 🎓",
        "Eksplorasi Hobi & Proyek Kreatif ✨"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Personalisasi Belajarmu 🎯",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimaryDark
        )
        Text(
            text = "Pilih topik kurikulum yang ingin kamu kuasai dan tujuan belajarmu.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondaryDark
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "1. Pilih Track Minat (Bisa lebih dari 1):",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = SecondaryTeal
        )

        Spacer(modifier = Modifier.height(12.dp))

        val visibleTracks = remember(tracks) { tracks.filter { it.id != "ui-ux" } }
        visibleTracks.forEach { track ->
            val isSelected = selectedTracks.contains(track.id)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable {
                        if (isSelected) {
                            if (selectedTracks.size > 1) selectedTracks.remove(track.id)
                        } else {
                            selectedTracks.add(track.id)
                        }
                    },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) SurfaceDark else SurfaceVariantDark.copy(alpha = 0.5f)
                ),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = if (isSelected) Brush.horizontalGradient(listOf(PrimaryPurple, SecondaryTeal))
                    else Brush.horizontalGradient(listOf(BorderDark, BorderDark))
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(text = track.icon, fontSize = 28.sp)
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = track.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )
                        Text(
                            text = track.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondaryDark
                        )
                    }
                    Checkbox(
                        checked = isSelected,
                        onCheckedChange = null,
                        colors = CheckboxDefaults.colors(checkedColor = PrimaryPurple)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "2. Apa Tujuan Utama Belajarmu?",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = SecondaryTeal
        )

        Spacer(modifier = Modifier.height(12.dp))

        goals.forEach { goal ->
            val isSelected = selectedGoal == goal
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { selectedGoal = goal },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) SurfaceDark else SurfaceVariantDark.copy(alpha = 0.4f)
                ),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = if (isSelected) Brush.horizontalGradient(listOf(PrimaryPurple, SecondaryTeal))
                    else Brush.horizontalGradient(listOf(BorderDark, BorderDark))
                )
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = goal,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) TextPrimaryDark else TextSecondaryDark
                    )
                    RadioButton(
                        selected = isSelected,
                        onClick = null,
                        colors = RadioButtonDefaults.colors(selectedColor = SecondaryTeal)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = {
                onCompleteOnboarding(selectedTracks.toList(), selectedGoal)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
        ) {
            Text(
                text = "Mulai Petualangan Belajar 🚀",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
