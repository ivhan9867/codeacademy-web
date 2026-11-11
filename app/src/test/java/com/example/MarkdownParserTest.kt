package com.example

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.ui.components.MarkdownBlock
import com.example.ui.components.TableAlign
import com.example.ui.components.parseMarkdownBlocks
import com.example.ui.components.parseMarkdownToAnnotatedString
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MarkdownParserTest {

    @Test
    fun testInlineMarkdown_boldAndInlineCodeCombination() {
        val input = "**`<head>`**: Berisi metadata"
        val annotated = parseMarkdownToAnnotatedString(
            text = input,
            codeBackgroundColor = Color.LightGray,
            codeTextColor = Color.Blue
        )

        // Raw text inside AnnotatedString should be "<head>: Berisi metadata"
        assertEquals("<head>: Berisi metadata", annotated.text)

        // Verify span styles
        val styles = annotated.spanStyles
        assertTrue("Should have span styles", styles.isNotEmpty())

        // Check that bold is applied to <head>
        val boldSpan = styles.find { it.item.fontWeight == FontWeight.Bold }
        assertTrue("Bold span must be present", boldSpan != null)
        assertEquals(0, boldSpan!!.start)
        assertEquals(6, boldSpan.end)

        // Check that code style (Monospace) is applied to <head>
        val codeSpan = styles.find { it.item.fontFamily == FontFamily.Monospace }
        assertTrue("Code span must be present", codeSpan != null)
        assertEquals(0, codeSpan!!.start)
        assertEquals(6, codeSpan.end)
    }

    @Test
    fun testInlineMarkdown_multipleInlineCodesAndItalics() {
        val input = "HTML (*HyperText Markup Language*) dengan `<!DOCTYPE html>` dan `<html>`."
        val annotated = parseMarkdownToAnnotatedString(
            text = input,
            codeBackgroundColor = Color.LightGray,
            codeTextColor = Color.Blue
        )

        assertEquals("HTML (HyperText Markup Language) dengan <!DOCTYPE html> dan <html>.", annotated.text)

        val italicSpan = annotated.spanStyles.find { it.item.fontStyle == FontStyle.Italic }
        assertTrue("Italic span must be present", italicSpan != null)

        val codeSpans = annotated.spanStyles.filter { it.item.fontFamily == FontFamily.Monospace }
        assertEquals("Should have 2 code spans", 2, codeSpans.size)
    }

    @Test
    fun testInlineMarkdown_plainAsteriskMultiplicationDoesNotBreak() {
        val input = "Hasil 2 * 3 = 6 adalah benar"
        val annotated = parseMarkdownToAnnotatedString(
            text = input,
            codeBackgroundColor = Color.LightGray,
            codeTextColor = Color.Blue
        )

        assertEquals("Hasil 2 * 3 = 6 adalah benar", annotated.text)
        assertTrue("No bold or italic span should be formed", annotated.spanStyles.isEmpty())
    }

    @Test
    fun testBlockParser_htmlFundamentalsModuleContent() {
        val raw = """
            # Pengenalan HTML

            Selamat datang di langkah pertama perjalanan web development kamu! 🚀

            HTML (*HyperText Markup Language*) adalah bahasa standar yang digunakan untuk membuat dan menyusun struktur sebuah halaman web.

            ### Anatomi Dasar Dokumen HTML:
            Setiap halaman web HTML5 diawali dengan deklarasi `<!DOCTYPE html>` dan elemen utama `<html>`, `<head>`, serta `<body>`.

            - **`<head>`**: Berisi metadata, judul halaman, dan link stylesheet.
            - **`<body>`**: Berisi seluruh konten visual yang dilihat pengguna di browser.
        """.trimIndent()

        val blocks = parseMarkdownBlocks(raw)

        // 1. Heading 1
        assertTrue(blocks[0] is MarkdownBlock.Heading)
        assertEquals(1, (blocks[0] as MarkdownBlock.Heading).level)
        assertEquals("Pengenalan HTML", (blocks[0] as MarkdownBlock.Heading).text)

        // 2. Paragraph 1
        assertTrue(blocks[1] is MarkdownBlock.Paragraph)

        // 3. Paragraph 2
        assertTrue(blocks[2] is MarkdownBlock.Paragraph)

        // 4. Heading 3
        assertTrue(blocks[3] is MarkdownBlock.Heading)
        assertEquals(3, (blocks[3] as MarkdownBlock.Heading).level)
        assertEquals("Anatomi Dasar Dokumen HTML:", (blocks[3] as MarkdownBlock.Heading).text)

        // 5. Paragraph 3
        assertTrue(blocks[4] is MarkdownBlock.Paragraph)

        // 6. Unordered List
        assertTrue(blocks[5] is MarkdownBlock.UnorderedList)
        val list = blocks[5] as MarkdownBlock.UnorderedList
        assertEquals(2, list.items.size)
        assertEquals("**`<head>`**: Berisi metadata, judul halaman, dan link stylesheet.", list.items[0])
        assertEquals("**`<body>`**: Berisi seluruh konten visual yang dilihat pengguna di browser.", list.items[1])
    }

    @Test
    fun testBlockParser_orderedListAndFencedCodeBlock() {
        val raw = """
            ### 4 Lapisan Box Model:
            1. **Content**: Area tempat teks atau gambar berada
            2. **Padding**: Ruang transparan di dalam border mengelilingi konten

            ```css
            .kotak {
              padding: 16px;
            }
            ```
        """.trimIndent()

        val blocks = parseMarkdownBlocks(raw)
        assertEquals(3, blocks.size)

        assertTrue(blocks[0] is MarkdownBlock.Heading)
        assertTrue(blocks[1] is MarkdownBlock.OrderedList)
        val orderedList = blocks[1] as MarkdownBlock.OrderedList
        assertEquals(2, orderedList.items.size)
        assertEquals(1, orderedList.items[0].first)
        assertEquals("**Content**: Area tempat teks atau gambar berada", orderedList.items[0].second)

        assertTrue(blocks[2] is MarkdownBlock.CodeBlock)
        val codeBlock = blocks[2] as MarkdownBlock.CodeBlock
        assertEquals("css", codeBlock.language)
        assertTrue(codeBlock.code.contains(".kotak"))
    }

    @Test
    fun testBlockParser_imageWithLocalAssetAndCaption() {
        val raw = """
            ![Diagram Box Model](asset:images/fe-web-foundation-00/box-model.png)
            *Diagram 4 lapisan Box Model dalam CSS*
        """.trimIndent()

        val blocks = parseMarkdownBlocks(raw)
        assertEquals(1, blocks.size)
        assertTrue("Block must be Image", blocks[0] is MarkdownBlock.Image)

        val image = blocks[0] as MarkdownBlock.Image
        assertEquals("Diagram Box Model", image.alt)
        assertEquals("asset:images/fe-web-foundation-00/box-model.png", image.url)
        assertEquals("Diagram 4 lapisan Box Model dalam CSS", image.caption)
    }

    @Test
    fun testBlockParser_imageWithUrlAndNoCaption() {
        val raw = """
            ![Struktur Web](https://example.com/images/structure.png)

            Paragraf berikutnya setelah gambar.
        """.trimIndent()

        val blocks = parseMarkdownBlocks(raw)
        assertEquals(2, blocks.size)
        assertTrue(blocks[0] is MarkdownBlock.Image)
        assertTrue(blocks[1] is MarkdownBlock.Paragraph)

        val image = blocks[0] as MarkdownBlock.Image
        assertEquals("Struktur Web", image.alt)
        assertEquals("https://example.com/images/structure.png", image.url)
        org.junit.Assert.assertNull(image.caption)
    }

    @Test
    fun testBlockParser_videoSyntax() {
        val raw = """
            ::video[Tutorial Flexbox Dasar](https://youtube.com/watch?v=dQw4w9WgXcQ)
        """.trimIndent()

        val blocks = parseMarkdownBlocks(raw)
        assertEquals(1, blocks.size)
        assertTrue("Block must be Video", blocks[0] is MarkdownBlock.Video)

        val video = blocks[0] as MarkdownBlock.Video
        assertEquals("Tutorial Flexbox Dasar", video.title)
        assertEquals("https://youtube.com/watch?v=dQw4w9WgXcQ", video.url)
    }

    @Test
    fun testBlockParser_calloutTipWarningInfo() {
        val raw = """
            > [!TIP]
            > Gunakan tag semantik untuk meningkatkan SEO dan aksesibilitas.

            > [!WARNING] Jangan Hapus Doctype
            > Melewatkan doctype membuat browser masuk ke Quirks Mode.

            > [!INFO]
            > HTML5 resmi dirilis oleh W3C pada tahun 2014.
        """.trimIndent()

        val blocks = parseMarkdownBlocks(raw)
        assertEquals(3, blocks.size)

        // 1. TIP Callout
        assertTrue(blocks[0] is MarkdownBlock.Callout)
        val tip = blocks[0] as MarkdownBlock.Callout
        assertEquals(com.example.ui.components.CalloutType.TIP, tip.type)
        org.junit.Assert.assertNull(tip.title)
        assertEquals("Gunakan tag semantik untuk meningkatkan SEO dan aksesibilitas.", tip.text)

        // 2. WARNING Callout with custom title
        assertTrue(blocks[1] is MarkdownBlock.Callout)
        val warning = blocks[1] as MarkdownBlock.Callout
        assertEquals(com.example.ui.components.CalloutType.WARNING, warning.type)
        assertEquals("Jangan Hapus Doctype", warning.title)
        assertEquals("Melewatkan doctype membuat browser masuk ke Quirks Mode.", warning.text)

        // 3. INFO Callout
        assertTrue(blocks[2] is MarkdownBlock.Callout)
        val info = blocks[2] as MarkdownBlock.Callout
        assertEquals(com.example.ui.components.CalloutType.INFO, info.type)
        org.junit.Assert.assertNull(info.title)
        assertEquals("HTML5 resmi dirilis oleh W3C pada tahun 2014.", info.text)
    }

    @Test
    fun testBlockParser_regularBlockquoteStillWorks() {
        val raw = """
            > Ini adalah kutipan biasa bukan callout.
        """.trimIndent()

        val blocks = parseMarkdownBlocks(raw)
        assertEquals(1, blocks.size)
        assertTrue("Block must be Blockquote", blocks[0] is MarkdownBlock.Blockquote)
        val quote = blocks[0] as MarkdownBlock.Blockquote
        assertEquals("Ini adalah kutipan biasa bukan callout.", quote.text)
    }

    @Test
    fun testExtractYouTubeVideoId() {
        val watchUrl = "https://youtube.com/watch?v=dQw4w9WgXcQ"
        val shortUrl = "https://youtu.be/dQw4w9WgXcQ"
        val embedUrl = "https://www.youtube.com/embed/dQw4w9WgXcQ"
        val nonYoutube = "https://example.com/video.mp4"

        assertEquals("dQw4w9WgXcQ", com.example.ui.components.extractYouTubeVideoId(watchUrl))
        assertEquals("dQw4w9WgXcQ", com.example.ui.components.extractYouTubeVideoId(shortUrl))
        assertEquals("dQw4w9WgXcQ", com.example.ui.components.extractYouTubeVideoId(embedUrl))
        org.junit.Assert.assertNull(com.example.ui.components.extractYouTubeVideoId(nonYoutube))
    }

    @Test
    fun testResolveImageUri() {
        val assetUrl = "asset:images/fe-web-foundation-00/box-model.png"
        val webUrl = "https://images.example.com/pic.png"

        assertEquals(
            "file:///android_asset/images/fe-web-foundation-00/box-model.png",
            com.example.ui.components.resolveImageUri(assetUrl)
        )
        assertEquals(
            "https://images.example.com/pic.png",
            com.example.ui.components.resolveImageUri(webUrl)
        )
    }

    @Test
    fun testBlockParser_mixedDocumentWithImageVideoAndCallout() {
        val raw = """
            # Web Development

            Paragraf pembuka materi web.

            > [!TIP] Tips Penting
            > Ingat untuk selalu memvalidasi input pengguna.

            ![Arsitektur Client-Server](asset:images/fe-web-foundation-00/client-server.png)
            *Alur permintaan browser ke server*

            ::video[Penjelasan Arsitektur](https://youtube.com/watch?v=12345678901)

            ```html
            <h1>Halo Dunia</h1>
            ```
        """.trimIndent()

        val blocks = parseMarkdownBlocks(raw)
        assertEquals(6, blocks.size)

        assertTrue(blocks[0] is MarkdownBlock.Heading)
        assertTrue(blocks[1] is MarkdownBlock.Paragraph)
        assertTrue(blocks[2] is MarkdownBlock.Callout)
        assertTrue(blocks[3] is MarkdownBlock.Image)
        assertTrue(blocks[4] is MarkdownBlock.Video)
        assertTrue(blocks[5] is MarkdownBlock.CodeBlock)

        val image = blocks[3] as MarkdownBlock.Image
        assertEquals("Arsitektur Client-Server", image.alt)
        assertEquals("Alur permintaan browser ke server", image.caption)

        val video = blocks[4] as MarkdownBlock.Video
        assertEquals("Penjelasan Arsitektur", video.title)
    }

    @Test
    fun testBlockParser_tableWithUnequalCellsNormalized() {
        val raw = """
            | Fitur | Flexbox | Grid |
            |---|---|---|
            | Dimensi | 1 Dimensi |
            | Penggunaan | Komponen UI | Tata Letak Halaman | Ekstra Kolom yang Diabaikan |
            | Kontrol | Konten |
        """.trimIndent()

        val blocks = parseMarkdownBlocks(raw)
        assertEquals(1, blocks.size)
        assertTrue("Block must be Table", blocks[0] is MarkdownBlock.Table)

        val table = blocks[0] as MarkdownBlock.Table
        assertEquals(3, table.headers.size)
        assertEquals(listOf("Fitur", "Flexbox", "Grid"), table.headers)

        // All rows must have exactly 3 cells
        assertEquals(3, table.rows.size)
        // Row 0 had 2 cells -> padded with ""
        assertEquals(listOf("Dimensi", "1 Dimensi", ""), table.rows[0])
        // Row 1 had 4 cells -> truncated to 3
        assertEquals(listOf("Penggunaan", "Komponen UI", "Tata Letak Halaman"), table.rows[1])
        // Row 2 had 2 cells -> padded with ""
        assertEquals(listOf("Kontrol", "Konten", ""), table.rows[2])
    }

    @Test
    fun testBlockParser_tableWithAlignments() {
        val raw = """
            | Perintah | Deskripsi | Status |
            | :--- | :---: | ---: |
            | `git status` | Cek status | Aktif |
            | `git commit` | Simpan perubahan | Selesai |
        """.trimIndent()

        val blocks = parseMarkdownBlocks(raw)
        assertEquals(1, blocks.size)
        assertTrue("Block must be Table", blocks[0] is MarkdownBlock.Table)

        val table = blocks[0] as MarkdownBlock.Table
        assertEquals(3, table.headers.size)
        assertEquals(3, table.alignments.size)

        // Verify column alignments: START, CENTER, END
        assertEquals(TableAlign.START, table.alignments[0])
        assertEquals(TableAlign.CENTER, table.alignments[1])
        assertEquals(TableAlign.END, table.alignments[2])

        assertEquals(2, table.rows.size)
        assertEquals(listOf("`git status`", "Cek status", "Aktif"), table.rows[0])
        assertEquals(listOf("`git commit`", "Simpan perubahan", "Selesai"), table.rows[1])
    }
}

