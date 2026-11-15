package com.example.ui.screens

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.LearningTrack

data class TrackVisualTheme(
    val gradientColors: List<Color>,
    val accentColor: Color,
    val pillText: String,
    val badgeBgColor: Color
)

fun getTrackVisualTheme(trackId: String): TrackVisualTheme {
    return when (trackId) {
        "front-end" -> TrackVisualTheme(
            gradientColors = listOf(Color(0xFF0F3A5D), Color(0xFF0D5E56), Color(0xFF082F49)),
            accentColor = Color(0xFF2DD4BF),
            pillText = "HTML • CSS • JS",
            badgeBgColor = Color(0xFF14B8A6).copy(alpha = 0.22f)
        )
        "back-end" -> TrackVisualTheme(
            gradientColors = listOf(Color(0xFF3B1D64), Color(0xFF221643), Color(0xFF1E293B)),
            accentColor = Color(0xFFA78BFA),
            pillText = "Node • SQL • API",
            badgeBgColor = Color(0xFF8B5CF6).copy(alpha = 0.22f)
        )
        "python" -> TrackVisualTheme(
            gradientColors = listOf(Color(0xFF064E3B), Color(0xFF063B34), Color(0xFF0C243B)),
            accentColor = Color(0xFF34D399),
            pillText = "Automation • AI",
            badgeBgColor = Color(0xFF10B981).copy(alpha = 0.22f)
        )
        "c-lang" -> TrackVisualTheme(
            gradientColors = listOf(Color(0xFF78350F), Color(0xFF451A03), Color(0xFF1C1917)),
            accentColor = Color(0xFFFBBF24),
            pillText = "Memory • System",
            badgeBgColor = Color(0xFFF59E0B).copy(alpha = 0.22f)
        )
        "cpp-lang" -> TrackVisualTheme(
            gradientColors = listOf(Color(0xFF831843), Color(0xFF4C0519), Color(0xFF1E1B4B)),
            accentColor = Color(0xFFFB7185),
            pillText = "OOP • High-Perf",
            badgeBgColor = Color(0xFFF43F5E).copy(alpha = 0.22f)
        )
        else -> TrackVisualTheme(
            gradientColors = listOf(Color(0xFF1E293B), Color(0xFF0F172A)),
            accentColor = Color(0xFF38BDF8),
            pillText = "Spesialisasi",
            badgeBgColor = Color(0xFF0284C7).copy(alpha = 0.22f)
        )
    }
}

@Composable
fun LearningTrackCard(
    track: LearningTrack,
    progressFraction: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.width(210.dp)
) {
    val theme = remember(track.id) { getTrackVisualTheme(track.id) }
    val percent = remember(progressFraction) { (progressFraction * 100).toInt() }

    // Interactive scale-down tap feedback
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val animatedScale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "track_card_scale_${track.id}"
    )

    Card(
        modifier = modifier
            .heightIn(min = 205.dp)
            .then(
                if (animatedScale < 0.999f) {
                    Modifier.graphicsLayer {
                        scaleX = animatedScale
                        scaleY = animatedScale
                    }
                } else {
                    Modifier
                }
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Row: Emblem & Course Count Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(theme.badgeBgColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = track.iconEmoji,
                        fontSize = 24.sp
                    )
                }

                Surface(
                    color = theme.accentColor.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = androidx.compose.ui.res.stringResource(
                            com.example.R.string.home_track_courses_count,
                            track.totalCourses
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = theme.accentColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Middle: Track title and topics pill
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = com.example.util.localization.DataLabelMappings.trackName(track),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = theme.pillText,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Bottom Section: Progress bar or Start CTA
            Column(modifier = Modifier.fillMaxWidth()) {
                if (progressFraction > 0f) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.home_track_progress_label),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = androidx.compose.ui.res.stringResource(
                                com.example.R.string.home_track_progress_percent,
                                percent
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = theme.accentColor
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    LinearProgressIndicator(
                        progress = { progressFraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = theme.accentColor,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.home_start_learning_button),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = theme.accentColor
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = theme.accentColor,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreenTrackSection(
    tracks: List<LearningTrack>,
    trackProgressMap: Map<String, Float>,
    onTrackClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val visibleTracks = remember(tracks) { tracks.filter { it.totalCourses > 0 } }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = androidx.compose.ui.res.stringResource(com.example.R.string.home_explore_tracks_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = androidx.compose.ui.res.stringResource(
                        com.example.R.string.home_track_interactive_specializations,
                        visibleTracks.size
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = androidx.compose.ui.res.stringResource(com.example.R.string.home_see_all),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { onTrackClick("front-end") }
            )
        }

        if (visibleTracks.size == 1) {
            val singleTrack = visibleTracks.first()
            LearningTrackCard(
                track = singleTrack,
                progressFraction = trackProgressMap[singleTrack.id] ?: 0f,
                onClick = { onTrackClick(singleTrack.id) },
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(horizontal = 2.dp)
            ) {
                items(visibleTracks, key = { it.id }) { track ->
                    val fraction = trackProgressMap[track.id] ?: 0f
                    LearningTrackCard(
                        track = track,
                        progressFraction = fraction,
                        onClick = { onTrackClick(track.id) }
                    )
                }
            }
        }
    }
}
