package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.BuildConfig
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.TealDark
import com.example.ui.theme.TealLight
import com.example.ui.theme.TealPrimary
import androidx.compose.ui.res.stringResource
import com.example.R

/**
 * Model representing a single FAQ item.
 */
data class FaqItem(
    val id: String,
    val category: String,
    val question: String,
    val answer: String
)

@Composable
fun HelpScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val isDark = isSystemInDarkTheme()
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0.dp),
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier
                                .testTag("help_back_button")
                                .size(44.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.common_back),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = stringResource(R.string.help_title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // 3 Category Tabs
                    ScrollableTabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = if (isDark) CyanAccent else TealPrimary,
                        edgePadding = 16.dp,
                        indicator = { tabPositions ->
                            if (selectedTab < tabPositions.size) {
                                TabRowDefaults.SecondaryIndicator(
                                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                    color = if (isDark) CyanAccent else TealPrimary,
                                    height = 3.dp
                                )
                            }
                        },
                        divider = {}
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = {
                                Text(
                                    text = stringResource(R.string.help_tab_faq),
                                    fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 14.sp
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.HelpOutline,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        )

                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = {
                                Text(
                                    text = stringResource(R.string.help_tab_privacy),
                                    fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 14.sp
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Policy,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        )

                        Tab(
                            selected = selectedTab == 2,
                            onClick = { selectedTab = 2 },
                            text = {
                                Text(
                                    text = stringResource(R.string.help_tab_feedback),
                                    fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 14.sp
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (selectedTab) {
                0 -> FaqSection()
                1 -> PrivacyTermsSection()
                2 -> ContactFeedbackSection(versionName = BuildConfig.VERSION_NAME)
            }
        }
    }
}

/**
 * Section A: FAQ Accordion / Expandable List with search filter.
 */
@Composable
private fun FaqSection() {
    val isDark = isSystemInDarkTheme()
    val currentLanguage = com.example.util.localization.LocalAppLanguage.current
    var searchQuery by remember { mutableStateOf("") }
    val expandedStates = remember { mutableStateMapOf<String, Boolean>() }

    val allFaqItems = remember(currentLanguage) {
        if (currentLanguage == com.example.util.localization.AppLanguage.INDONESIAN) {
            listOf(
                FaqItem(
                    id = "faq_placement",
                    category = "Placement Test",
                    question = "Bagaimana cara kerja placement test, dan apakah saya bisa mengulanginya?",
                    answer = "Placement test adalah asesmen adaptif singkat untuk mengukur kemampuan awal koding kamu. Jika lulus dengan skor tinggi, modul-modul materi dasar di jalur belajarmu akan otomatis ditandai selesai (fast-tracked) sehingga kamu bisa langsung melompat ke materi inti. Kamu bisa mengulangi tes ini kapan saja lewat menu Pengaturan Profil > 'Restart Onboarding / Tes Diagnostik'."
                ),
                FaqItem(
                    id = "faq_local_progress",
                    category = "Progres & Data",
                    question = "Kenapa progres belajar saya tidak muncul lagi setelah ganti HP atau install ulang?",
                    answer = "Seluruh progres belajar, modul yang telah diselesaikan, riwayat section quiz, serta catatan kode kamu disimpan secara lokal di memori perangkat menggunakan Room Database (SQLite). Karena data tidak disinkronkan ke cloud secara penuh, mencopot aplikasi atau berpindah perangkat akan mereset data lokal ke kondisi awal."
                ),
                FaqItem(
                    id = "faq_quiz_exit",
                    category = "Section Quiz & Fokus",
                    question = "Kenapa quiz saya otomatis dibatalkan/gagal padahal saya cuma sebentar keluar aplikasi?",
                    answer = "Untuk menjaga kejujuran dan fokus penuh saat Section Quiz berlangsung, aplikasi menerapkan sistem deteksi fokus:\n\n• Pelanggaran Pertama: Ketika aplikasi keluar foreground (misal membuka Recent Apps, Home, atau notifikasi), kamu masih mendapat toleransi berupa dialog peringatan saat kembali ke quiz.\n• Pelanggaran Kedua: Quiz akan otomatis dibatalkan, dicatat gagal dengan alasan 'FOCUS_VIOLATION', dan memicu fail-streak.\n\nCatatan: Memutar layar atau rotasi perangkat diizinkan dan tidak dihitung sebagai pelanggaran."
                ),
                FaqItem(
                    id = "faq_certificate",
                    category = "Sertifikat",
                    question = "Bagaimana cara mendapatkan sertifikat setelah menyelesaikan sebuah kelas?",
                    answer = "Setelah kamu menyelesaikan seluruh modul dalam suatu kelas (100%) dan lulus kuis evaluasi akhir/section quiz, kamu dapat langsung membuka layar 'Hasil Evaluasi' atau mengunjungi menu 'Sertifikat Kelulusan' di profil akun. Sertifikat diterbitkan dengan ID verifikasi unik yang dapat kamu simpan atau unduh."
                ),
                FaqItem(
                    id = "faq_playground",
                    category = "Playground",
                    question = "Apakah kode yang saya tulis di Playground tersimpan otomatis?",
                    answer = "Kode yang sedang kamu ketik di Playground tersimpan selama sesi aplikasi aktif. Namun, jika kamu ingin mengembalikan kode ke contoh template modul aslinya, kamu cukup mengetuk tombol 'Reset' di pojok kanan atas layar Playground."
                ),
                FaqItem(
                    id = "faq_ai_tutor",
                    category = "AI Tutor",
                    question = "Apa bedanya AI Tutor dengan mengerjakan soal sendiri — apakah AI Tutor akan memberi jawaban langsung?",
                    answer = "AI Tutor dirancang sebagai mentor belajar yang menggunakan metode bimbingan terarah (Socratic guidance). AI Tutor akan memberikan analogi konsep, petunjuk alur logika, dan penjelasan penyebab bug/error, bukan langsung menyuapkan jawaban akhir. Hal ini bertujuan agar pemahaman problem-solving kamu berkembang maksimal."
                ),
                FaqItem(
                    id = "faq_forum",
                    category = "Forum Komunitas",
                    question = "Bagaimana cara ikut forum diskusi kelas, dan apakah postingan saya terlihat oleh murid lain?",
                    answer = "Ya! Tab Diskusi/Forum terhubung ke Firebase Firestore cloud. Setiap pertanyaan, kendala kode, atau jawaban diskusi yang kamu kirimkan akan dapat dilihat dan direspons oleh seluruh murid yang sedang mempelajari kelas tersebut secara real-time."
                ),
                FaqItem(
                    id = "faq_password_reset",
                    category = "Akun & Keamanan",
                    question = "Saya lupa kata sandi akun saya, bagaimana cara reset?",
                    answer = "Pada layar Masuk (Login), pilih opsi 'Lupa Kata Sandi'. Karena akun disimpan di database lokal perangkat, kamu dapat mereset PIN/kata sandi akun lokal dengan memasukkan email terdaftar, atau mendaftarkan akun lokal baru tanpa kehilangan unduhan modul pembelajaran."
                )
            )
        } else {
            listOf(
                FaqItem(
                    id = "faq_placement",
                    category = "Placement Test",
                    question = "How does the placement test work, and can I retake it?",
                    answer = "The placement test is a short adaptive assessment that measures your initial coding level. If you pass with a high score, foundational modules in your learning track are automatically marked completed (fast-tracked) so you can jump straight into core topics. You can retake it anytime from Profile Settings > 'Restart Onboarding / Diagnostic Test'."
                ),
                FaqItem(
                    id = "faq_local_progress",
                    category = "Progress & Data",
                    question = "Why is my learning progress missing after switching phones or reinstalling?",
                    answer = "Your learning progress, completed modules, section quiz history, and code drafts are stored locally on your device using Room Database (SQLite). Because local learning data is not fully mirrored to the cloud, uninstalling the app or switching devices resets local progress."
                ),
                FaqItem(
                    id = "faq_quiz_exit",
                    category = "Section Quiz & Focus",
                    question = "Why was my quiz automatically cancelled/failed when I briefly left the app?",
                    answer = "To preserve integrity and focus during a Section Quiz, the app monitors foreground focus:\n\n• First Violation: Leaving the foreground (opening Recent Apps, Home, or notifications) shows a warning dialog when you return.\n• Second Violation: The quiz is automatically cancelled, recorded as failed with reason 'FOCUS_VIOLATION', and increments your fail-streak.\n\nNote: Rotating your screen is allowed and is not counted as a violation."
                ),
                FaqItem(
                    id = "faq_certificate",
                    category = "Certificates",
                    question = "How do I earn a certificate after finishing a course?",
                    answer = "Once you complete 100% of the modules in a course and pass the required section quizzes and final evaluation, open the Evaluation Results screen or visit 'Graduation Certificates' in your Profile. Each certificate includes a unique verification ID you can save or share."
                ),
                FaqItem(
                    id = "faq_playground",
                    category = "Playground",
                    question = "Is the code I write in the Playground saved automatically?",
                    answer = "Yes, your Playground code is saved during your active session. If you want to restore the original module starter template at any time, tap the 'Reset' button in the top-right corner of the Playground."
                ),
                FaqItem(
                    id = "faq_ai_tutor",
                    category = "AI Tutor",
                    question = "How is the AI Tutor different from solving exercises myself — does it give direct answers?",
                    answer = "The AI Tutor acts as a mentor using Socratic guidance. Instead of handing out final answers right away, it explains concepts with analogies, walks through logic step-by-step, and helps you diagnose bugs so your problem-solving skills grow."
                ),
                FaqItem(
                    id = "faq_forum",
                    category = "Community Forum",
                    question = "How do I join a course discussion forum, and are my posts visible to other students?",
                    answer = "Yes! The Discussion/Forum tab is connected to Firebase Firestore in the cloud. Every question, code issue, or reply you post is visible in real time to other learners taking the same course."
                ),
                FaqItem(
                    id = "faq_password_reset",
                    category = "Account & Security",
                    question = "I forgot my account password. How can I reset it?",
                    answer = "On the Login screen, choose 'Forgot Password'. Because accounts are stored in your device's local database, you can reset your local PIN/password using your registered email, or register a new local account without losing downloaded course modules."
                )
            )
        }
    }

    val filteredItems = remember(searchQuery, allFaqItems) {
        val q = searchQuery.trim().lowercase()
        if (q.isEmpty()) {
            allFaqItems
        } else {
            allFaqItems.filter {
                it.question.lowercase().contains(q) ||
                it.answer.lowercase().contains(q) ||
                it.category.lowercase().contains(q)
            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            // Search Input for FAQ
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = stringResource(R.string.help_search_placeholder),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = if (isDark) CyanAccent else TealPrimary
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (isDark) CyanAccent else TealPrimary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("faq_search_field")
            )
            Spacer(modifier = Modifier.height(6.dp))
        }

        if (filteredItems.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.HelpOutline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = stringResource(R.string.help_not_found_title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.help_not_found_desc),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(filteredItems, key = { it.id }) { faq ->
                val isExpanded = expandedStates[faq.id] ?: false

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { expandedStates[faq.id] = !isExpanded }
                        .testTag("faq_item_${faq.id}"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isExpanded) {
                            (if (isDark) CyanAccent else TealPrimary).copy(alpha = 0.5f)
                        } else {
                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                        }
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                // Category badge
                                Surface(
                                    color = (if (isDark) TealLight else TealPrimary).copy(alpha = 0.12f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = faq.category,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDark) CyanAccent else TealPrimary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = faq.question,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 20.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isExpanded) {
                                            (if (isDark) CyanAccent else TealPrimary).copy(alpha = 0.15f)
                                        } else {
                                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = if (isExpanded) stringResource(R.string.help_faq_close) else stringResource(R.string.help_faq_open),
                                    tint = if (isExpanded) (if (isDark) CyanAccent else TealPrimary) else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        AnimatedVisibility(
                            visible = isExpanded,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            Column {
                                Spacer(modifier = Modifier.height(12.dp))
                                Surface(
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = faq.answer,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 21.sp,
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Section B: Kebijakan Privasi & Ketentuan Layanan (transparan berbasis arsitektur data asli).
 */
@Composable
private fun PrivacyTermsSection() {
    val isDark = isSystemInDarkTheme()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Status Badge Header Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background((if (isDark) TealLight else TealPrimary).copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = if (isDark) CyanAccent else TealPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = stringResource(R.string.help_privacy_header_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = stringResource(R.string.help_privacy_draft_subtitle),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Section 1: Data Lokal (Room Database)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Storage,
                        contentDescription = null,
                        tint = if (isDark) CyanAccent else TealPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.help_privacy_sec1_title),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(R.string.help_privacy_s1_intro),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                BulletItem(text = stringResource(R.string.help_privacy_s1_b1))
                BulletItem(text = stringResource(R.string.help_privacy_s1_b2))
                BulletItem(text = stringResource(R.string.help_privacy_s1_b3))
                BulletItem(text = stringResource(R.string.help_privacy_s1_b4))
                BulletItem(text = stringResource(R.string.help_privacy_s1_b5))
            }
        }

        // Section 2: Data Cloud (Firebase Firestore)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Policy,
                        contentDescription = null,
                        tint = FlameOrange,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.help_privacy_sec2_title),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(R.string.help_privacy_s2_intro),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                BulletItem(text = stringResource(R.string.help_privacy_s2_b1))
                BulletItem(text = stringResource(R.string.help_privacy_s2_b2))
            }
        }

        // Section 3: Penghapusan & Kontrol Pengguna
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.help_privacy_s3_title),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.help_privacy_s3_body),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )
            }
        }

        // Notice Disclaimer
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.help_privacy_draft_note),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 17.sp,
                modifier = Modifier.padding(14.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

/**
 * Section C: Kontak Dukungan, Feedback Email via Intent ACTION_SENDTO, dan Info Versi Aplikasi.
 */
@Composable
private fun ContactFeedbackSection(versionName: String) {
    val context = LocalContext.current
    val isDark = isSystemInDarkTheme()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Info & Version Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background((if (isDark) TealLight else TealPrimary).copy(alpha = 0.16f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = if (isDark) CyanAccent else TealPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "CodeAcademy",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    color = (if (isDark) CyanAccent else TealPrimary).copy(alpha = 0.12f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "${stringResource(R.string.help_app_version_label)} $versionName",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) CyanAccent else TealPrimary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = stringResource(R.string.help_about_desc),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )
            }
        }

        // Contact Support Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.help_contact_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = stringResource(R.string.help_contact_desc),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = if (isDark) CyanAccent else TealPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = stringResource(R.string.help_contact_email_label),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "support@codeacademy.app",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:support@codeacademy.app")
                            putExtra(
                                Intent.EXTRA_SUBJECT,
                                context.getString(R.string.help_email_subject_format, versionName)
                            )
                            putExtra(
                                Intent.EXTRA_TEXT,
                                context.getString(R.string.help_email_body_format, versionName)
                            )
                        }
                        try {
                            context.startActivity(
                                Intent.createChooser(emailIntent, context.getString(R.string.help_email_chooser_title))
                            )
                        } catch (e: Exception) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.help_email_no_app),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_send_feedback_email"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TealPrimary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.help_send_feedback_button),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun BulletItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "•",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 18.sp,
            modifier = Modifier.weight(1f)
        )
    }
}
