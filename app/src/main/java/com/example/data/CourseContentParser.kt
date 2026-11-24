package com.example.data

import android.content.Context
import android.util.Log
import com.example.data.models.CourseModule
import com.example.data.models.ModuleSection
import java.io.File

/**
 * Parser for CourseModule markdown files with YAML front-matter.
 * Reads metadata from the front-matter block (enclosed in '---')
 * and the markdown body as the module content.
 */
object CourseContentParser {
    private const val TAG = "CourseContentParser"
    private val CLOSING_DELIMITER_REGEX = Regex("""(?m)^---\s*$""")

    private fun logWarning(msg: String, throwable: Throwable? = null) {
        try {
            if (throwable != null) {
                Log.w(TAG, msg, throwable)
            } else {
                Log.w(TAG, msg)
            }
        } catch (_: Throwable) {
            System.err.println("[$TAG] WARNING: $msg")
            throwable?.printStackTrace(System.err)
        }
    }

    private fun logDebug(msg: String) {
        try {
            Log.d(TAG, msg)
        } catch (_: Throwable) {
            println("[$TAG] DEBUG: $msg")
        }
    }

    /**
     * Parses a single markdown file content with YAML front-matter into a CourseModule.
     * Returns null and logs a warning if front-matter is missing, corrupt, or invalid.
     */
    fun parseModule(path: String, fileContent: String): CourseModule? {
        val trimmed = fileContent.trimStart()
        if (!trimmed.startsWith("---")) {
            logWarning("Skipping $path: file does not start with front-matter delimiter '---'")
            return null
        }

        // Find line break after opening '---'
        val firstDelimiterEnd = trimmed.indexOf('\n')
        if (firstDelimiterEnd == -1) {
            logWarning("Skipping $path: incomplete front-matter header")
            return null
        }

        val remaining = trimmed.substring(firstDelimiterEnd + 1)
        val closingMatch = CLOSING_DELIMITER_REGEX.find(remaining)
        if (closingMatch == null) {
            logWarning("Skipping $path: closing front-matter delimiter '---' not found")
            return null
        }

        val frontMatterText = remaining.substring(0, closingMatch.range.first)
        val afterClosing = remaining.substring(closingMatch.range.last)
        val newlineAfterDelim = afterClosing.indexOf('\n')
        val bodyContent = if (newlineAfterDelim != -1) {
            afterClosing.substring(newlineAfterDelim + 1)
        } else {
            ""
        }.trimStart('\r', '\n')

        return try {
            buildCourseModule(path, frontMatterText, bodyContent)
        } catch (e: Exception) {
            logWarning("Skipping $path: error parsing front-matter: ${e.message}", e)
            null
        }
    }

    /**
     * Parses all files provided in the map (e.g. from ContentAssetLoader),
     * optionally filtered by courseId, and returns a list sorted by module order ascending.
     */
    fun parseAll(files: Map<String, String>, targetCourseId: String? = null): List<CourseModule> {
        val modules = mutableListOf<CourseModule>()
        for ((path, content) in files) {
            val mod = parseModule(path, content)
            if (mod != null) {
                if (targetCourseId == null || mod.courseId == targetCourseId) {
                    modules.add(mod)
                }
            }
        }
        return modules.sortedBy { it.order }
    }

    /**
     * Suspended loader that reads all markdown assets via ContentAssetLoader,
     * parses them, and returns modules for the given courseId sorted by order.
     */
    suspend fun loadCourseModules(context: Context, courseId: String): List<CourseModule> {
        val allAssets = ContentAssetLoader.loadAll(context)
        val parsed = parseAll(allAssets, courseId)
        logDebug("Loaded ${parsed.size} modules from assets for course '$courseId'")
        return parsed
    }

    /**
     * Utility for JVM Unit Tests or local file loading where Android Context is not available.
     * Recursively traverses directory to read all .md files and parse them.
     */
    fun loadCourseModulesFromDirectory(dir: File, courseId: String? = null): List<CourseModule> {
        if (!dir.exists() || !dir.isDirectory) {
            logWarning("Directory ${dir.path} does not exist or is not a directory")
            return emptyList()
        }
        val fileMap = mutableMapOf<String, String>()
        dir.walkTopDown()
            .filter { it.isFile && it.extension.equals("md", ignoreCase = true) }
            .forEach { file ->
                try {
                    fileMap[file.path] = file.readText(Charsets.UTF_8)
                } catch (e: Exception) {
                    logWarning("Failed to read file ${file.path}: ${e.message}")
                }
            }
        return parseAll(fileMap, courseId)
    }

    private fun buildCourseModule(path: String, yamlText: String, bodyContent: String): CourseModule? {
        val lines = yamlText.lines()

        var id: String? = null
        var courseId: String? = null
        var order: Int? = null
        var title: String? = null
        var readingMinutes = 5
        var section = ModuleSection.INTI
        var codeSnippet: String? = null
        var codeLanguage: String? = null
        val codeSnippets = mutableMapOf<String, String>()
        var starterHtml = ""
        var starterCss = ""
        var starterJs = ""
        var playgroundTask: String? = null
        val verificationRules = mutableListOf<String>()
        var isProjectContinuation = false
        val learningObjectives = mutableListOf<String>()

        var currentKey: String? = null
        var currentBlockScalar: StringBuilder? = null
        var blockScalarIndent = -1
        var inMap = false
        var currentMapSubKey: String? = null
        var currentMapBlockScalar: StringBuilder? = null

        fun finishBlockScalar() {
            if (currentKey != null && currentBlockScalar != null) {
                val value = currentBlockScalar.toString().trimEnd('\r', '\n')
                when (currentKey) {
                    "codeSnippet", "starterCode" -> codeSnippet = value
                    "playgroundTask" -> playgroundTask = value
                    "starterHtml" -> starterHtml = value
                    "starterCss" -> starterCss = value
                    "starterJs" -> starterJs = value
                }
                currentBlockScalar = null
            }
            val mapSubKey = currentMapSubKey
            val mapBlock = currentMapBlockScalar
            if (currentKey == "codeSnippets" && mapSubKey != null && mapBlock != null) {
                codeSnippets[mapSubKey] = mapBlock.toString().trimEnd('\r', '\n')
                currentMapBlockScalar = null
                currentMapSubKey = null
            }
        }

        for (rawLine in lines) {
            val line = rawLine.trimEnd()
            val indent = rawLine.takeWhile { it == ' ' }.length

            // If we are collecting a multiline block scalar (e.g. codeSnippet: |)
            val blockScalar = currentBlockScalar
            if (blockScalar != null) {
                if (line.isBlank()) {
                    blockScalar.append("\n")
                    continue
                }
                if (blockScalarIndent == -1) {
                    blockScalarIndent = indent
                }
                if (indent >= blockScalarIndent) {
                    val contentLine = if (rawLine.length >= blockScalarIndent) rawLine.substring(blockScalarIndent) else line
                    blockScalar.append(contentLine).append("\n")
                    continue
                } else {
                    finishBlockScalar()
                    blockScalarIndent = -1
                }
            }

            // If collecting a map block scalar
            val mapScalar = currentMapBlockScalar
            if (mapScalar != null) {
                if (line.isBlank()) {
                    mapScalar.append("\n")
                    continue
                }
                if (blockScalarIndent == -1) {
                    blockScalarIndent = indent
                }
                if (indent >= blockScalarIndent) {
                    val contentLine = if (rawLine.length >= blockScalarIndent) rawLine.substring(blockScalarIndent) else line
                    mapScalar.append(contentLine).append("\n")
                    continue
                } else {
                    finishBlockScalar()
                    blockScalarIndent = -1
                }
            }

            if (line.isBlank() || line.trimStart().startsWith("#")) {
                continue
            }

            val trimmedLine = line.trimStart()

            // List item: "- value"
            if (trimmedLine.startsWith("- ")) {
                val rawItem = trimmedLine.removePrefix("- ").trim()
                val itemValue = unquote(rawItem)
                when (currentKey) {
                    "verificationRules" -> verificationRules.add(itemValue)
                    "learningObjectives" -> learningObjectives.add(itemValue)
                }
                continue
            }

            // Map entry: "  key: value"
            if (inMap && indent > 0 && trimmedLine.contains(":")) {
                val colonIdx = trimmedLine.indexOf(':')
                val subKey = trimmedLine.substring(0, colonIdx).trim()
                val subVal = trimmedLine.substring(colonIdx + 1).trim()
                if (subVal == "|" || subVal == "|-" || subVal == ">") {
                    currentMapSubKey = subKey
                    currentMapBlockScalar = StringBuilder()
                    blockScalarIndent = -1
                } else if (subVal.isNotEmpty()) {
                    codeSnippets[subKey] = unquote(subVal)
                }
                continue
            }

            // Top-level key: value
            val colonIdx = line.indexOf(':')
            if (colonIdx != -1) {
                finishBlockScalar()
                inMap = false
                val key = line.substring(0, colonIdx).trim()
                val valuePart = line.substring(colonIdx + 1).trim()
                currentKey = key

                if (valuePart == "|" || valuePart == "|-" || valuePart == ">") {
                    currentBlockScalar = StringBuilder()
                    blockScalarIndent = -1
                    continue
                }

                if (key == "codeSnippets") {
                    inMap = true
                    continue
                }

                if (valuePart.startsWith("[") && valuePart.endsWith("]")) {
                    val items = parseInlineList(valuePart)
                    when (key) {
                        "verificationRules" -> verificationRules.addAll(items)
                        "learningObjectives" -> learningObjectives.addAll(items)
                    }
                    continue
                }

                val unquotedVal = unquote(valuePart)
                when (key) {
                    "id" -> id = unquotedVal
                    "courseId" -> courseId = unquotedVal
                    "order" -> order = unquotedVal.toIntOrNull()
                    "title" -> title = unquotedVal
                    "readingMinutes" -> readingMinutes = unquotedVal.toIntOrNull() ?: 5
                    "section" -> {
                        section = try {
                            ModuleSection.valueOf(unquotedVal.uppercase())
                        } catch (_: Exception) {
                            ModuleSection.INTI
                        }
                    }
                    "codeLanguage" -> codeLanguage = unquotedVal.ifEmpty { null }
                    "codeSnippet", "starterCode" -> codeSnippet = unquotedVal.ifEmpty { null }
                    "starterHtml" -> starterHtml = unquotedVal
                    "starterCss" -> starterCss = unquotedVal
                    "starterJs" -> starterJs = unquotedVal
                    "playgroundTask" -> playgroundTask = unquotedVal.ifEmpty { null }
                    "isProjectContinuation" -> isProjectContinuation = unquotedVal.equals("true", ignoreCase = true)
                }
            }
        }
        finishBlockScalar()

        // Mandatory validation
        if (id.isNullOrBlank() || courseId.isNullOrBlank() || title.isNullOrBlank() || order == null) {
            logWarning("Skipping $path: missing mandatory field (id=$id, courseId=$courseId, title=$title, order=$order)")
            return null
        }

        return CourseModule(
            id = id,
            courseId = courseId,
            order = order,
            title = title,
            readingMinutes = readingMinutes,
            content = bodyContent,
            codeSnippet = codeSnippet,
            codeLanguage = codeLanguage,
            codeSnippets = if (codeSnippets.isNotEmpty()) codeSnippets else null,
            starterHtml = starterHtml,
            starterCss = starterCss,
            starterJs = starterJs,
            playgroundTask = playgroundTask,
            section = section,
            verificationRules = verificationRules,
            isProjectContinuation = isProjectContinuation,
            learningObjectives = learningObjectives
        )
    }

    private fun unquote(str: String): String {
        val s = str.trim()
        if ((s.startsWith("\"") && s.endsWith("\"")) || (s.startsWith("'") && s.endsWith("'"))) {
            if (s.length >= 2) {
                return s.substring(1, s.length - 1)
                    .replace("\\\"", "\"")
                    .replace("\\'", "'")
                    .replace("\\\\", "\\")
                    .replace("\\n", "\n")
                    .replace("\\t", "\t")
            }
        }
        return s
    }

    private fun parseInlineList(str: String): List<String> {
        val inner = str.trim().removePrefix("[").removeSuffix("]").trim()
        if (inner.isEmpty()) return emptyList()
        val items = mutableListOf<String>()
        val sb = StringBuilder()
        var inQuotes = false
        var quoteChar = ' '
        for (c in inner) {
            if (!inQuotes && (c == '"' || c == '\'')) {
                inQuotes = true
                quoteChar = c
                sb.append(c)
            } else if (inQuotes && c == quoteChar) {
                inQuotes = false
                sb.append(c)
            } else if (!inQuotes && c == ',') {
                items.add(unquote(sb.toString().trim()))
                sb.clear()
            } else {
                sb.append(c)
            }
        }
        if (sb.isNotEmpty()) {
            items.add(unquote(sb.toString().trim()))
        }
        return items
    }
}
