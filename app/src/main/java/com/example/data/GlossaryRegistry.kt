package com.example.data

import com.example.data.models.CourseModule
import java.util.concurrent.ConcurrentHashMap

data class GlossaryItem(
    val term: String,
    val definition: String,
    val courseId: String
)

/**
 * Per-course registry for glossary terms and definitions.
 * Parses the "Glosarium Istilah" module of a course into a searchable lookup map.
 */
object GlossaryRegistry {
    private val courseGlossaryCache = ConcurrentHashMap<String, Map<String, GlossaryItem>>()
    private val courseRegexCache = ConcurrentHashMap<String, Regex>()
    private val GLOSSARY_LINE_REGEX = Regex("""^-\s*\*\*(.*?)\*\*\s*(?:[—–:]|-)\s*(.*)$""")

    /**
     * Retrieves glossary map (termKey.lowercase() -> GlossaryItem) for the given courseId.
     */
    fun getGlossaryForCourse(courseId: String?): Map<String, GlossaryItem> {
        if (courseId.isNullOrBlank()) return emptyMap()

        val cached = courseGlossaryCache[courseId]
        if (cached != null) return cached

        val course = CourseCatalog.courses.find { it.id == courseId }
        val glossaryModule = course?.modules?.find { module ->
            module.title.contains("Glosarium", ignoreCase = true) || module.id.contains("prep-7")
        }

        if (glossaryModule != null && glossaryModule.content.isNotBlank()) {
            val parsedMap = parseGlossaryContent(courseId, glossaryModule.content)
            courseGlossaryCache[courseId] = parsedMap
            return parsedMap
        }

        return emptyMap()
    }

    /**
     * Returns a compiled regex that matches any of the course's glossary terms as whole words,
     * or null if no terms exist.
     */
    fun getGlossaryRegexForCourse(courseId: String?): Regex? {
        if (courseId.isNullOrBlank()) return null
        val cached = courseRegexCache[courseId]
        if (cached != null) return cached

        val glossaryMap = getGlossaryForCourse(courseId)
        if (glossaryMap.isEmpty()) return null

        val terms = glossaryMap.keys.sortedByDescending { it.length }
        if (terms.isEmpty()) return null

        val pattern = "(?i)\\b(" + terms.joinToString("|") { Regex.escape(it) } + ")\\b"
        val regex = Regex(pattern)
        courseRegexCache[courseId] = regex
        return regex
    }

    /**
     * Parses the markdown content of a Glosarium Istilah module into a map of GlossaryItems.
     */
    fun parseGlossaryContent(courseId: String, content: String): Map<String, GlossaryItem> {
        val resultMap = mutableMapOf<String, GlossaryItem>()
        val lines = content.lines()

        for (rawLine in lines) {
            val line = rawLine.trim()
            val match = GLOSSARY_LINE_REGEX.find(line)
            if (match != null) {
                val fullTerm = match.groupValues[1].trim()
                val definition = match.groupValues[2].trim()

                if (fullTerm.isNotEmpty() && definition.isNotEmpty()) {
                    val item = GlossaryItem(
                        term = fullTerm,
                        definition = definition,
                        courseId = courseId
                    )
                    // Register main term
                    resultMap[fullTerm.lowercase()] = item

                    // Register aliases/variations:
                    // e.g. "DNS (Domain Name System)" -> "dns", "domain name system"
                    if (fullTerm.contains("(") && fullTerm.contains(")")) {
                        val beforeParen = fullTerm.substringBefore("(").trim()
                        val insideParen = fullTerm.substringAfter("(").substringBefore(")").trim()
                        if (beforeParen.isNotEmpty()) resultMap[beforeParen.lowercase()] = item
                        if (insideParen.isNotEmpty()) resultMap[insideParen.lowercase()] = item
                    }
                    // e.g. "WWW (World Wide Web)" -> "www", "world wide web"
                    if (fullTerm.contains("/") ) {
                        val parts = fullTerm.split("/")
                        for (part in parts) {
                            val p = part.trim()
                            if (p.isNotEmpty()) resultMap[p.lowercase()] = item
                        }
                    }
                    // e.g. "DOM Tree" -> also match "dom"
                    if (fullTerm.equals("DOM Tree", ignoreCase = true)) {
                        resultMap["dom"] = item
                    }
                }
            }
        }
        return resultMap
    }

    /**
     * Clears cache (useful for testing or dynamic module reloads).
     */
    fun clearCache() {
        courseGlossaryCache.clear()
        courseRegexCache.clear()
    }
}
