package com.example

import com.example.data.CourseCatalog
import com.example.data.models.Course
import com.example.data.models.CourseModule
import com.example.data.models.ModuleSection
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ModuleSearchTest {

    private fun createDummyCourses(): List<Course> {
        return listOf(
            Course(
                id = "course-html",
                trackId = "front-end",
                title = "Dasar HTML",
                level = "Pemula",
                durationMinutes = 60,
                rating = 4.8f,
                studentsCount = 1000,
                description = "Belajar dasar HTML",
                prerequisites = emptyList(),
                instructor = "Budi",
                modules = listOf(
                    CourseModule(
                        id = "mod-1",
                        courseId = "course-html",
                        order = 1,
                        title = "Pengenalan HTML dan Struktur Dokumen",
                        readingMinutes = 5,
                        content = "HTML adalah bahasa markup standar. Dokumen HTML diawali dengan deklarasi doctype html yang memberi tahu browser.",
                        section = ModuleSection.PENDAHULUAN
                    ),
                    CourseModule(
                        id = "mod-2",
                        courseId = "course-html",
                        order = 2,
                        title = "Elemen dan Atribut Dasar",
                        readingMinutes = 8,
                        content = "Elemen HTML terdiri dari tag pembuka, isi, dan tag penutup. Atribut memberikan informasi tambahan seperti href dan src.",
                        section = ModuleSection.INTI
                    )
                ),
                quizQuestions = emptyList()
            ),
            Course(
                id = "course-css",
                trackId = "front-end",
                title = "Dasar CSS Modern",
                level = "Pemula",
                durationMinutes = 90,
                rating = 4.9f,
                studentsCount = 800,
                description = "Belajar styling CSS",
                prerequisites = emptyList(),
                instructor = "Siti",
                modules = listOf(
                    CourseModule(
                        id = "mod-3",
                        courseId = "course-css",
                        order = 1,
                        title = "Sintaks dan Selektor CSS",
                        readingMinutes = 6,
                        content = "Selektor digunakan untuk memilih elemen HTML yang ingin diberi gaya warna, font, dan margin.",
                        section = ModuleSection.PENDAHULUAN
                    )
                ),
                quizQuestions = emptyList()
            )
        )
    }

    private fun search(courses: List<Course>, query: String) =
        courses.flatMap { course ->
            course.modules.filter { mod ->
                val clean = query.trim()
                clean.isNotEmpty() && (mod.title.contains(clean, ignoreCase = true) || mod.content.contains(clean, ignoreCase = true))
            }.map { mod ->
                val clean = query.trim()
                val titleMatch = mod.title.contains(clean, ignoreCase = true)
                val contentIdx = mod.content.indexOf(clean, ignoreCase = true)
                val snippet = if (contentIdx != -1) {
                    val start = (contentIdx - 30).coerceAtLeast(0)
                    val end = (contentIdx + clean.length + 30).coerceAtMost(mod.content.length)
                    val raw = mod.content.substring(start, end).replace("\n", " ").trim()
                    val prefix = if (start > 0) "... " else ""
                    val suffix = if (end < mod.content.length) " ..." else ""
                    "$prefix$raw$suffix"
                } else null

                Triple(course.title, mod, snippet)
            }
        }

    @Test
    fun testSearchByTitleMatch() {
        val courses = createDummyCourses()
        val results = search(courses, "Struktur Dokumen")

        assertEquals(1, results.size)
        assertEquals("mod-1", results[0].second.id)
        assertEquals("Dasar HTML", results[0].first)
    }

    @Test
    fun testSearchByContentMatchWithSnippet() {
        val courses = createDummyCourses()
        val results = search(courses, "doctype html")

        assertEquals(1, results.size)
        assertEquals("mod-1", results[0].second.id)
        val snippet = results[0].third
        assertNotNull(snippet)
        assertTrue("Snippet should contain doctype html", snippet!!.contains("doctype html", ignoreCase = true))
    }

    @Test
    fun testSearchCaseInsensitiveAndTrim() {
        val courses = createDummyCourses()
        val results = search(courses, "   sElEkToR   ")

        assertEquals(1, results.size)
        assertEquals("mod-3", results[0].second.id)
        assertEquals("Dasar CSS Modern", results[0].first)
    }

    @Test
    fun testSearchEmptyQueryReturnsEmpty() {
        val courses = createDummyCourses()
        val results = search(courses, "    ")

        assertTrue(results.isEmpty())
    }

    @Test
    fun testSearchMultipleMatchesAcrossCourses() {
        val courses = createDummyCourses()
        // "HTML" is in mod-1 (title+content), mod-2 (title+content), and mod-3 (content)
        val results = search(courses, "HTML")

        assertEquals(3, results.size)
    }
}
