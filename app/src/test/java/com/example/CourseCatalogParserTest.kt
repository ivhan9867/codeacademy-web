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
        assertEquals("Should parse 5 learning tracks", 5, catalogData.tracks.size)

        // Verify tracks totalCourses dynamic calculation
        val frontEndTrack = catalogData.tracks.find { it.id == "front-end" }
        assertNotNull("front-end track must exist", frontEndTrack)
        assertEquals("front-end track totalCourses must match actual courses count (3)", 3, frontEndTrack?.totalCourses)

        val backEndTrack = catalogData.tracks.find { it.id == "back-end" }
        assertNotNull("back-end track must exist", backEndTrack)
        assertEquals("back-end track totalCourses must match actual courses count (1)", 1, backEndTrack?.totalCourses)

        val pythonTrack = catalogData.tracks.find { it.id == "python" }
        assertNotNull("python track must exist", pythonTrack)
        assertEquals("python track totalCourses must match actual courses count (1)", 1, pythonTrack?.totalCourses)

        val cTrack = catalogData.tracks.find { it.id == "c-lang" }
        assertNotNull("c-lang track must exist", cTrack)
        assertEquals("c-lang track totalCourses must match actual courses count (1)", 1, cTrack?.totalCourses)

        val cppTrack = catalogData.tracks.find { it.id == "cpp-lang" }
        assertNotNull("cpp-lang track must exist", cppTrack)
        assertEquals("cpp-lang track totalCourses must match actual courses count (1)", 1, cppTrack?.totalCourses)

        val uiUxTrack = catalogData.tracks.find { it.id == "ui-ux" }
        org.junit.Assert.assertNull("ui-ux track must not exist", uiUxTrack)

        // Total courses across all tracks
        assertEquals("Total courses parsed must be 7", 7, catalogData.courses.size)

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
        assertEquals("content/fe-css-02", catalogData.contentPaths["fe-css-02"])

        val jsCourse = catalogData.courses.find { it.id == "fe-js-03" }
        assertNotNull("fe-js-03 must exist", jsCourse)
        assertEquals(0, jsCourse?.modules?.size)
        assertEquals("content/fe-js-03", catalogData.contentPaths["fe-js-03"])
        assertEquals(20, jsCourse?.quizQuestions?.size)

        val nodeCourse = catalogData.courses.find { it.id == "be-node-01" }
        assertNotNull("be-node-01 must exist", nodeCourse)
        assertEquals(0, nodeCourse?.modules?.size)

        val pyCourse = catalogData.courses.find { it.id == "py-fund-01" }
        assertNotNull("py-fund-01 must exist", pyCourse)
        assertEquals(0, pyCourse?.modules?.size)
        assertEquals("content/py-fund-01", catalogData.contentPaths["py-fund-01"])

        val cCourse = catalogData.courses.find { it.id == "c-fund-01" }
        assertNotNull("c-fund-01 must exist", cCourse)
        assertEquals(0, cCourse?.modules?.size)

        val cppCourse = catalogData.courses.find { it.id == "cpp-fund-01" }
        assertNotNull("cpp-fund-01 must exist", cppCourse)
        assertEquals(0, cppCourse?.modules?.size)
        assertEquals("content/cpp-fund-01", catalogData.contentPaths["cpp-fund-01"])
        assertEquals(21, cppCourse?.quizQuestions?.size)
    }

    @Test
    fun testCourseCatalogSingletonBehavior() {
        val tracks = CourseCatalog.tracks
        assertEquals(5, tracks.size)

        val courses = CourseCatalog.courses
        assertEquals(7, courses.size)

        val feWeb = courses.find { it.id == "fe-web-foundation-00" }
        assertNotNull(feWeb)
        // Ensure dynamic modules loaded from assets/content/fe-web-foundation-00 (49 HTML + 7 CSS Dasar + 43 Pendalaman CSS)
        assertEquals(106, feWeb?.modules?.size)

        val pyFund = courses.find { it.id == "py-fund-01" }
        assertNotNull(pyFund)
        assertEquals(25, pyFund?.modules?.size)

        val cppFund = courses.find { it.id == "cpp-fund-01" }
        assertNotNull(cppFund)
        assertEquals(28, cppFund?.modules?.size)

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
