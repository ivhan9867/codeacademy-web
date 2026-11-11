package com.example.ui.screens.onboarding

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CourseCatalog
import com.example.data.models.LearningTrack
import com.example.ui.theme.Dimens
import com.example.ui.theme.TealLight
import com.example.ui.theme.TealPrimary

/**
 * Step 4: Pilih Track Minat.
 * Clean 2-column grid of available tracks from CourseCatalog.tracks with high accessibility.
 */
@Composable
fun TrackSelectionStep(
    selectedTrackId: String,
    onSelectTrack: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val tracks = remember { CourseCatalog.tracks.filter { it.totalCourses > 0 } }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Track Selection Grid (fills dominant central area)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 12.dp)
        ) {
            items(tracks, key = { it.id }) { track ->
                TrackCard(
                    track = track,
                    isSelected = track.id == selectedTrackId,
                    onSelect = { onSelectTrack(track.id) }
                )
            }
        }

        // Headline & Subtext (max 2 lines each)
        OnboardingStepHeader(
            title = "Pilih Jalur Minatmu",
            subtitle = "Mulai dari topik yang paling membuatmu penasaran. Kamu bebas berganti track kapan saja.",
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Composable
private fun TrackCard(
    track: LearningTrack,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) TealPrimary else MaterialTheme.colorScheme.outlineVariant,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "track_border_${track.id}"
    )

    val containerColor by animateColorAsState(
        targetValue = if (isSelected) TealPrimary.copy(alpha = 0.10f) else MaterialTheme.colorScheme.surface,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "track_container_${track.id}"
    )

    Card(
        shape = Dimens.CardDefaultShape,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(if (isSelected) 2.dp else 1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 0.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(108.dp)
            .clip(Dimens.CardDefaultShape)
            .clickable(
                role = Role.RadioButton,
                onClick = onSelect
            )
            .testTag("track_card_${track.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Emoji container badge
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) TealPrimary.copy(alpha = 0.18f)
                            else MaterialTheme.colorScheme.surfaceVariant
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = track.iconEmoji,
                        fontSize = 18.sp
                    )
                }

                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Terpilih",
                        tint = TealPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Column {
                Text(
                    text = track.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) TealPrimary else MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = getTrackTagline(track.id),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

private fun getTrackTagline(trackId: String): String = when (trackId) {
    "front-end" -> "HTML, CSS, JavaScript"
    "back-end" -> "Node.js, REST API, SQL"
    "python" -> "Dasar, Otomasi & Logika"
    "c-lang" -> "Fondasi Sistem & Memori"
    "cpp-lang" -> "OOP & Performa Cepat"
    else -> "Materi Lengkap dari Nol"
}
