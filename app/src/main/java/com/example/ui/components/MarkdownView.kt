package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.draw.clip
import com.example.R
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.data.GlossaryItem
import com.example.data.GlossaryRegistry
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CodeDarkBg
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.CyanAccentText
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TealDark
import com.example.ui.theme.TealLight
import com.example.ui.theme.TealPrimary

/**
 * Parses inline Markdown syntax (**bold**, *italic*, `inline code`, and nested combos like **`code`**)
 * and appends formatted Spans and optional Glossary annotations to an AnnotatedString.Builder.
 */
fun AnnotatedString.Builder.appendMarkdownInline(
    text: String,
    codeBackgroundColor: Color,
    codeTextColor: Color,
    glossaryRegex: Regex? = null,
    glossaryMap: Map<String, GlossaryItem> = emptyMap(),
    accentColor: Color = Color.Unspecified
) {
    var i = 0
    val len = text.length

    fun appendTextWithGlossary(plainText: String) {
        if (glossaryRegex == null || glossaryMap.isEmpty()) {
            append(plainText)
            return
        }

        var lastIdx = 0
        val matches = glossaryRegex.findAll(plainText)
        for (match in matches) {
            val start = match.range.first
            val end = match.range.last + 1
            if (start > lastIdx) {
                append(plainText.substring(lastIdx, start))
            }
            val matchedTerm = match.value
            val itemKey = matchedTerm.lowercase()
            val glossaryItem = glossaryMap[itemKey]

            if (glossaryItem != null) {
                pushStringAnnotation(tag = "GLOSSARY", annotation = itemKey)
                pushStyle(
                    SpanStyle(
                        color = accentColor,
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                append(matchedTerm)
                pop()
                pop()
            } else {
                append(matchedTerm)
            }
            lastIdx = end
        }
        if (lastIdx < plainText.length) {
            append(plainText.substring(lastIdx))
        }
    }

    while (i < len) {
        // 1. Check for ** (Bold) or __ (Bold)
        if (i + 1 < len && ((text[i] == '*' && text[i + 1] == '*') || (text[i] == '_' && text[i + 1] == '_'))) {
            val delimiter = text.substring(i, i + 2)
            val closeIdx = text.indexOf(delimiter, i + 2)
            if (closeIdx != -1) {
                val inner = text.substring(i + 2, closeIdx)
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                appendMarkdownInline(inner, codeBackgroundColor, codeTextColor, glossaryRegex, glossaryMap, accentColor)
                pop()
                i = closeIdx + 2
                continue
            }
        }

        // 2. Check for ` (Inline Code)
        if (text[i] == '`') {
            val closeIdx = text.indexOf('`', i + 1)
            if (closeIdx != -1) {
                val codeContent = text.substring(i + 1, closeIdx)
                pushStyle(
                    SpanStyle(
                        fontFamily = FontFamily.Monospace,
                        background = codeBackgroundColor,
                        color = codeTextColor,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                append(codeContent)
                pop()
                i = closeIdx + 1
                continue
            }
        }

        // 3. Check for single * (Italic) or _ (Italic)
        if ((text[i] == '*' || text[i] == '_') && i + 1 < len && text[i + 1] != ' ' && text[i + 1] != text[i]) {
            val delimiter = text[i]
            val closeIdx = text.indexOf(delimiter, i + 1)
            if (closeIdx != -1 && closeIdx > i + 1 && text[closeIdx - 1] != ' ') {
                val inner = text.substring(i + 1, closeIdx)
                pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                appendMarkdownInline(inner, codeBackgroundColor, codeTextColor, glossaryRegex, glossaryMap, accentColor)
                pop()
                i = closeIdx + 1
                continue
            }
        }

        // 4. Regular plain text run until next markdown symbol.
        // Start scanning at i + 1 so an unmatched '*', '_', or '`' at index i is consumed as literal text.
        var nextSpecial = len
        for (j in (i + 1) until len) {
            val c = text[j]
            if (c == '*' || c == '_' || c == '`') {
                nextSpecial = j
                break
            }
        }
        val plainRun = text.substring(i, nextSpecial)
        appendTextWithGlossary(plainRun)
        i = nextSpecial
    }
}

/**
 * Builds an AnnotatedString from a markdown string with customizable inline code styling.
 */
fun parseMarkdownToAnnotatedString(
    text: String,
    codeBackgroundColor: Color,
    codeTextColor: Color,
    glossaryRegex: Regex? = null,
    glossaryMap: Map<String, GlossaryItem> = emptyMap(),
    accentColor: Color = Color.Unspecified
): AnnotatedString {
    return buildAnnotatedString {
        appendMarkdownInline(text, codeBackgroundColor, codeTextColor, glossaryRegex, glossaryMap, accentColor)
    }
}

/**
 * Composable that displays Markdown text with inline formatting and interactive glossary tooltips.
 */
@Composable
fun MarkdownText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified,
    fontStyle: FontStyle? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    codeBackgroundColor: Color = if (isSystemInDarkTheme()) TealLight.copy(alpha = 0.22f) else TealPrimary.copy(alpha = 0.12f),
    codeTextColor: Color = if (isSystemInDarkTheme()) TealLight else TealDark,
    glossaryMap: Map<String, GlossaryItem> = emptyMap(),
    glossaryRegex: Regex? = null,
    onGlossaryClick: ((GlossaryItem) -> Unit)? = null
) {
    val isDark = isSystemInDarkTheme()
    val accentColor = if (isDark) CyanAccent else TealPrimary

    val annotated = remember(text, codeBackgroundColor, codeTextColor, glossaryRegex, glossaryMap, isDark) {
        parseMarkdownToAnnotatedString(
            text = text,
            codeBackgroundColor = codeBackgroundColor,
            codeTextColor = codeTextColor,
            glossaryRegex = glossaryRegex,
            glossaryMap = glossaryMap,
            accentColor = accentColor
        )
    }

    var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    val clickModifier = if (onGlossaryClick != null && glossaryMap.isNotEmpty()) {
        Modifier.pointerInput(annotated) {
            detectTapGestures { pos ->
                layoutResult?.let { layout ->
                    val offset = layout.getOffsetForPosition(pos)
                    val annotations = annotated.getStringAnnotations(tag = "GLOSSARY", start = offset, end = offset)
                    val annotation = annotations.firstOrNull()
                    if (annotation != null) {
                        val item = glossaryMap[annotation.item.lowercase()]
                        if (item != null) {
                            onGlossaryClick(item)
                        }
                    }
                }
            }
        }
    } else {
        Modifier
    }

    Text(
        text = annotated,
        modifier = modifier.then(clickModifier),
        style = style,
        color = color,
        fontStyle = fontStyle,
        textAlign = textAlign,
        lineHeight = lineHeight,
        maxLines = maxLines,
        overflow = overflow,
        onTextLayout = { layoutResult = it }
    )
}

/**
 * Structural representations of Markdown blocks.
 */
enum class TableAlign { START, CENTER, END }

enum class CalloutType {
    TIP,
    WARNING,
    INFO,
    NOTE,
    IMPORTANT,
    CAUTION;

    companion object {
        fun fromString(value: String): CalloutType {
            return when (value.trim().uppercase()) {
                "TIP" -> TIP
                "WARNING" -> WARNING
                "INFO" -> INFO
                "NOTE" -> NOTE
                "IMPORTANT" -> IMPORTANT
                "CAUTION" -> CAUTION
                else -> INFO
            }
        }
    }
}

sealed class MarkdownBlock {
    data class Heading(val level: Int, val text: String) : MarkdownBlock()
    data class Paragraph(val text: String) : MarkdownBlock()
    data class UnorderedList(val items: List<String>) : MarkdownBlock()
    data class OrderedList(val items: List<Pair<Int, String>>) : MarkdownBlock()
    data class Blockquote(val text: String) : MarkdownBlock()
    data class CodeBlock(val code: String, val language: String?) : MarkdownBlock()
    data class Table(
        val headers: List<String>,
        val alignments: List<TableAlign>,
        val rows: List<List<String>>
    ) : MarkdownBlock()
    data class Image(
        val alt: String,
        val url: String,
        val caption: String? = null
    ) : MarkdownBlock()
    data class Video(
        val title: String,
        val url: String
    ) : MarkdownBlock()
    data class Callout(
        val type: CalloutType,
        val title: String?,
        val text: String
    ) : MarkdownBlock()
}

/**
 * Resolves local asset URLs (asset:images/<courseId>/...) or external URLs to Coil-compatible models.
 */
fun resolveImageUri(rawUrl: String, courseId: String? = null): String {
    val trimmed = rawUrl.trim()
    return when {
        trimmed.startsWith("asset:") -> {
            val path = trimmed.removePrefix("asset:").trim().removePrefix("/")
            "file:///android_asset/$path"
        }
        trimmed.startsWith("file:///android_asset/") -> trimmed
        trimmed.startsWith("http://") || trimmed.startsWith("https://") -> trimmed
        trimmed.startsWith("images/") -> "file:///android_asset/$trimmed"
        else -> {
            if (trimmed.startsWith("/")) {
                "file:///android_asset$trimmed"
            } else {
                "file:///android_asset/$trimmed"
            }
        }
    }
}

private val YT_SHORT_REGEX = Regex("""youtu\.be/([a-zA-Z0-9_-]{11})""")
private val YT_LONG_REGEX = Regex("""[?&]v=([a-zA-Z0-9_-]{11})""")
private val YT_EMBED_REGEX = Regex("""youtube\.com/embed/([a-zA-Z0-9_-]{11})""")
private val MD_VIDEO_REGEX = Regex("""^::video\[(.*?)\](?:\((.*?)\))?\s*$""")
private val MD_IMAGE_REGEX = Regex("""^!\[(.*?)\]\((.*?)\)\s*$""")
private val MD_ORDERED_REGEX = Regex("""^(\d+)\.\s+(.*)$""")
private val MD_CALLOUT_REGEX = Regex("""^\[!(TIP|WARNING|INFO|NOTE|IMPORTANT|CAUTION)\](?:\s+(.*))?$""", RegexOption.IGNORE_CASE)
private val MD_TABLE_SEP_REGEX = Regex("^:?-{2,}:?$")

/**
 * Extracts 11-character YouTube video ID from various YouTube URL formats.
 */
fun extractYouTubeVideoId(url: String): String? {
    val trimmed = url.trim()
    val shortMatch = YT_SHORT_REGEX.find(trimmed)
    if (shortMatch != null) return shortMatch.groupValues[1]

    val longMatch = YT_LONG_REGEX.find(trimmed)
    if (longMatch != null) return longMatch.groupValues[1]

    val embedMatch = YT_EMBED_REGEX.find(trimmed)
    if (embedMatch != null) return embedMatch.groupValues[1]

    return null
}

private fun extractItalicCaption(line: String): String? {
    val t = line.trim()
    if (t.length >= 2) {
        if (t.startsWith("*") && t.endsWith("*") && !t.startsWith("**") && !t.endsWith("**")) {
            return t.substring(1, t.length - 1).trim().ifEmpty { null }
        }
        if (t.startsWith("_") && t.endsWith("_") && !t.startsWith("__") && !t.endsWith("__")) {
            return t.substring(1, t.length - 1).trim().ifEmpty { null }
        }
    }
    return null
}

/**
 * Parses raw text content into a structured list of Markdown blocks (Headings, Lists, Paragraphs, CodeBlocks, etc.).
 */
fun parseMarkdownBlocks(raw: String): List<MarkdownBlock> {
    val blocks = mutableListOf<MarkdownBlock>()
    val lines = raw.lines()
    var i = 0

    val videoRegex = MD_VIDEO_REGEX
    val imageRegex = MD_IMAGE_REGEX
    val orderedRegex = MD_ORDERED_REGEX
    val calloutRegex = MD_CALLOUT_REGEX

    while (i < lines.size) {
        val line = lines[i]
        val trimmed = line.trim()

        if (trimmed.isEmpty()) {
            i++
            continue
        }

        // 1. Fenced Code block (``` ... ```)
        if (trimmed.startsWith("```")) {
            val lang = trimmed.removePrefix("```").trim().ifEmpty { null }
            val codeLines = mutableListOf<String>()
            i++
            while (i < lines.size && !lines[i].trim().startsWith("```")) {
                codeLines.add(lines[i])
                i++
            }
            if (i < lines.size) i++ // skip closing ```
            blocks.add(MarkdownBlock.CodeBlock(codeLines.joinToString("\n"), lang))
            continue
        }

        // 1b. Video block (::video[title](url))
        val videoMatch = videoRegex.find(trimmed)
        if (videoMatch != null) {
            val title = videoMatch.groupValues[1].trim()
            val url = if (videoMatch.groupValues.size > 2) videoMatch.groupValues[2].trim() else ""
            if (url.isNotBlank()) {
                blocks.add(MarkdownBlock.Video(title = title, url = url))
            }
            i++
            continue
        }

        // 1c. Image block (![alt](url) with optional *italic caption* on next line)
        val imageMatch = imageRegex.find(trimmed)
        if (imageMatch != null) {
            val alt = imageMatch.groupValues[1].trim()
            val url = imageMatch.groupValues[2].trim()
            var caption: String? = null
            if (i + 1 < lines.size) {
                val nextTrimmed = lines[i + 1].trim()
                val extracted = extractItalicCaption(nextTrimmed)
                if (extracted != null) {
                    caption = extracted
                    i++ // consume caption line
                }
            }
            blocks.add(MarkdownBlock.Image(alt = alt, url = url, caption = caption))
            i++
            continue
        }

        // 2. Headings: #, ##, ###, ####
        if (trimmed.startsWith("#")) {
            val hashCount = trimmed.takeWhile { it == '#' }.length
            if (hashCount in 1..6 && trimmed.length > hashCount && trimmed[hashCount] == ' ') {
                val headingText = trimmed.substring(hashCount).trim()
                blocks.add(MarkdownBlock.Heading(hashCount, headingText))
                i++
                continue
            }
        }

        // 3. Unordered list: lines starting with "- " or "* "
        if (trimmed.startsWith("- ") || (trimmed.startsWith("* ") && trimmed.length > 2 && trimmed[2] != '*')) {
            val listItems = mutableListOf<String>()
            while (i < lines.size) {
                val l = lines[i].trim()
                if (l.startsWith("- ") || (l.startsWith("* ") && l.length > 2 && l[2] != '*')) {
                    listItems.add(l.substring(2).trim())
                    i++
                } else if (l.isEmpty()) {
                    var peek = i + 1
                    while (peek < lines.size && lines[peek].trim().isEmpty()) {
                        peek++
                    }
                    if (peek < lines.size && (lines[peek].trim().startsWith("- ") || lines[peek].trim().startsWith("* "))) {
                        i = peek
                    } else {
                        break
                    }
                } else {
                    break
                }
            }
            blocks.add(MarkdownBlock.UnorderedList(listItems))
            continue
        }

        // 4. Ordered list: lines matching "1. ", "2. ", etc.
        val orderedMatch = orderedRegex.find(trimmed)
        if (orderedMatch != null) {
            val listItems = mutableListOf<Pair<Int, String>>()
            while (i < lines.size) {
                val l = lines[i].trim()
                val m = orderedRegex.find(l)
                if (m != null) {
                    val num = m.groupValues[1].toIntOrNull() ?: (listItems.size + 1)
                    val itemText = m.groupValues[2].trim()
                    listItems.add(Pair(num, itemText))
                    i++
                } else if (l.isEmpty()) {
                    var peek = i + 1
                    while (peek < lines.size && lines[peek].trim().isEmpty()) {
                        peek++
                    }
                    if (peek < lines.size && orderedRegex.matches(lines[peek].trim())) {
                        i = peek
                    } else {
                        break
                    }
                } else {
                    break
                }
            }
            blocks.add(MarkdownBlock.OrderedList(listItems))
            continue
        }

        // 4b. Table: baris diawali "|", baris berikutnya adalah separator (|---|:---:|)
        if (trimmed.startsWith("|")) {
            val nextLine = if (i + 1 < lines.size) lines[i + 1].trim() else ""
            fun isSeparatorRow(l: String): Boolean {
                if (!l.startsWith("|")) return false
                var s = l.removePrefix("|")
                if (s.endsWith("|")) s = s.removeSuffix("|")
                val cells = s.split("|")
                if (cells.isEmpty()) return false
                return cells.all { it.trim().matches(MD_TABLE_SEP_REGEX) }
            }
            if (isSeparatorRow(nextLine)) {
                fun splitRow(l: String): List<String> {
                    var s = l.trim()
                    if (s.startsWith("|")) s = s.removePrefix("|")
                    if (s.endsWith("|")) s = s.removeSuffix("|")
                    return s.split("|").map { it.trim() }
                }
                val headerCells = splitRow(trimmed)
                val headerCount = headerCells.size
                val alignCells = splitRow(nextLine)
                val alignments = (0 until headerCount).map { colIndex ->
                    val c = alignCells.getOrNull(colIndex)?.trim() ?: ""
                    when {
                        c.startsWith(":") && c.endsWith(":") -> TableAlign.CENTER
                        c.endsWith(":") -> TableAlign.END
                        else -> TableAlign.START
                    }
                }
                i += 2
                val rows = mutableListOf<List<String>>()
                while (i < lines.size && lines[i].trim().startsWith("|")) {
                    val rawCells = splitRow(lines[i].trim())
                    val normalizedCells = if (rawCells.size < headerCount) {
                        rawCells + List(headerCount - rawCells.size) { "" }
                    } else if (rawCells.size > headerCount) {
                        rawCells.take(headerCount)
                    } else {
                        rawCells
                    }
                    rows.add(normalizedCells)
                    i++
                }
                blocks.add(MarkdownBlock.Table(headerCells, alignments, rows))
                continue
            }
        }

        // 5. Blockquote / Callout: lines starting with ">"
        if (trimmed.startsWith(">")) {
            val quoteLines = mutableListOf<String>()
            while (i < lines.size && lines[i].trim().startsWith(">")) {
                quoteLines.add(lines[i].trim().removePrefix(">").trim())
                i++
            }
            val firstLine = quoteLines.firstOrNull() ?: ""
            val calloutMatch = calloutRegex.find(firstLine)
            if (calloutMatch != null) {
                val typeStr = calloutMatch.groupValues[1]
                val customTitle = calloutMatch.groupValues[2].trim().ifEmpty { null }
                val bodyText = quoteLines.drop(1).joinToString("\n").trim()
                blocks.add(
                    MarkdownBlock.Callout(
                        type = CalloutType.fromString(typeStr),
                        title = customTitle,
                        text = bodyText
                    )
                )
            } else {
                blocks.add(MarkdownBlock.Blockquote(quoteLines.joinToString("\n")))
            }
            continue
        }

        // 6. Regular paragraph (combines lines until empty line or block marker)
        val paraLines = mutableListOf<String>()
        while (i < lines.size) {
            val l = lines[i]
            val t = l.trim()
            if (t.isEmpty() || t.startsWith("#") || t.startsWith("- ") ||
                (t.startsWith("* ") && t.length > 2 && t[2] != '*') ||
                t.startsWith(">") || t.startsWith("```") || t.startsWith("|") ||
                videoRegex.matches(t) || imageRegex.matches(t) ||
                orderedRegex.containsMatchIn(t)) {
                break
            }
            paraLines.add(t)
            i++
        }
        if (paraLines.isNotEmpty()) {
            blocks.add(MarkdownBlock.Paragraph(paraLines.joinToString(" ")))
        }
    }

    return blocks
}

/**
 * Full Markdown content renderer for module reading, supporting headings, lists,
 * code blocks, blockquotes, and inline clickable glossary tooltips.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarkdownContent(
    content: String,
    modifier: Modifier = Modifier,
    courseId: String? = null
) {
    val blocks = remember(content) { parseMarkdownBlocks(content) }
    val isDark = isSystemInDarkTheme()

    val glossaryMap = remember(courseId) { GlossaryRegistry.getGlossaryForCourse(courseId) }
    val glossaryRegex = remember(courseId) { GlossaryRegistry.getGlossaryRegexForCourse(courseId) }

    var selectedGlossaryItem by remember { mutableStateOf<GlossaryItem?>(null) }

    Column(modifier = modifier.fillMaxWidth()) {
        blocks.forEachIndexed { index, block ->
            when (block) {
                is MarkdownBlock.Heading -> {
                    val headingStyle = when (block.level) {
                        1 -> MaterialTheme.typography.headlineSmall
                        2 -> MaterialTheme.typography.titleLarge
                        else -> MaterialTheme.typography.titleMedium
                    }
                    val headingColor = when (block.level) {
                        1 -> MaterialTheme.colorScheme.onSurface
                        2 -> if (isDark) TealLight else TealDark
                        else -> if (isDark) CyanAccent else TealPrimary
                    }
                    val topPadding = if (index == 0) 0.dp else when (block.level) {
                        1 -> 18.dp
                        2 -> 16.dp
                        else -> 12.dp
                    }

                    Text(
                        text = block.text,
                        style = headingStyle,
                        fontWeight = FontWeight.Bold,
                        color = headingColor,
                        modifier = Modifier.padding(top = topPadding, bottom = 6.dp)
                    )
                }

                is MarkdownBlock.Paragraph -> {
                    MarkdownText(
                        text = block.text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 22.sp,
                        glossaryMap = glossaryMap,
                        glossaryRegex = glossaryRegex,
                        onGlossaryClick = { item -> selectedGlossaryItem = item },
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                is MarkdownBlock.UnorderedList -> {
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        block.items.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = "•",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 16.sp,
                                    color = if (isDark) CyanAccent else TealLight,
                                    modifier = Modifier.padding(end = 10.dp, top = 1.dp)
                                )
                                MarkdownText(
                                    text = item,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 22.sp,
                                    glossaryMap = glossaryMap,
                                    glossaryRegex = glossaryRegex,
                                    onGlossaryClick = { gItem -> selectedGlossaryItem = gItem },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                is MarkdownBlock.OrderedList -> {
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        block.items.forEach { (num, item) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = "$num.",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (isDark) TealLight else TealPrimary,
                                    modifier = Modifier.padding(end = 8.dp, top = 2.dp)
                                )
                                MarkdownText(
                                    text = item,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 22.sp,
                                    glossaryMap = glossaryMap,
                                    glossaryRegex = glossaryRegex,
                                    onGlossaryClick = { gItem -> selectedGlossaryItem = gItem },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                is MarkdownBlock.Blockquote -> {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                                    .background(if (isDark) CyanAccent else TealLight)
                            )
                            MarkdownText(
                                text = block.text,
                                style = MaterialTheme.typography.bodyMedium,
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 22.sp,
                                glossaryMap = glossaryMap,
                                glossaryRegex = glossaryRegex,
                                onGlossaryClick = { gItem -> selectedGlossaryItem = gItem },
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                is MarkdownBlock.Table -> {
                    MarkdownTable(
                        block = block,
                        glossaryMap = glossaryMap,
                        glossaryRegex = glossaryRegex,
                        onGlossaryClick = { item -> selectedGlossaryItem = item }
                    )
                }

                is MarkdownBlock.CodeBlock -> {
                    Spacer(modifier = Modifier.height(6.dp))
                    CodeBlockView(
                        code = block.code,
                        language = block.language ?: "code",
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                is MarkdownBlock.Image -> {
                    MarkdownImageView(
                        image = block,
                        courseId = courseId,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                is MarkdownBlock.Video -> {
                    MarkdownVideoView(
                        video = block,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                is MarkdownBlock.Callout -> {
                    MarkdownCalloutView(
                        callout = block,
                        glossaryMap = glossaryMap,
                        glossaryRegex = glossaryRegex,
                        onGlossaryClick = { gItem -> selectedGlossaryItem = gItem },
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
            }
        }
    }

    // Interactive Glossary Tooltip Bottom Sheet
    val item = selectedGlossaryItem
    if (item != null) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { selectedGlossaryItem = null },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            dragHandle = {
                BottomSheetDefaults.DragHandle()
            },
            modifier = Modifier.testTag("glossary_bottom_sheet")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
                    .padding(bottom = 28.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = stringResource(R.string.markdown_glossary_cd),
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = stringResource(R.string.markdown_glossary_header),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = item.term,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    IconButton(
                        onClick = { selectedGlossaryItem = null },
                        modifier = Modifier.testTag("glossary_close_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.markdown_close)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = item.definition,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 24.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { selectedGlossaryItem = null },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(stringResource(R.string.markdown_close), fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

/**
 * Shimmer effect placeholder for image and video loading states.
 */
@Composable
fun ShimmerPlaceholder(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "shimmer_transition")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_anim"
    )
    val isDark = isSystemInDarkTheme()
    val shimmerColors = if (isDark) {
        listOf(
            Color(0xFF262320),
            Color(0xFF3B3632),
            Color(0xFF262320)
        )
    } else {
        listOf(
            Color(0xFFECE7DF),
            Color(0xFFFAF7F2),
            Color(0xFFECE7DF)
        )
    }

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 400f, translateAnim - 400f),
        end = Offset(translateAnim, translateAnim)
    )

    Box(
        modifier = modifier.background(brush)
    )
}

/**
 * Neat error placeholder shown if an image fails to load or asset is missing.
 */
@Composable
fun ImageErrorPlaceholder(
    alt: String,
    rawUrl: String,
    modifier: Modifier = Modifier
) {
    val fileName = remember(rawUrl) {
        rawUrl.substringAfterLast("/").substringBefore("?")
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.BrokenImage,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = if (alt.isNotBlank()) alt else stringResource(R.string.markdown_image_default_title),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
            ) {
                Text(
                    text = stringResource(R.string.markdown_image_asset_format, fileName),
                    style = MaterialTheme.typography.labelSmall,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.markdown_image_preparing_desc),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}


/**
 * Renders an image with Coil, shimmer loading placeholder, graceful error fallback,
 * and an optional caption rendered in italics directly below the image.
 */
@Composable
fun MarkdownImageView(
    image: MarkdownBlock.Image,
    courseId: String? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val resolvedModel = remember(image.url, courseId) {
        resolveImageUri(image.url, courseId)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("markdown_image_${image.alt.lowercase().replace(" ", "_")}"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(context)
                .data(resolvedModel)
                .crossfade(true)
                .build(),
            contentDescription = image.alt.ifEmpty { stringResource(R.string.markdown_image_cd) },
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            loading = {
                ShimmerPlaceholder(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            },
            error = {
                ImageErrorPlaceholder(
                    alt = image.alt,
                    rawUrl = image.url
                )
            }
        )

        if (!image.caption.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = image.caption,
                style = MaterialTheme.typography.bodySmall,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            )
        }
    }
}

/**
 * Renders a video preview thumbnail card with a prominent play button,
 * launching the video via Intent to YouTube or the system browser.
 */
@Composable
fun MarkdownVideoView(
    video: MarkdownBlock.Video,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val videoId = remember(video.url) { extractYouTubeVideoId(video.url) }

    val onPlayClick = {
        try {
            val parsedUri = Uri.parse(video.url)
            val intent = Intent(Intent.ACTION_VIEW, parsedUri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (_: Exception) {
            // Graceful fallback - prevent crash if no browser or invalid intent
        }
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onPlayClick)
            .testTag("markdown_video_card"),
        shape = RoundedCornerShape(14.dp),
        color = CodeDarkBg,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f),
            contentAlignment = Alignment.Center
        ) {
            // Thumbnail image if YouTube video ID is present
            if (videoId != null) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(context)
                        .data("https://img.youtube.com/vi/$videoId/hqdefault.jpg")
                        .crossfade(true)
                        .build(),
                    contentDescription = video.title.ifEmpty { "Video" },
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    loading = {
                        ShimmerPlaceholder(modifier = Modifier.fillMaxSize())
                    },
                    error = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Brush.verticalGradient(listOf(TealDark, CodeDarkBg))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SmartDisplay,
                                contentDescription = null,
                                tint = TealLight.copy(alpha = 0.5f),
                                modifier = Modifier.size(54.dp)
                            )
                        }
                    }
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.verticalGradient(listOf(TealDark, CodeDarkBg))),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SmartDisplay,
                        contentDescription = null,
                        tint = TealLight.copy(alpha = 0.5f),
                        modifier = Modifier.size(54.dp)
                    )
                }
            }

            // Dark semi-transparent scrim overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.38f))
            )

            // Centered Play Button
            Surface(
                shape = CircleShape,
                color = Color(0xFFE53935),
                shadowElevation = 8.dp,
                modifier = Modifier.size(54.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = stringResource(R.string.markdown_video_play_cd_format, video.title),
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            // Title banner and source indicator at bottom
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                        )
                    )
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = video.title.ifEmpty { stringResource(R.string.markdown_video_default_title) },
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color.Black.copy(alpha = 0.6f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = if (videoId != null) "YouTube" else "Video",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontSize = 10.sp
                        )
                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(10.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Renders callout notification blocks (> [!TIP], > [!WARNING], > [!INFO], > [!NOTE], > [!IMPORTANT]).
 */
@Composable
fun MarkdownCalloutView(
    callout: MarkdownBlock.Callout,
    glossaryMap: Map<String, GlossaryItem>,
    glossaryRegex: Regex?,
    onGlossaryClick: (GlossaryItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val (calloutColor, icon, defaultTitle) = when (callout.type) {
        CalloutType.TIP -> Triple(
            if (isDark) TealLight else TealPrimary,
            Icons.Default.Lightbulb,
            stringResource(R.string.markdown_callout_tips)
        )
        CalloutType.WARNING -> Triple(
            FlameOrange,
            Icons.Default.Warning,
            stringResource(R.string.markdown_callout_warning)
        )
        CalloutType.INFO -> Triple(
            if (isDark) CyanAccent else CyanAccentText,
            Icons.Default.Info,
            stringResource(R.string.markdown_callout_info)
        )
        CalloutType.NOTE -> Triple(
            if (isDark) TealLight else TealPrimary,
            Icons.Default.Info,
            stringResource(R.string.markdown_callout_note)
        )
        CalloutType.IMPORTANT, CalloutType.CAUTION -> Triple(
            ErrorRed,
            Icons.Default.PriorityHigh,
            stringResource(R.string.markdown_callout_important)
        )
    }

    val displayTitle = callout.title ?: defaultTitle

    Surface(
        color = calloutColor.copy(alpha = if (isDark) 0.12f else 0.08f),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, calloutColor.copy(alpha = if (isDark) 0.35f else 0.25f)),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .testTag("markdown_callout_${callout.type.name.lowercase()}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            // Left accent stripe
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(calloutColor)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                // Header: Icon + Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = calloutColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = displayTitle,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = calloutColor
                    )
                }

                if (callout.text.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    MarkdownText(
                        text = callout.text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 22.sp,
                        glossaryMap = glossaryMap,
                        glossaryRegex = glossaryRegex,
                        onGlossaryClick = onGlossaryClick
                    )
                }
            }
        }
    }
}

/**
 * Custom table layout for MarkdownBlock.Table:
 * - Equal width for all cells in the same column (widest cell, bounded [96.dp, 220.dp]).
 * - Proportional expansion to full width if total width < availableWidth.
 * - Horizontal scroll if total width > availableWidth.
 * - Cells wrap text when exceeding column width; row height matches tallest cell in the row.
 * - All cells vertically centered in their respective rows.
 * - Row backgrounds (header & zebra striping) span the entire table width.
 * - Column text alignments (START, CENTER, END) applied to headers and data cells.
 * - Preserves inline formatting (bold, italic, code) and glossary tooltips.
 */
@Composable
fun MarkdownTable(
    block: MarkdownBlock.Table,
    glossaryMap: Map<String, GlossaryItem> = emptyMap(),
    glossaryRegex: Regex? = null,
    onGlossaryClick: (GlossaryItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (block.headers.isEmpty()) return

    val isDark = isSystemInDarkTheme()
    val density = LocalDensity.current
    val minColWidthPx = with(density) { 96.dp.roundToPx() }
    val maxColWidthPx = with(density) { 220.dp.roundToPx() }
    val dividerHeightPx = with(density) { 1.dp.roundToPx().coerceAtLeast(1) }

    val numCols = block.headers.size
    val allRows = remember(block.headers, block.rows) {
        listOf(block.headers) + block.rows
    }
    val numRows = allRows.size

    val headerBgColor = if (isDark) TealDark.copy(alpha = 0.35f) else TealLight.copy(alpha = 0.3f)
    val zebraBgColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
    val dividerColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)
    val headerDividerColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val availableWidthPx = constraints.maxWidth
            val scrollState = rememberScrollState()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
            ) {
                SubcomposeLayout { _ ->
                    // 1. Subcompose all cells to calculate natural width & measure
                    val cellMeasurables = subcompose("cells") {
                        allRows.forEachIndexed { r, row ->
                            val isHeader = r == 0
                            row.forEachIndexed { c, cellText ->
                                val align = block.alignments.getOrElse(c) { TableAlign.START }
                                val textAlign = when (align) {
                                    TableAlign.START -> TextAlign.Start
                                    TableAlign.CENTER -> TextAlign.Center
                                    TableAlign.END -> TextAlign.End
                                }
                                Box(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                    contentAlignment = when (align) {
                                        TableAlign.START -> Alignment.CenterStart
                                        TableAlign.CENTER -> Alignment.Center
                                        TableAlign.END -> Alignment.CenterEnd
                                    }
                                ) {
                                    MarkdownText(
                                        text = cellText,
                                        style = if (isHeader) {
                                            MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                        } else {
                                            MaterialTheme.typography.bodySmall
                                        },
                                        textAlign = textAlign,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        glossaryMap = glossaryMap,
                                        glossaryRegex = glossaryRegex,
                                        onGlossaryClick = onGlossaryClick,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }

                    // 2. Compute natural column widths from maxIntrinsicWidth
                    val colNaturalWidths = IntArray(numCols) { col ->
                        var maxIntrinsic = minColWidthPx
                        for (r in 0 until numRows) {
                            val idx = r * numCols + col
                            if (idx < cellMeasurables.size) {
                                val w = cellMeasurables[idx].maxIntrinsicWidth(Constraints.Infinity)
                                if (w > maxIntrinsic) maxIntrinsic = w
                            }
                        }
                        maxIntrinsic.coerceIn(minColWidthPx, maxColWidthPx)
                    }

                    val totalNaturalWidth = colNaturalWidths.sum()

                    // 3. Proportionally expand columns if totalNaturalWidth < availableWidthPx
                    val finalColWidths = IntArray(numCols)
                    if (availableWidthPx in 1 until Constraints.Infinity && totalNaturalWidth < availableWidthPx) {
                        var allocated = 0
                        for (c in 0 until numCols) {
                            if (c == numCols - 1) {
                                finalColWidths[c] = (availableWidthPx - allocated).coerceAtLeast(minColWidthPx)
                            } else {
                                val share = (colNaturalWidths[c].toDouble() / totalNaturalWidth * availableWidthPx).toInt()
                                finalColWidths[c] = share.coerceAtLeast(minColWidthPx)
                                allocated += finalColWidths[c]
                            }
                        }
                    } else {
                        for (c in 0 until numCols) {
                            finalColWidths[c] = colNaturalWidths[c]
                        }
                    }

                    val tableTotalWidth = finalColWidths.sum()

                    // 4. Measure each cell with its exact column width
                    val cellPlaceables = Array(numRows) { r ->
                        Array(numCols) { c ->
                            val idx = r * numCols + c
                            val colW = finalColWidths[c]
                            cellMeasurables[idx].measure(
                                Constraints(
                                    minWidth = colW,
                                    maxWidth = colW,
                                    minHeight = 0,
                                    maxHeight = Constraints.Infinity
                                )
                            )
                        }
                    }

                    // 5. Compute each row's height based on its tallest cell
                    val rowHeights = IntArray(numRows) { r ->
                        var maxH = 0
                        for (c in 0 until numCols) {
                            val h = cellPlaceables[r][c].height
                            if (h > maxH) maxH = h
                        }
                        maxH
                    }

                    // 6. Subcompose and measure row backgrounds & dividers
                    val bgMeasurables = subcompose("backgrounds") {
                        for (r in 0 until numRows) {
                            val bgColor = when {
                                r == 0 -> headerBgColor
                                r % 2 == 1 -> zebraBgColor
                                else -> Color.Transparent
                            }
                            Box(modifier = Modifier.background(bgColor))
                        }
                        for (r in 0 until numRows - 1) {
                            val dColor = if (r == 0) headerDividerColor else dividerColor
                            Box(modifier = Modifier.background(dColor))
                        }
                    }

                    val bgPlaceables = Array(numRows) { r ->
                        bgMeasurables[r].measure(
                            Constraints.fixed(tableTotalWidth, rowHeights[r])
                        )
                    }

                    val dividerPlaceables = Array(numRows - 1) { r ->
                        bgMeasurables[numRows + r].measure(
                            Constraints.fixed(tableTotalWidth, dividerHeightPx)
                        )
                    }

                    // 7. Calculate total table height
                    val totalTableHeight = rowHeights.sum() + (numRows - 1) * dividerHeightPx

                    // Calculate X offsets for columns
                    val colXOffsets = IntArray(numCols)
                    var curX = 0
                    for (c in 0 until numCols) {
                        colXOffsets[c] = curX
                        curX += finalColWidths[c]
                    }

                    // 8. Place everything
                    layout(tableTotalWidth, totalTableHeight) {
                        var curY = 0
                        for (r in 0 until numRows) {
                            val rowH = rowHeights[r]

                            // Place row background across entire table width
                            bgPlaceables[r].placeRelative(0, curY)

                            // Place each cell in this row, vertically centered
                            for (c in 0 until numCols) {
                                val cell = cellPlaceables[r][c]
                                val yOffset = curY + (rowH - cell.height) / 2
                                cell.placeRelative(colXOffsets[c], yOffset)
                            }

                            curY += rowH

                            // Place divider if not last row
                            if (r < numRows - 1) {
                                dividerPlaceables[r].placeRelative(0, curY)
                                curY += dividerHeightPx
                            }
                        }
                    }
                }
            }
        }
    }
}


