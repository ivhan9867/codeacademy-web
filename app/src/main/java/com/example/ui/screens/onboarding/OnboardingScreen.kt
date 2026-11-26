package com.example.ui.screens.onboarding

import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import com.example.R
import com.example.data.local.sessionDataStore
import kotlinx.coroutines.launch

/**
 * Calm, bright, purposeful 4-step onboarding tailored for Front-End Web Development.
 */
@Composable
fun OnboardingScreen(
    userName: String = "",
    initialTrack: String = "front-end",
    onFinishOnboarding: (selectedTrackId: String) -> Unit,
    onSkipOnboarding: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val isInspectionMode = LocalInspectionMode.current

    val areAnimationsEnabled = remember(context, isInspectionMode) {
        if (isInspectionMode) false
        else {
            val scale = runCatching {
                Settings.Global.getFloat(context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f)
            }.getOrDefault(1f)
            scale > 0f
        }
    }

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 4 })
    val selectedTrack = if (initialTrack.isNotBlank()) initialTrack else "front-end"
    var selectedGoal by remember { mutableStateOf("Karier Web Dev") }
    var selectedDailyMinutes by remember { mutableIntStateOf(20) }

    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val calmBgBrush = if (isDark) {
        Brush.verticalGradient(
            listOf(MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.surface)
        )
    } else {
        Brush.verticalGradient(
            listOf(Color(0xFFEEF6F5), Color(0xFFF7FAFA))
        )
    }

    BackHandler(enabled = pagerState.currentPage > 0) {
        coroutineScope.launch {
            pagerState.animateScrollToPage(pagerState.currentPage - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(calmBgBrush)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        OnboardingTopBar(
            canGoBack = pagerState.currentPage > 0,
            canSkip = pagerState.currentPage in 1..2,
            onBackClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                }
            },
            onSkipClick = {
                if (onSkipOnboarding != null) {
                    onSkipOnboarding()
                } else {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(3)
                    }
                }
            }
        )

        OnboardingProgressBar(
            currentPage = pagerState.currentPage,
            totalPages = 4,
            areAnimationsEnabled = areAnimationsEnabled
        )

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { page ->
            when (page) {
                0 -> WelcomeStep(
                    userName = userName,
                    areAnimationsEnabled = areAnimationsEnabled
                )
                1 -> CodePreviewStep(
                    areAnimationsEnabled = areAnimationsEnabled
                )
                2 -> RoadmapStep()
                3 -> PlanSummaryStep(
                    selectedTrack = selectedTrack,
                    selectedGoal = selectedGoal,
                    selectedDailyMinutes = selectedDailyMinutes,
                    onSelectGoal = { selectedGoal = it },
                    onSelectMinutes = { selectedDailyMinutes = it }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        val buttonText = when (pagerState.currentPage) {
            0 -> stringResource(R.string.onboarding_btn_start)
            in 1..2 -> stringResource(R.string.onboarding_btn_next)
            else -> stringResource(R.string.onboarding_btn_start_learning)
        }

        OnboardingActionButton(
            text = buttonText,
            onClick = {
                if (pagerState.currentPage < 3) {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                } else {
                    coroutineScope.launch {
                        try {
                            context.sessionDataStore.edit { prefs ->
                                prefs[KEY_USER_GOAL] = selectedGoal
                                prefs[KEY_DAILY_STUDY_MINUTES] = selectedDailyMinutes
                            }
                        } catch (_: Exception) {
                        }
                        onFinishOnboarding("front-end")
                    }
                }
            }
        )
    }
}
