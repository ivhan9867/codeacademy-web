package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.CertificateEntity
import com.example.data.local.ModuleProgressEntity
import com.example.data.models.BadgeItem
import com.example.ui.MainViewModel
import com.example.util.localization.AppLanguage
import androidx.compose.ui.res.stringResource
import com.example.R

@Composable
fun ProfileScreen(
    viewModel: MainViewModel,
    onNavigateToBookmarks: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToCertificates: () -> Unit = {},
    onResetOnboarding: () -> Unit = {},
    onLogout: () -> Unit = {},
    onDeleteAccount: () -> Unit = {},
    onMenuClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val isDark = userProfile?.isDarkMode ?: true
    val certificates by viewModel.certificates.collectAsStateWithLifecycle()
    val progressList by viewModel.moduleProgressList.collectAsStateWithLifecycle()
    val badges by viewModel.badges.collectAsStateWithLifecycle()
    val currentLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()

    ProfileScreen(
        name = userProfile?.name ?: "Siswa CodeAcademy",
        email = userProfile?.email ?: "siswa@codeacademy.id",
        bio = userProfile?.bio ?: "Belajar coding setiap hari untuk masa depan cerah!",
        totalXp = userProfile?.totalXp ?: 0,
        streak = userProfile?.currentStreak ?: 1,
        isDark = isDark,
        currentLanguage = currentLanguage,
        certificates = certificates,
        allBadges = badges,
        progressList = progressList,
        onToggleDarkMode = { viewModel.toggleDarkMode(it) },
        onSelectLanguage = { viewModel.setAppLanguage(it) },
        onUpdateProfile = { name, bio -> viewModel.updateProfile(name, bio) },
        onResetOnboarding = {
            viewModel.startReplayingOnboarding()
            onResetOnboarding()
        },
        onLogout = {
            onLogout()
        },
        onDeleteAccount = {
            onDeleteAccount()
        },
        onNavigateToBookmarks = onNavigateToBookmarks,
        onNavigateToNotifications = onNavigateToNotifications,
        onNavigateToCertificates = onNavigateToCertificates,
        onMenuClick = onMenuClick,
        modifier = modifier
    )
}

@Composable
fun ProfileScreen(
    name: String,
    email: String,
    bio: String,
    totalXp: Int,
    streak: Int,
    isDark: Boolean,
    currentLanguage: AppLanguage = AppLanguage.INDONESIAN,
    certificates: List<CertificateEntity>,
    allBadges: List<BadgeItem>,
    progressList: List<ModuleProgressEntity> = emptyList(),
    onToggleDarkMode: (Boolean) -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit = {},
    onUpdateProfile: (String, String) -> Unit,
    onResetOnboarding: () -> Unit,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit = {},
    onNavigateToBookmarks: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToCertificates: () -> Unit,
    onMenuClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showAllBadgesSheet by remember { mutableStateOf(false) }
    var selectedBadgeForDetail by remember { mutableStateOf<BadgeItem?>(null) }
    var selectedHeatmapDay by remember { mutableStateOf<HeatmapDayInfo?>(null) }

    // Language Selector Dialog
    if (showLanguageDialog) {
        ProfileLanguageDialog(
            currentLanguage = currentLanguage,
            onDismiss = { showLanguageDialog = false },
            onSelectLanguage = onSelectLanguage
        )
    }

    // Edit Profile Dialog
    if (showEditDialog) {
        ProfileEditDialog(
            initialName = name,
            initialBio = bio,
            onDismiss = { showEditDialog = false },
            onSave = onUpdateProfile
        )
    }

    // Badge Detail Dialog
    selectedBadgeForDetail?.let { badge ->
        ProfileBadgeDetailDialog(
            badge = badge,
            onDismiss = { selectedBadgeForDetail = null }
        )
    }

    // Modal Bottom Sheet: All Badges
    if (showAllBadgesSheet) {
        ProfileAllBadgesSheet(
            allBadges = allBadges,
            onDismiss = { showAllBadgesSheet = false }
        )
    }

    // Heatmap Day Detail Dialog
    selectedHeatmapDay?.let { dayInfo ->
        HeatmapDayDetailDialog(
            dayInfo = dayInfo,
            onDismiss = { selectedHeatmapDay = null }
        )
    }

    // Logout Confirmation Dialog
    var showDeleteAccountDialog by remember { mutableStateOf(false) }

    if (showDeleteAccountDialog) {
        ProfileDeleteAccountDialog(
            onDismiss = { showDeleteAccountDialog = false },
            onConfirm = {
                showDeleteAccountDialog = false
                onDeleteAccount()
            }
        )
    }

    if (showLogoutDialog) {
        ProfileLogoutDialog(
            onDismiss = { showLogoutDialog = false },
            onConfirm = {
                showLogoutDialog = false
                onLogout()
            }
        )
    }

    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title
        item(key = "title") {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (onMenuClick != null) {
                    IconButton(
                        onClick = onMenuClick,
                        modifier = Modifier
                            .testTag("btn_menu_profile")
                            .size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu Navigasi",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Text(
                    text = stringResource(R.string.profile_title_text),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        // 1. Header Identitas & Stat Ringkas
        item(key = "header_identity") {
            ProfileIdentityCard(
                name = name,
                email = email,
                bio = bio,
                totalXp = totalXp,
                streak = streak,
                certificateCount = certificates.size,
                onEditClick = { showEditDialog = true }
            )
        }

        // 2. Peta Semangat Belajar (Flame Heatmap)
        item(key = "activity_heatmap") {
            ActivityHeatmapCard(
                progressList = progressList,
                streakDays = streak,
                onDayClick = { dayInfo -> selectedHeatmapDay = dayInfo }
            )
        }

        // 3. Koleksi Badge
        item(key = "badge_collection") {
            ProfileBadgesCard(
                allBadges = allBadges,
                onBadgeClick = { badge -> selectedBadgeForDetail = badge },
                onSeeAllClick = { showAllBadgesSheet = true }
            )
        }

        // 4. Section: Pencapaian Belajar
        item(key = "section_achievements") {
            ProfileAchievementsSection(
                certificateCount = certificates.size,
                onNavigateToCertificates = onNavigateToCertificates,
                onNavigateToBookmarks = onNavigateToBookmarks
            )
        }

        // 5. Section: Pengaturan & Preferensi
        item(key = "section_settings") {
            ProfilePreferencesSection(
                isDark = isDark,
                currentLanguage = currentLanguage,
                onToggleDarkMode = onToggleDarkMode,
                onSelectLanguageClick = { showLanguageDialog = true },
                onNavigateToNotifications = onNavigateToNotifications,
                onResetOnboarding = onResetOnboarding,
                onLogoutClick = { showLogoutDialog = true },
                onDeleteAccountClick = { showDeleteAccountDialog = true }
            )
        }
    }
}
