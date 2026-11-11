package com.example.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.ui.screens.onboarding.KEY_DAILY_STUDY_MINUTES as OnboardingDailyStudyMinutesKey
import com.example.ui.screens.onboarding.KEY_USER_GOAL as OnboardingUserGoalKey
import com.example.ui.screens.onboarding.OnboardingScreen

val KEY_USER_GOAL = OnboardingUserGoalKey
val KEY_DAILY_STUDY_MINUTES = OnboardingDailyStudyMinutesKey

/**
 * Public facade for the CodeAcademy Onboarding Flow.
 * Retains backward compatibility with MainActivity navigation parameters.
 */
@Composable
fun AuthOnboardingScreen(
    userName: String = "",
    initialTrack: String = "front-end",
    onFinishOnboarding: (selectedTrackId: String) -> Unit,
    onSkipOnboarding: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    OnboardingScreen(
        userName = userName,
        initialTrack = initialTrack,
        onFinishOnboarding = onFinishOnboarding,
        onSkipOnboarding = onSkipOnboarding,
        modifier = modifier
    )
}
