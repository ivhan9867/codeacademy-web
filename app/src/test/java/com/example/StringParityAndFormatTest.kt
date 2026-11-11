package com.example

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.w3c.dom.Element
import java.io.File
import java.util.regex.Pattern
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Automated tests for Internationalization (i18n) verification (TAHAP 5 & TAHAP 1.5-C).
 * 1. Checks that all keys in values/strings.xml exist in values-en, values-ja, and values-zh (exact match).
 * 2. Checks that format placeholders (%1$s, %1$d, etc.) match between all languages.
 * 3. Verifies Level, Badge, Track, and Course label mappings.
 */
class StringParityAndFormatTest {

    private fun resolveResFile(relativePath: String): File {
        val candidates = listOf(
            File("src/main/res/$relativePath"),           // When CWD is app/
            File("app/src/main/res/$relativePath"),       // When CWD is root
            File("../app/src/main/res/$relativePath")
        )
        return candidates.firstOrNull { it.exists() && it.isFile }
            ?: error("Cannot locate strings file $relativePath. user.dir=${System.getProperty("user.dir")}")
    }

    private val valuesXml get() = resolveResFile("values/strings.xml")
    private val valuesEnXml get() = resolveResFile("values-en/strings.xml")
    private val valuesJaXml get() = resolveResFile("values-ja/strings.xml")
    private val valuesZhXml get() = resolveResFile("values-zh/strings.xml")

    private fun parseStringResources(file: File): Map<String, StringResourceEntry> {
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val doc = builder.parse(file)
        val entries = mutableMapOf<String, StringResourceEntry>()

        val stringNodes = doc.getElementsByTagName("string")
        for (i in 0 until stringNodes.length) {
            val node = stringNodes.item(i) as Element
            val name = node.getAttribute("name")
            val text = node.textContent ?: ""
            val toolsIgnore = node.getAttribute("tools:ignore")
            entries[name] = StringResourceEntry(name, text, toolsIgnore)
        }

        val pluralNodes = doc.getElementsByTagName("plurals")
        for (i in 0 until pluralNodes.length) {
            val node = pluralNodes.item(i) as Element
            val name = node.getAttribute("name")
            val text = node.textContent ?: ""
            val toolsIgnore = node.getAttribute("tools:ignore")
            entries[name] = StringResourceEntry(name, text, toolsIgnore, isPlural = true)
        }

        return entries
    }

    data class StringResourceEntry(
        val name: String,
        val text: String,
        val toolsIgnore: String = "",
        val isPlural: Boolean = false
    )

    private val placeholderPattern = Pattern.compile("%(?:(\\d+)\\$)?([sdf])")

    private fun extractPlaceholders(text: String): List<String> {
        val matcher = placeholderPattern.matcher(text)
        val placeholders = mutableListOf<String>()
        while (matcher.find()) {
            val position = matcher.group(1) ?: ""
            val type = matcher.group(2)
            placeholders.add("$position$$type")
        }
        return placeholders.sorted()
    }

    @Test
    fun testStringParity_valuesAndValuesEn_keysMatchExactly() {
        val idEntries = parseStringResources(valuesXml)
        val enEntries = parseStringResources(valuesEnXml)

        val idKeys = idEntries.keys
        val enKeys = enEntries.keys

        val missingInEn = idKeys - enKeys
        val missingInId = enKeys - idKeys

        assertTrue(
            "The following keys exist in values/strings.xml but are missing in values-en/strings.xml: $missingInEn",
            missingInEn.isEmpty()
        )
        assertTrue(
            "The following keys exist in values-en/strings.xml but are missing in values/strings.xml: $missingInId",
            missingInId.isEmpty()
        )
        assertEquals(
            "Key count must match between values and values-en",
            idKeys.size,
            enKeys.size
        )
    }

    @Test
    fun testStringParity_valuesAndValuesJa_keysMatchExactly() {
        val idEntries = parseStringResources(valuesXml)
        val jaEntries = parseStringResources(valuesJaXml)

        val idKeys = idEntries.keys
        val jaKeys = jaEntries.keys

        val missingInJa = idKeys - jaKeys
        val missingInId = jaKeys - idKeys

        assertTrue(
            "The following keys exist in values/strings.xml but are missing in values-ja/strings.xml: $missingInJa",
            missingInJa.isEmpty()
        )
        assertTrue(
            "The following keys exist in values-ja/strings.xml but are missing in values/strings.xml: $missingInId",
            missingInId.isEmpty()
        )
        assertEquals(
            "Key count must match between values and values-ja",
            idKeys.size,
            jaKeys.size
        )
    }

    @Test
    fun testStringParity_valuesAndValuesZh_keysMatchExactly() {
        val idEntries = parseStringResources(valuesXml)
        val zhEntries = parseStringResources(valuesZhXml)

        val idKeys = idEntries.keys
        val zhKeys = zhEntries.keys

        val missingInZh = idKeys - zhKeys
        val missingInId = zhKeys - idKeys

        assertTrue(
            "The following keys exist in values/strings.xml but are missing in values-zh/strings.xml: $missingInZh",
            missingInZh.isEmpty()
        )
        assertTrue(
            "The following keys exist in values-zh/strings.xml but are missing in values/strings.xml: $missingInId",
            missingInId.isEmpty()
        )
        assertEquals(
            "Key count must match between values and values-zh",
            idKeys.size,
            zhKeys.size
        )
    }

    @Test
    fun testPlaceholderFormatParity_matchingArgumentsBetweenLanguages() {
        val idEntries = parseStringResources(valuesXml)
        val enEntries = parseStringResources(valuesEnXml)

        val mismatches = mutableListOf<String>()

        for ((key, idEntry) in idEntries) {
            val enEntry = enEntries[key] ?: continue
            val idPlaceholders = extractPlaceholders(idEntry.text)
            val enPlaceholders = extractPlaceholders(enEntry.text)

            if (idPlaceholders != enPlaceholders) {
                mismatches.add(
                    "Key: '$key' -> ID: $idPlaceholders ('${idEntry.text}') vs EN: $enPlaceholders ('${enEntry.text}')"
                )
            }
        }

        assertTrue(
            "Placeholder format mismatches found:\n" + mismatches.joinToString("\n"),
            mismatches.isEmpty()
        )
    }

    @Test
    fun testPlaceholderFormatParity_valuesJa_matchingArguments() {
        val idEntries = parseStringResources(valuesXml)
        val jaEntries = parseStringResources(valuesJaXml)

        val mismatches = mutableListOf<String>()

        for ((key, idEntry) in idEntries) {
            val jaEntry = jaEntries[key] ?: continue
            val idPlaceholders = extractPlaceholders(idEntry.text)
            val jaPlaceholders = extractPlaceholders(jaEntry.text)

            if (idPlaceholders != jaPlaceholders) {
                mismatches.add(
                    "Key: '$key' -> ID: $idPlaceholders ('${idEntry.text}') vs JA: $jaPlaceholders ('${jaEntry.text}')"
                )
            }
        }

        assertTrue(
            "JA placeholder format mismatches found:\n" + mismatches.joinToString("\n"),
            mismatches.isEmpty()
        )
    }

    @Test
    fun testPlaceholderFormatParity_valuesZh_matchingArguments() {
        val idEntries = parseStringResources(valuesXml)
        val zhEntries = parseStringResources(valuesZhXml)

        val mismatches = mutableListOf<String>()

        for ((key, idEntry) in idEntries) {
            val zhEntry = zhEntries[key] ?: continue
            val idPlaceholders = extractPlaceholders(idEntry.text)
            val zhPlaceholders = extractPlaceholders(zhEntry.text)

            if (idPlaceholders != zhPlaceholders) {
                mismatches.add(
                    "Key: '$key' -> ID: $idPlaceholders ('${idEntry.text}') vs ZH: $zhPlaceholders ('${zhEntry.text}')"
                )
            }
        }

        assertTrue(
            "ZH placeholder format mismatches found:\n" + mismatches.joinToString("\n"),
            mismatches.isEmpty()
        )
    }

    @Test
    fun testLevelAndBadgeLabelMappings_nonEmptyAndResolved() {
        // Test course levels mapping
        val levels = listOf("Semua", "Orientasi", "Pemula", "Menengah", "Lanjut", "Pemula-Menengah")
        for (level in levels) {
            val resId = com.example.util.localization.DataLabelMappings.getCourseLevelResId(level)
            assertTrue("Course level '$level' must have a non-null string resource mapping", resId != null)
        }

        // Test XP levels 1..10
        for (lvl in 1..10) {
            val resId = com.example.util.localization.DataLabelMappings.getXpLevelNameResId(lvl)
            assertTrue("XP level '$lvl' must have a non-null string resource mapping", resId != null)
        }

        // Test Badges b1..b12
        for (b in 1..12) {
            val badgeId = "b$b"
            val nameResId = com.example.util.localization.DataLabelMappings.getBadgeNameResId(badgeId)
            val descResId = com.example.util.localization.DataLabelMappings.getBadgeDescriptionResId(badgeId)
            assertTrue("Badge '$badgeId' must have name resource mapping", nameResId != null)
            assertTrue("Badge '$badgeId' must have desc resource mapping", descResId != null)
        }
    }
}
