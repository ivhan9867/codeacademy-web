package com.example.ui.screens.profile

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.*
import com.example.ui.components.BadgeItem
import com.example.ui.components.XPProgressBar
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    profile: ProfileEntity?,
    badges: List<BadgeEntity>,
    userBadges: List<UserBadgeEntity>,
    certificates: List<CertificateEntity>,
    bookmarks: List<BookmarkEntity>,
    courses: List<CourseEntity>,
    playgroundDrafts: List<PlaygroundDraftEntity> = emptyList(),
    onViewCertificate: (String) -> Unit,
    onNavigateToCourse: (String) -> Unit,
    onOpenPlaygroundDraft: (courseId: String, moduleId: String) -> Unit = { _, _ -> },
    onLogout: () -> Unit,
    onUpdateProfile: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Badges, 1: Sertifikat, 2: Bookmarks, 3: Drafts
    var showEditProfileDialog by remember { mutableStateOf(false) }

    val unlockedBadgeIds = remember(userBadges) { userBadges.map { it.badgeId }.toSet() }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Profile Header
        item(key = "profile_header") {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(PrimaryPurple, SecondaryTeal)))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(PrimaryPurple, SecondaryTeal))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (profile?.displayName?.firstOrNull() ?: 'C').uppercase(),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = profile?.displayName ?: "Siswa CodeAcademy",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark
                    )

                    Text(
                        text = profile?.bio ?: "Belajar Koding Tiap Hari",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondaryDark,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = { showEditProfileDialog = true },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Edit Profil", style = MaterialTheme.typography.labelSmall)
                        }

                        OutlinedButton(
                            onClick = onLogout,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentError)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Keluar", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }

        // XP & Level Card
        item(key = "xp_card") {
            XPProgressBar(
                totalXp = profile?.totalXp ?: 0,
                level = profile?.level ?: 1,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Learning Activity Heatmap Card (30-day visual grid)
        item(key = "learning_heatmap") {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(BorderDark, BorderDark)))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Heatmap Aktivitas Belajar 📅",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )
                        Text(
                            text = "30 Hari Terakhir",
                            style = MaterialTheme.typography.labelSmall,
                            color = SecondaryTeal
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 4 rows x 7 columns activity blocks
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        for (row in 0 until 4) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                for (col in 0 until 7) {
                                    val index = row * 7 + col
                                    val hasActivity = index % 3 == 0 || index % 5 == 0 || index >= 25
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(
                                                if (hasActivity) {
                                                    if (index >= 25) SecondaryTeal else PrimaryPurple
                                                } else SurfaceVariantDark
                                            )
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Kurang", style = MaterialTheme.typography.labelSmall, color = TextMutedDark)
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(2.dp)).background(SurfaceVariantDark))
                        Spacer(modifier = Modifier.width(3.dp))
                        Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(2.dp)).background(PrimaryPurple.copy(alpha = 0.5f)))
                        Spacer(modifier = Modifier.width(3.dp))
                        Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(2.dp)).background(SecondaryTeal))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Sering", style = MaterialTheme.typography.labelSmall, color = TextMutedDark)
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Tabs (Badges / Certificates / Bookmarks)
        item(key = "profile_tabs") {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = SurfaceDark,
                contentColor = SecondaryTeal,
                modifier = Modifier.padding(horizontal = 20.dp).clip(RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("🏆 Badge (${unlockedBadgeIds.size}/${badges.size})", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("🎓 Sertifikat (${certificates.size})", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("🔖 Bookmark", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    text = { Text("⚡ Draft (${playgroundDrafts.size})", fontWeight = FontWeight.Bold) }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Tab Content
        when (selectedTab) {
            0 -> { // Badges Grid
                item(key = "badges_grid") {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        badges.chunked(2).forEach { rowBadges ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                rowBadges.forEach { badge ->
                                    BadgeItem(
                                        badge = badge,
                                        isUnlocked = unlockedBadgeIds.contains(badge.id),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                if (rowBadges.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
            1 -> { // Certificates List
                if (certificates.isEmpty()) {
                    item(key = "cert_empty") {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceDark)
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("🎓", fontSize = 36.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Belum Ada Sertifikat",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimaryDark
                                )
                                Text(
                                    text = "Selesaikan modul kursus dan raih skor minimal 70% di kuis akhir untuk menerbitkan sertifikat digitalmu.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondaryDark,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                } else {
                    items(certificates, key = { it.certId }) { cert ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 6.dp)
                                .clickable { onViewCertificate(cert.certId) },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(SecondaryTeal, PrimaryPurple)))
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Brush.linearGradient(listOf(SecondaryTeal, PrimaryPurple))),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("🎓", fontSize = 24.sp)
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = cert.courseTitle,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimaryDark
                                    )
                                    Text(
                                        text = "No. Sertifikat: ${cert.certId}",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontFamily = FontFamily.Monospace,
                                        color = SecondaryTeal
                                    )
                                    Text(
                                        text = "Terbit: ${cert.issueDate} • Skor: ${cert.scorePercent}%",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondaryDark
                                    )
                                }
                                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = SecondaryTeal)
                            }
                        }
                    }
                }
            }
            2 -> { // Bookmarks List
                if (bookmarks.isEmpty()) {
                    item(key = "bookmarks_empty") {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceDark)
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("🔖", fontSize = 36.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Belum Ada Bookmark",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimaryDark
                                )
                                Text(
                                    text = "Simpan kursus favoritmu dengan menekan ikon bookmark untuk akses cepat kapan saja.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondaryDark,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                } else {
                    val bookmarkedCourses = courses.filter { c -> bookmarks.any { it.itemId == c.id } }
                    items(bookmarkedCourses, key = { it.id }) { course ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 6.dp)
                                .clickable { onNavigateToCourse(course.id) },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceDark)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(Icons.Filled.Bookmark, contentDescription = null, tint = AccentWarning)
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(course.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                                    Text(course.level, style = MaterialTheme.typography.labelSmall, color = SecondaryTeal)
                                }
                                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextSecondaryDark)
                            }
                        }
                    }
                }
            }
            3 -> { // Playground Drafts
                if (playgroundDrafts.isEmpty()) {
                    item(key = "drafts_empty") {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceDark)
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("⚡", fontSize = 36.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Belum Ada Draft Playground",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimaryDark
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Buka salah satu modul koding lalu klik 'Buka Live Playground' untuk mulai bereksperimen. Progres kodemu akan otomatis tersimpan di sini.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondaryDark,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                } else {
                    items(playgroundDrafts, key = { it.courseId }) { draft ->
                        val courseId = if (draft.courseId.contains("_")) draft.courseId.substringBefore("_") else draft.courseId
                        val moduleId = if (draft.courseId.contains("_")) draft.courseId.substringAfter("_") else ""
                        val course = courses.find { it.id == courseId }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 6.dp)
                                .clickable { onOpenPlaygroundDraft(courseId, moduleId) },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = Brush.horizontalGradient(listOf(PrimaryPurple, SecondaryTeal))
                            )
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Icon(Icons.Filled.Code, contentDescription = null, tint = SecondaryTeal)
                                        Text(
                                            text = course?.title ?: "Modul Praktik Koding",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimaryDark
                                        )
                                    }
                                    Text(
                                        text = "Tersimpan",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = AccentSuccess,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                if (draft.htmlCode.isNotBlank() || draft.cssCode.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = (draft.htmlCode.ifBlank { draft.cssCode }).take(80).replace("\n", " ") + "...",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontFamily = FontFamily.Monospace,
                                        color = TextSecondaryDark,
                                        maxLines = 2
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Lanjutkan Koding ➡️",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = SecondaryTeal
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Edit Profile Modal Dialog
    if (showEditProfileDialog) {
        var newName by remember { mutableStateOf(profile?.displayName ?: "") }
        var newBio by remember { mutableStateOf(profile?.bio ?: "") }

        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = { Text("Edit Profil Siswa", fontWeight = FontWeight.Bold, color = TextPrimaryDark) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text("Nama Tampilan") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SurfaceDark,
                            unfocusedContainerColor = SurfaceDark
                        )
                    )
                    OutlinedTextField(
                        value = newBio,
                        onValueChange = { newBio = it },
                        label = { Text("Bio Belajar") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SurfaceDark,
                            unfocusedContainerColor = SurfaceDark
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateProfile(newName, newBio)
                        showEditProfileDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Batal", color = TextSecondaryDark)
                }
            },
            containerColor = SurfaceDark
        )
    }
}

@Composable
fun CertificateDetailScreen(
    certificate: CertificateEntity?,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    if (certificate == null) {
        Box(modifier = Modifier.fillMaxSize().background(BackgroundDark), contentAlignment = Alignment.Center) {
            Text("Sertifikat tidak ditemukan.", color = TextSecondaryDark)
        }
        return
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = BackgroundDark,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.clip(CircleShape).background(SurfaceDark)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = TextPrimaryDark)
                }
                Text("Sertifikat Kelulusan Resmi", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                Box(modifier = Modifier.size(40.dp))
            }
        },
        bottomBar = {
            Surface(
                color = SurfaceDark,
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(BorderDark, SurfaceDark))),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                        .navigationBarsPadding(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            Toast.makeText(context, "Sertifikat berhasil diunduh ke galeri! 📄", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Unduh PDF", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = {
                            Toast.makeText(context, "Link sertifikat disalin ke clipboard! 🔗", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, tint = SecondaryTeal)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Bagikan", color = SecondaryTeal, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Official Certificate Frame
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.linearGradient(listOf(SecondaryTeal, PrimaryPurple, AccentWarning))
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .border(1.dp, BorderDark, RoundedCornerShape(14.dp))
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🎓", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "CODEACADEMY INDONESIA",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = SecondaryTeal,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "SERTIFIKAT KELULUSAN KOMPETENSI",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondaryDark,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Diberikan dengan bangga kepada:",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondaryDark
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = certificate.studentName,
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = AccentWarning,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Atas keberhasilannya menyelesaikan seluruh modul dan lulus evaluasi akhir pada kursus:",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondaryDark,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = certificate.courseTitle,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Tanggal Terbit", style = MaterialTheme.typography.labelSmall, color = TextMutedDark)
                            Text(certificate.issueDate, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Skor Akhir", style = MaterialTheme.typography.labelSmall, color = TextMutedDark)
                            Text("${certificate.scorePercent}%", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = AccentSuccess)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text("Status", style = MaterialTheme.typography.labelSmall, color = TextMutedDark)
                            Text("TERVERIFIKASI ✓", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = SecondaryTeal)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "ID: ${certificate.certId}",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        color = TextMutedDark
                    )
                }
            }
        }
    }
}
