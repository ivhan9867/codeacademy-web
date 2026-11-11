package com.example

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import java.util.regex.Pattern

/**
 * Task 2 Automated Check (TAHAP 1.5-B):
 * Fails if any NEW unlocalized UI string literals appear in the active screen tree.
 * Single source of truth for unused subpackages: tools/unused_subpackages.txt.
 */
class ActiveTreeHardcodedStringCheckTest {

    companion object {
        /**
         * Fallback list of unused subpackages if config file cannot be found.
         * Must match tools/unused_subpackages.txt.
         */
        val FALLBACK_UNUSED_SUBPACKAGES = setOf(
            "app/src/main/java/com/example/ui/navigation",
            "app/src/main/java/com/example/ui/screens/auth",
            "app/src/main/java/com/example/ui/screens/home",
            "app/src/main/java/com/example/ui/screens/catalog",
            "app/src/main/java/com/example/ui/screens/profile",
            "app/src/main/java/com/example/ui/screens/social",
            "app/src/main/java/com/example/ui/screens/forum",
            "app/src/main/java/com/example/ui/screens/quiz",
            "app/src/main/java/com/example/ui/screens/lesson",
            "app/src/main/java/com/example/ui/screens/playground",
            "app/src/main/java/com/example/ui/screens/aitutor",
            "app/src/main/java/com/example/ui/screens/notifications",
            "app/src/main/java/com/example/ui/screens/ar",
            "app/src/main/java/com/example/ui/screens/leaderboard",
            "app/src/main/java/com/example/data/repository",
            "app/src/main/java/com/example/data/model"
        )
    }

    /**
     * Baseline ceiling for the active tree hardcoded UI string literals.
     * PENTING: Angka ini (diturunkan dari 680 ke 300 setelah lokalisasi Batch A & Batch B) hanya boleh TURUN seiring migrasi string ke strings.xml;
     * menaikkannya DILARANG KERAS dan membutuhkan alasan tertulis serta persetujuan arsitektur.
     * Satu penambahan literal baru di pohon aktif harus membuat test ini GAGAL.
     */
    private val BASELINE_CEILING = 300

    private val uiLiteralPattern = Pattern.compile(
        """(?:Text\s*\(\s*text\s*=\s*|Text\s*\(\s*|text\s*=\s*|title\s*=\s*|label\s*=\s*|placeholder\s*=\s*|contentDescription\s*=\s*|subtitle\s*=\s*|description\s*=\s*)"([^"\n]{2,})""""
    )

    /**
     * Resolves unused subpackages from tools/unused_subpackages.txt (Single Source of Truth).
     */
    private fun resolveUnusedSubpackages(): Set<String> {
        val candidates = listOf(
            File("../tools/unused_subpackages.txt"), // When running from app/ module directory
            File("tools/unused_subpackages.txt"),    // When running from root workspace directory
            File("app/../tools/unused_subpackages.txt")
        )
        val file = candidates.firstOrNull { it.exists() && it.isFile }
        return file?.readLines()
            ?.map { it.trim() }
            ?.filter { it.isNotEmpty() && !it.startsWith("#") }
            ?.toSet()
            ?: FALLBACK_UNUSED_SUBPACKAGES
    }

    /**
     * Resolves source directory regardless of whether Gradle executes from app/ or project root.
     */
    private fun resolveSourceDir(): File {
        val candidates = listOf(
            File("src/main/java/com/example"),           // standard Gradle submodule working dir (app/)
            File("app/src/main/java/com/example"),       // root project working dir
            File("../app/src/main/java/com/example")
        )
        return candidates.firstOrNull { it.exists() && it.isDirectory }
            ?: error("Source directory not found. user.dir=${System.getProperty("user.dir")}")
    }

    /**
     * Counts hardcoded UI string literals in a given Kotlin code content.
     */
    fun countHardcodedUiStrings(content: String): Int {
        val matcher = uiLiteralPattern.matcher(content)
        var count = 0
        while (matcher.find()) {
            val literal = matcher.group(1) ?: continue
            if (literal.startsWith("http") || literal.startsWith("com.") ||
                literal.startsWith("ic_") || literal.startsWith("asset:")
            ) {
                continue
            }
            count++
        }
        return count
    }

    @Test
    fun testActiveTree_noNewHardcodedUiStringLiterals() {
        val unusedSubpackages = resolveUnusedSubpackages()
        val rootDir = resolveSourceDir()
        val activeFiles = mutableListOf<File>()

        rootDir.walkTopDown().forEach { file ->
            if (file.isFile && file.extension == "kt") {
                val relPath = file.path.replace("\\", "/")
                val isIgnored = unusedSubpackages.any { relPath.contains(it.removePrefix("app/")) } ||
                        (relPath.contains("ui/viewmodel/") && !relPath.endsWith("SocialViewModel.kt"))
                if (!isIgnored) {
                    activeFiles.add(file)
                }
            }
        }

        assertTrue(
            "Active source files must not be empty! Directory resolved: ${rootDir.absolutePath}",
            activeFiles.size >= 40
        )

        var totalActiveHardcoded = 0
        val fileCounts = mutableMapOf<String, Int>()

        for (file in activeFiles) {
            val content = file.readText()
            val count = countHardcodedUiStrings(content)
            if (count > 0) {
                totalActiveHardcoded += count
                fileCounts[file.name] = count
            }
        }

        assertTrue(
            "New hardcoded UI string literals detected! Current active tree count: $totalActiveHardcoded (Ceiling: $BASELINE_CEILING). File breakdown: $fileCounts",
            totalActiveHardcoded <= BASELINE_CEILING
        )
    }

    @Test
    fun testHardcodedStringCounter_detectsLiteralAndIgnoresStringResource() {
        // Synthetic Kotlin Composable snippet:
        // Contains exactly 1 hardcoded UI string literal ("Halo Dunia")
        // and 1 localized stringResource (stringResource(R.string.btn_submit))
        val syntheticKotlinSnippet = """
            @Composable
            fun SampleComponent() {
                Text(text = "Halo Dunia")
                Button(onClick = {}) {
                    Text(text = stringResource(R.string.btn_submit))
                }
            }
        """.trimIndent()

        val detectedCount = countHardcodedUiStrings(syntheticKotlinSnippet)
        assertEquals(
            "Guard must detect exactly 1 hardcoded UI literal and ignore stringResource",
            1,
            detectedCount
        )
    }
}
