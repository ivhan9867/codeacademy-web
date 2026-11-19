package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CodeDarkBg
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TealPrimary
import com.example.util.localization.AppLanguage
import com.example.util.localization.LocalAppLanguage
import java.util.Calendar

private data class DailyTipItem(
    val titleId: String,
    val titleEn: String,
    val bodyId: String,
    val bodyEn: String,
    val starterCode: String,
    val language: String
)

private data class MicroChallengeItem(
    val tag: String,
    val questionId: String,
    val questionEn: String,
    val codeSnippet: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanationId: String,
    val explanationEn: String
)

private val DAILY_TIPS = listOf(
    DailyTipItem(
        titleId = "Gunakan Flexbox `gap` daripada margin berulang",
        titleEn = "Use Flexbox `gap` instead of repetitive margins",
        bodyId = "Properti `gap: 12px;` otomatis memberi jarak rapi antar elemen anak tanpa margin ekstra di ujung.",
        bodyEn = "`gap: 12px;` automatically spaces flex children evenly without extra edge margins.",
        starterCode = "<div style=\"display:flex; gap:12px;\">\n  <button>HTML</button>\n  <button>CSS</button>\n  <button>JS</button>\n</div>",
        language = "html"
    ),
    DailyTipItem(
        titleId = "Biasakan `const` dulu sebelum `let` di JavaScript",
        titleEn = "Prefer `const` over `let` in modern JavaScript",
        bodyId = "Memakai `const` mencegah perubahan nilai tidak sengaja sehingga bug logika lebih mudah dihindari.",
        bodyEn = "Using `const` prevents accidental reassignments and makes your JS logic easier to reason about.",
        starterCode = "const nama = 'Codi';\nconsole.log(`Halo, ${'$'}{nama}!`);",
        language = "javascript"
    ),
    DailyTipItem(
        titleId = "Tag HTML Semantik `<main>` & `<section>`",
        titleEn = "Semantic HTML `<main>` & `<section>` tags",
        bodyId = "Struktur semantik membantu screen reader, SEO, dan membuat kode HTML jauh lebih mudah dibaca.",
        bodyEn = "Semantic tags improve accessibility, SEO, and keep your HTML structure clean.",
        starterCode = "<main>\n  <section>\n    <h2>Belajar Web</h2>\n    <p>Mulai dari struktur semantik!</p>\n  </section>\n</main>",
        language = "html"
    ),
    DailyTipItem(
        titleId = "Responsif Instan dengan CSS Grid `auto-fit`",
        titleEn = "Instant responsiveness with CSS Grid `auto-fit`",
        bodyId = "Kombinasi `repeat(auto-fit, minmax(140px, 1fr))` membuat grid otomatis menyesuaikan lebar layar.",
        bodyEn = "`repeat(auto-fit, minmax(140px, 1fr))` adapts grid columns automatically to any screen width.",
        starterCode = "<div style=\"display:grid; grid-template-columns:repeat(auto-fit, minmax(120px, 1fr)); gap:8px;\">\n  <div style=\"background:#1A6368;color:#fff;padding:12px;\">Box 1</div>\n  <div style=\"background:#DF7B56;color:#fff;padding:12px;\">Box 2</div>\n</div>",
        language = "html"
    )
)

private val MICRO_CHALLENGES = listOf(
    MicroChallengeItem(
        tag = "JavaScript",
        questionId = "Apa output dari kode JavaScript berikut?",
        questionEn = "What is the output of this JavaScript snippet?",
        codeSnippet = "const arr = [10, 20, 30];\nconsole.log(arr.length + arr[0]);",
        options = listOf("13", "30", "33"),
        correctIndex = 0,
        explanationId = "`arr.length` bernilai 3 dan `arr[0]` bernilai 10, jadi 3 + 10 = 13.",
        explanationEn = "`arr.length` is 3 and `arr[0]` is 10, so 3 + 10 = 13."
    ),
    MicroChallengeItem(
        tag = "CSS",
        questionId = "Properti Flexbox mana yang meratakan item secara horizontal ke tengah?",
        questionEn = "Which Flexbox property centers items horizontally along the main axis?",
        codeSnippet = ".container {\n  display: flex;\n  /* ??? */\n}",
        options = listOf("justify-content: center;", "align-content: top;", "flex-wrap: center;"),
        correctIndex = 0,
        explanationId = "`justify-content: center;` mengatur perataan di sumbu utama (horizontal pada flex row).",
        explanationEn = "`justify-content: center;` aligns items along the main axis (horizontal in flex row)."
    ),
    MicroChallengeItem(
        tag = "HTML",
        questionId = "Tag HTML mana yang paling tepat untuk navigasi utama halaman?",
        questionEn = "Which semantic HTML tag is best for primary page navigation?",
        codeSnippet = "<header>\n  <___>\n    <a href=\"#home\">Home</a>\n  </___>\n</header>",
        options = listOf("<div-nav>", "<nav>", "<menu-bar>"),
        correctIndex = 1,
        explanationId = "Tag semantik standar HTML5 untuk tautan navigasi adalah `<nav>`.",
        explanationEn = "The standard semantic HTML5 tag for navigation links is `<nav>`."
    ),
    MicroChallengeItem(
        tag = "JavaScript",
        questionId = "Apa hasil dari ekspresi perbandingan berikut?",
        questionEn = "What does this strict equality expression evaluate to?",
        codeSnippet = "console.log(5 === \"5\");",
        options = listOf("true", "false", "undefined"),
        correctIndex = 1,
        explanationId = "Operator `===` memeriksa nilai sekaligus tipe data (Number vs String), sehingga hasilnya `false`.",
        explanationEn = "Strict equality `===` checks both value and type (Number vs String), returning `false`."
    )
)

private val INLINE_CODE_REGEX = Regex("`([^`]+)`")

private fun formatInlineCode(
    text: String,
    codeBg: Color,
    codeColor: Color
): AnnotatedString {
    if (!text.contains('`')) return AnnotatedString(text)
    return buildAnnotatedString {
        var lastIndex = 0
        INLINE_CODE_REGEX.findAll(text).forEach { match ->
            if (match.range.first > lastIndex) {
                append(text.substring(lastIndex, match.range.first))
            }
            pushStyle(
                SpanStyle(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                    color = codeColor,
                    background = codeBg.copy(alpha = 0.7f)
                )
            )
            append(" ${match.groupValues[1]} ")
            pop()
            lastIndex = match.range.last + 1
        }
        if (lastIndex < text.length) {
            append(text.substring(lastIndex))
        }
    }
}

@Composable
fun CodiDailyTipCard(
    onAskAi: (String) -> Unit,
    onTryInPlayground: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val lang = LocalAppLanguage.current
    val isEnLike = lang != AppLanguage.INDONESIAN
    val dayIndex = remember { Calendar.getInstance().get(Calendar.DAY_OF_YEAR) }
    val tip = DAILY_TIPS[dayIndex % DAILY_TIPS.size]

    val title = if (isEnLike) tip.titleEn else tip.titleId
    val body = if (isEnLike) tip.bodyEn else tip.bodyId

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, CyanAccent.copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            TealPrimary.copy(alpha = 0.08f),
                            CyanAccent.copy(alpha = 0.05f),
                            Color.Transparent
                        )
                    )
                )
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CodeAcademyMascot(
                size = 62.dp,
                mood = MascotMood.WAVING,
                showWebBadges = false
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = TealPrimary.copy(alpha = 0.14f)
                ) {
                    Text(
                        text = stringResource(R.string.codi_tip_badge),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                val inlineCodeBg = MaterialTheme.colorScheme.surfaceVariant
                val inlineCodeColor = MaterialTheme.colorScheme.primary

                Text(
                    text = formatInlineCode(title, inlineCodeBg, inlineCodeColor),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = formatInlineCode(body, inlineCodeBg, inlineCodeColor),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = TealPrimary,
                        modifier = Modifier.clickable { onAskAi(title) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = stringResource(R.string.codi_tip_ask_ai),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.clickable { onTryInPlayground(tip.starterCode, tip.language) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Code,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = stringResource(R.string.codi_tip_try_code),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DailyCodeChallengeCard(
    isCompletedToday: Boolean,
    onClaimReward: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lang = LocalAppLanguage.current
    val isEnLike = lang != AppLanguage.INDONESIAN
    val dayIndex = remember { Calendar.getInstance().get(Calendar.DAY_OF_YEAR) }
    val item = MICRO_CHALLENGES[dayIndex % MICRO_CHALLENGES.size]

    var selectedOption by remember { mutableIntStateOf(-1) }
    val isCorrectSelected = selectedOption == item.correctIndex
    var isCollapsedToCelebration by remember(isCompletedToday) {
        mutableStateOf(isCompletedToday && selectedOption == -1)
    }
    var showReviewExplanation by remember { mutableStateOf(false) }

    // Auto-collapse smoothly into Celebration Card ~2.2s after answering correctly
    LaunchedEffect(isCorrectSelected) {
        if (isCorrectSelected) {
            delay(2200L)
            isCollapsedToCelebration = true
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(
            1.dp,
            if (isCompletedToday || isCorrectSelected) SuccessGreen.copy(alpha = 0.45f)
            else AmberGold.copy(alpha = 0.45f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        AnimatedContent(
            targetState = isCollapsedToCelebration,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "dailyChallengeState"
        ) { collapsed ->
            if (collapsed) {
                val explanationText = if (isEnLike) item.explanationEn else item.explanationId
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    SuccessGreen.copy(alpha = 0.13f),
                                    TealPrimary.copy(alpha = 0.08f),
                                    AmberGold.copy(alpha = 0.08f)
                                )
                            )
                        )
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CodeAcademyMascot(
                            size = 54.dp,
                            mood = MascotMood.HAPPY,
                            showWebBadges = false
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(999.dp),
                                    color = SuccessGreen.copy(alpha = 0.18f)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.EmojiEvents,
                                            contentDescription = null,
                                            tint = SuccessGreen,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = stringResource(R.string.daily_challenge_celebrate_xp_badge),
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = SuccessGreen
                                        )
                                    }
                                }
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
                                ) {
                                    Text(
                                        text = item.tag,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = stringResource(R.string.daily_challenge_celebrate_title),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = stringResource(R.string.daily_challenge_done_msg),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.78f),
                        border = BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.28f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showReviewExplanation = !showReviewExplanation }
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (showReviewExplanation) {
                                        stringResource(R.string.daily_challenge_hide_review)
                                    } else {
                                        stringResource(R.string.daily_challenge_show_review)
                                    },
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TealPrimary
                                )
                                Icon(
                                    imageVector = if (showReviewExplanation) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = null,
                                    tint = TealPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            AnimatedVisibility(visible = showReviewExplanation) {
                                Text(
                                    text = formatInlineCode(
                                        text = explanationText,
                                        codeBg = MaterialTheme.colorScheme.surfaceVariant,
                                        codeColor = MaterialTheme.colorScheme.primary
                                    ),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(top = 6.dp)
                                )
                            }
                        }
                    }
                }
            } else {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(999.dp),
                            color = if (isCompletedToday || isCorrectSelected) SuccessGreen.copy(alpha = 0.14f) else FlameOrange.copy(alpha = 0.14f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isCompletedToday || isCorrectSelected) Icons.Default.CheckCircle else Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = if (isCompletedToday || isCorrectSelected) SuccessGreen else FlameOrange,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isCompletedToday || isCorrectSelected) {
                                        stringResource(R.string.daily_challenge_completed_badge)
                                    } else {
                                        stringResource(R.string.daily_challenge_badge)
                                    },
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCompletedToday || isCorrectSelected) SuccessGreen else FlameOrange
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = item.tag,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (isEnLike) item.questionEn else item.questionId,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(CodeDarkBg)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = item.codeSnippet,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = Color(0xFFE2E8F0)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        item.options.forEachIndexed { idx, opt ->
                            val isPicked = selectedOption == idx
                            val isRight = idx == item.correctIndex
                            val borderColor = when {
                                isPicked && isRight -> SuccessGreen
                                isPicked && !isRight -> ErrorRed
                                else -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
                            }
                            val bgColor = when {
                                isPicked && isRight -> SuccessGreen.copy(alpha = 0.12f)
                                isPicked && !isRight -> ErrorRed.copy(alpha = 0.10f)
                                else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                            }

                            val optionLabel = ('A' + idx).toString()
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = bgColor,
                                border = BorderStroke(1.dp, borderColor),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 48.dp)
                                    .clickable(enabled = !isCompletedToday && !isCorrectSelected) {
                                        selectedOption = idx
                                        if (idx == item.correctIndex && !isCompletedToday) {
                                            onClaimReward()
                                        }
                                    }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = when {
                                            isPicked && isRight -> SuccessGreen
                                            isPicked && !isRight -> ErrorRed
                                            else -> MaterialTheme.colorScheme.surfaceVariant
                                        }
                                    ) {
                                        Text(
                                            text = optionLabel,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isPicked) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = opt,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = if (isPicked) FontWeight.Bold else FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.weight(1f)
                                    )
                                    if (isPicked) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Icon(
                                            imageVector = if (isRight) Icons.Default.CheckCircle else Icons.Default.Cancel,
                                            contentDescription = null,
                                            tint = if (isRight) SuccessGreen else ErrorRed,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    AnimatedVisibility(visible = selectedOption != -1) {
                        val explanationText = if (isEnLike) item.explanationEn else item.explanationId
                        Column(modifier = Modifier.padding(top = 10.dp)) {
                            Text(
                                text = if (isCorrectSelected) {
                                    stringResource(R.string.daily_challenge_correct_msg)
                                } else {
                                    stringResource(R.string.daily_challenge_wrong_msg)
                                },
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isCorrectSelected) SuccessGreen else FlameOrange
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = formatInlineCode(
                                    text = explanationText,
                                    codeBg = MaterialTheme.colorScheme.surfaceVariant,
                                    codeColor = MaterialTheme.colorScheme.primary
                                ),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
