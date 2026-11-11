package com.example

import androidx.compose.ui.text.AnnotatedString
import com.example.ui.components.codeHighlightTransformation
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CodeSyntaxHighlighterTest {

    @Test
    fun testHtmlHighlightingPreservesTextLengthAndContent() {
        val rawHtml = """
            <header class="header">
              <!-- Comment -->
              <h1>Halo Dunia!</h1>
              <a href="https://example.com">Klik</a>
            </header>
        """.trimIndent()

        val transformation = codeHighlightTransformation("html")
        val transformed = transformation.filter(AnnotatedString(rawHtml))

        assertEquals(rawHtml, transformed.text.text)
        // Check that span styles were applied
        assertTrue(transformed.text.spanStyles.isNotEmpty())
        // Identity offset mapping test
        assertEquals(5, transformed.offsetMapping.originalToTransformed(5))
        assertEquals(10, transformed.offsetMapping.transformedToOriginal(10))
    }

    @Test
    fun testCssHighlightingPreservesTextLengthAndContent() {
        val rawCss = """
            body {
              color: #ffffff;
              font-size: 16px;
              margin: 0;
            }
        """.trimIndent()

        val transformation = codeHighlightTransformation("css")
        val transformed = transformation.filter(AnnotatedString(rawCss))

        assertEquals(rawCss, transformed.text.text)
        assertTrue(transformed.text.spanStyles.isNotEmpty())
    }

    @Test
    fun testJsHighlightingPreservesTextLengthAndContent() {
        val rawJs = """
            const count = 42;
            function add(a, b) {
              // Return sum
              return a + b;
            }
        """.trimIndent()

        val transformation = codeHighlightTransformation("js")
        val transformed = transformation.filter(AnnotatedString(rawJs))

        assertEquals(rawJs, transformed.text.text)
        assertTrue(transformed.text.spanStyles.isNotEmpty())
    }
}
