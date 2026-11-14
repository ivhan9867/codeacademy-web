package com.example

import com.example.data.CourseCatalog
import com.example.data.CourseCatalogParser
import com.example.data.util.CourseUnlockUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class CourseCatalogParserTest {

    @Test
    fun testCatalogParsingAndIntegrity() {
        val catalogDir = CourseCatalogParser.findLocalCatalogDirectory()
        assertNotNull("Local catalog directory must exist in assets/catalog", catalogDir)

        val catalogData = CourseCatalogParser.loadCatalogFromDirectory(catalogDir!!)
        assertEquals("Should parse 1 learning track (Front-End WebDev)", 1, catalogData.tracks.size)

        // Verify tracks totalCourses dynamic calculation
        val frontEndTrack = catalogData.tracks.find { it.id == "front-end" }
        assertNotNull("front-end track must exist", frontEndTrack)
        assertEquals("front-end track totalCourses must match actual courses count (3)", 3, frontEndTrack?.totalCourses)

        // Total courses across all tracks
        assertEquals("Total courses parsed must be 3", 3, catalogData.courses.size)

        // Verify fe-web-foundation-00 course integrity
        val feWebCourse = catalogData.courses.find { it.id == "fe-web-foundation-00" }
        assertNotNull("fe-web-foundation-00 must exist", feWebCourse)
        assertEquals("Fondasi Web: Dari Nol ke Baris Kode Pertama", feWebCourse?.title)
        assertEquals("Orientasi", feWebCourse?.level)
        assertEquals(20, feWebCourse?.quizQuestions?.size)
        assertEquals(5, feWebCourse?.sectionQuizzes?.size)
        assertTrue("cheatSheetMarkdown must not be empty", !feWebCourse?.cheatSheetMarkdown.isNullOrBlank())

        // Verify content path reference
        val contentPath = catalogData.contentPaths["fe-web-foundation-00"]
        assertEquals("content/fe-web-foundation-00", contentPath)

        // Verify fe-html-01 is removed
        val htmlCourse = catalogData.courses.find { it.id == "fe-html-01" }
        org.junit.Assert.assertNull("fe-html-01 must not exist in catalog", htmlCourse)

        val cssCourse = catalogData.courses.find { it.id == "fe-css-02" }
        assertNotNull("fe-css-02 must exist", cssCourse)
        assertEquals("CSS Layout Modern: Flexbox & Grid", cssCourse?.title)
        assertEquals("Pemula-Menengah", cssCourse?.level)
        assertEquals(0, cssCourse?.modules?.size)
        assertEquals(16, cssCourse?.quizQuestions?.size)
        assertTrue("fe-css-02 sectionQuizzes must not be empty", !cssCourse?.sectionQuizzes.isNullOrEmpty())
        assertTrue("fe-css-02 cheatSheetMarkdown must not be empty", !cssCourse?.cheatSheetMarkdown.isNullOrBlank())
        assertEquals("content/fe-css-02", catalogData.contentPaths["fe-css-02"])

        val jsCourse = catalogData.courses.find { it.id == "fe-js-03" }
        assertNotNull("fe-js-03 must exist", jsCourse)
        assertEquals(0, jsCourse?.modules?.size)
        assertEquals("content/fe-js-03", catalogData.contentPaths["fe-js-03"])
        assertEquals(20, jsCourse?.quizQuestions?.size)
        assertTrue("fe-js-03 sectionQuizzes must not be empty", !jsCourse?.sectionQuizzes.isNullOrEmpty())
        assertTrue("fe-js-03 cheatSheetMarkdown must not be empty", !jsCourse?.cheatSheetMarkdown.isNullOrBlank())
    }

    @Test
    fun testCourseCatalogSingletonBehavior() {
        val tracks = CourseCatalog.tracks
        assertEquals(1, tracks.size)

        val courses = CourseCatalog.courses
        assertEquals(3, courses.size)

        val feWeb = courses.find { it.id == "fe-web-foundation-00" }
        assertNotNull(feWeb)
        // Ensure dynamic modules loaded from assets/content/fe-web-foundation-00 (49 HTML + 7 CSS Dasar + 50 Pendalaman CSS)
        assertEquals(106, feWeb?.modules?.size)

        // Test CourseUnlockUtils logic remains 100% functional
        val firstModule = feWeb!!.modules.first()
        val isFirstUnlocked = CourseUnlockUtils.isModuleUnlocked(
            course = feWeb,
            moduleId = firstModule.id,
            completedModuleIds = emptySet()
        )
        assertTrue("First module must always be unlocked", isFirstUnlocked)
    }
}
