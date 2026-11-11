package com.example.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle

// Palet warna terinspirasi tema GitHub Dark
private val GhComment = Color(0xFF8B949E)
private val GhTag = Color(0xFF7EE787)
private val GhAttrName = Color(0xFF79C0FF)
private val GhString = Color(0xFFA5D6FF)
private val GhPunctuation = Color(0xFFC9D1D9)
private val GhDefault = Color(0xFFE6EDF3)
private val GhProperty = Color(0xFF7EE787)
private val GhNumber = Color(0xFF79C0FF)
private val GhKeyword = Color(0xFFFF7B72)

private val HTML_PATTERN = Regex(
    "(?<comment><!--[\\s\\S]*?-->)" +
        "|(?<tag></?[A-Za-z][A-Za-z0-9]*)" +
        "|(?<attr>\\b[a-zA-Z-]+(?=\\s*=))" +
        "|(?<string>\"[^\"]*\"|'[^']*')" +
        "|(?<punct>/?>)"
)

private val CSS_PATTERN = Regex(
    "(?<comment>/\\*[\\s\\S]*?\\*/)" +
        "|(?<string>\"[^\"]*\"|'[^']*')" +
        "|(?<hex>#[0-9a-fA-F]{3,8}\\b)" +
        "|(?<property>[a-zA-Z-]+(?=\\s*:))" +
        "|(?<number>\\b\\d+(\\.\\d+)?(px|em|rem|%|vh|vw|s|deg)?\\b)" +
        "|(?<punct>[{}:;,])"
)

private val JS_PATTERN = Regex(
    "(?<comment>//[^\\n]*|/\\*[\\s\\S]*?\\*/)" +
        "|(?<string>\"[^\"]*\"|'[^']*'|`[^`]*`)" +
        "|(?<keyword>\\b(function|const|let|var|return|if|else|for|while|true|false|null|undefined|new|this|typeof|break|continue|switch|case|default|try|catch|finally|class|extends|import|export|from|async|await)\\b)" +
        "|(?<number>\\b\\d+(\\.\\d+)?\\b)"
)

private val PYTHON_PATTERN = Regex(
    "(?<comment>#[^\\n]*)" +
        "|(?<string>f?\"\"\"[\\s\\S]*?\"\"\"|f?'''[\\s\\S]*?'''|f?\"[^\"\\n]*\"|f?'[^'\\n]*')" +
        "|(?<keyword>\\b(def|class|return|if|elif|else|for|while|in|is|not|and|or|True|False|None|import|from|as|try|except|finally|raise|pass|break|continue|lambda|with|global|nonlocal)\\b)" +
        "|(?<func>\\b(print|input|len|range|type|int|float|str|bool|list|dict|set|tuple|sum|min|max|sorted|enumerate|zip|open)(?=\\s*\\())" +
        "|(?<number>\\b\\d+(\\.\\d+)?\\b)"
)

private fun MatchGroupCollection.has(name: String): Boolean {
    val named = this as? MatchNamedGroupCollection ?: return false
    return try {
        named[name] != null
    } catch (_: IllegalArgumentException) {
        false
    }
}

private fun htmlColorFor(g: MatchGroupCollection): Color = when {
    g.has("comment") -> GhComment
    g.has("tag") -> GhTag
    g.has("attr") -> GhAttrName
    g.has("string") -> GhString
    g.has("punct") -> GhPunctuation
    else -> GhDefault
}

private fun cssColorFor(g: MatchGroupCollection): Color = when {
    g.has("comment") -> GhComment
    g.has("string") -> GhString
    g.has("hex") -> GhString
    g.has("property") -> GhProperty
    g.has("number") -> GhNumber
    g.has("punct") -> GhPunctuation
    else -> GhDefault
}

private fun jsColorFor(g: MatchGroupCollection): Color = when {
    g.has("comment") -> GhComment
    g.has("string") -> GhString
    g.has("keyword") -> GhKeyword
    g.has("number") -> GhNumber
    else -> GhDefault
}

private fun pythonColorFor(g: MatchGroupCollection): Color = when {
    g.has("comment") -> GhComment
    g.has("string") -> GhString
    g.has("keyword") -> GhKeyword
    g.has("func") -> GhTag
    g.has("number") -> GhNumber
    else -> GhDefault
}

private fun isBoldToken(g: MatchGroupCollection, language: String): Boolean {
    return when (language.lowercase()) {
        "js", "javascript" -> g.has("keyword")
        "python", "py" -> g.has("keyword")
        "html", "htm", "xml" -> g.has("tag")
        else -> false
    }
}

fun highlightCode(code: String, language: String): AnnotatedString {
    val (pattern, colorFor) = when (language.lowercase()) {
        "css" -> CSS_PATTERN to ::cssColorFor
        "js", "javascript" -> JS_PATTERN to ::jsColorFor
        "python", "py" -> PYTHON_PATTERN to ::pythonColorFor
        else -> HTML_PATTERN to ::htmlColorFor
    }
    return buildAnnotatedString {
        var lastIndex = 0
        for (match in pattern.findAll(code)) {
            if (match.range.first > lastIndex) {
                append(code.substring(lastIndex, match.range.first))
            }
            val bold = isBoldToken(match.groups, language)
            withStyle(
                SpanStyle(
                    color = colorFor(match.groups),
                    fontWeight = if (bold) androidx.compose.ui.text.font.FontWeight.Bold else null
                )
            ) {
                append(match.value)
            }
            lastIndex = match.range.last + 1
        }
        if (lastIndex < code.length) {
            append(code.substring(lastIndex))
        }
    }
}

/**
 * VisualTransformation ringan berbasis regex untuk mewarnai kode HTML/CSS/JS
 * di editor Playground, tanpa mengubah panjang atau isi teks aslinya.
 */
fun codeHighlightTransformation(language: String): VisualTransformation {
    return VisualTransformation { text ->
        TransformedText(highlightCode(text.text, language), OffsetMapping.Identity)
    }
}
