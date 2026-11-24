package com.example

import com.example.data.CourseCatalogParser
import com.example.data.models.Course
import com.example.data.models.ModuleSection
import com.example.data.util.CourseSectionUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CourseSectionUtilsTest {

    @Test
    fun testFrontEndSectionTitles() {
        assertEquals("Pengenalan HTML", CourseSectionUtils.sectionTitle("fe-web-foundation-00", ModuleSection.INTI))
        assertEquals("Pendalaman HTML", CourseSectionUtils.sectionTitle("fe-web-foundation-00", ModuleSection.LANJUTAN))
        assertEquals("Dasar CSS", CourseSectionUtils.sectionTitle("fe-web-foundation-00", ModuleSection.CSS_DASAR))
        assertEquals("Pendalaman CSS", CourseSectionUtils.sectionTitle("fe-web-foundation-00", ModuleSection.PENDALAMAN_CSS))
        assertEquals("Pendahuluan", CourseSectionUtils.sectionTitle("fe-web-foundation-00", ModuleSection.PENDAHULUAN))
        assertEquals("Persiapan", CourseSectionUtils.sectionTitle("fe-web-foundation-00", ModuleSection.PERSIAPAN))
    }

    @Test
    fun testPythonSectionTitles() {
        assertEquals("Persiapan", CourseSectionUtils.sectionTitle("py-fund-01", ModuleSection.PERSIAPAN))
        assertEquals("Dasar-Dasar Python", CourseSectionUtils.sectionTitle("py-fund-01", ModuleSection.PENDAHULUAN))
        assertEquals("Alur Program & Perulangan", CourseSectionUtils.sectionTitle("py-fund-01", ModuleSection.INTI))
        assertEquals("Struktur Data & Fungsi", CourseSectionUtils.sectionTitle("py-fund-01", ModuleSection.LANJUTAN))
        assertEquals("Proyek & Evaluasi", CourseSectionUtils.sectionTitle("py-fund-01", ModuleSection.EVALUASI))
    }

    @Test
    fun testDefaultGenericSectionTitles() {
        assertEquals("Materi Inti", CourseSectionUtils.sectionTitle("generic-course", ModuleSection.INTI))
        assertEquals("Materi Lanjutan", CourseSectionUtils.sectionTitle("generic-course", ModuleSection.LANJUTAN))
        assertEquals("Persiapan", CourseSectionUtils.sectionTitle("generic-course", ModuleSection.PERSIAPAN))
        assertEquals("Pendahuluan", CourseSectionUtils.sectionTitle("generic-course", ModuleSection.PENDAHULUAN))
        assertEquals("Dasar CSS", CourseSectionUtils.sectionTitle("generic-course", ModuleSection.CSS_DASAR))
        assertEquals("Pendalaman CSS", CourseSectionUtils.sectionTitle("generic-course", ModuleSection.PENDALAMAN_CSS))
        assertEquals("Evaluasi", CourseSectionUtils.sectionTitle("generic-course", ModuleSection.EVALUASI))
    }

    @Test
    fun testCourseObjectWithCustomSectionTitles() {
        val customCourse = Course(
            id = "custom-course-123",
            trackId = "general",
            title = "Custom Course",
            level = "Pemula",
            durationMinutes = 60,
            rating = 5.0f,
            studentsCount = 100,
            description = "Test description",
            prerequisites = emptyList(),
            instructor = "Test Instructor",
            modules = emptyList(),
            quizQuestions = emptyList(),
            sectionTitles = mapOf(
                "INTI" to "Fondasi Inti Bahasa",
                "LANJUTAN" to "Eksplorasi Algoritma"
            )
        )

        assertEquals("Fondasi Inti Bahasa", CourseSectionUtils.sectionTitle(customCourse, ModuleSection.INTI))
        assertEquals("Eksplorasi Algoritma", CourseSectionUtils.sectionTitle(customCourse, ModuleSection.LANJUTAN))
        // Fallback to default
        assertEquals("Persiapan", CourseSectionUtils.sectionTitle(customCourse, ModuleSection.PERSIAPAN))
    }

    @Test
    fun testSectionSubtitles() {
        val feCourse = Course(
            id = "fe-web-foundation-00",
            trackId = "front-end",
            title = "Web Foundation",
            level = "Pemula",
            durationMinutes = 100,
            rating = 5.0f,
            studentsCount = 1000,
            description = "Test",
            prerequisites = emptyList(),
            instructor = "Budi",
            modules = emptyList(),
            quizQuestions = emptyList()
        )
        val pyCourse = Course(
            id = "py-fund-01",
            trackId = "python",
            title = "Python Fund",
            level = "Pemula",
            durationMinutes = 100,
            rating = 5.0f,
            studentsCount = 1000,
            description = "Test",
            prerequisites = emptyList(),
            instructor = "Dewi",
            modules = emptyList(),
            quizQuestions = emptyList()
        )

        assertEquals("Variasi Styling & Aturan Dasar CSS", CourseSectionUtils.sectionSubtitle(feCourse, ModuleSection.CSS_DASAR))
        assertFalse(CourseSectionUtils.sectionSubtitle(pyCourse, ModuleSection.CSS_DASAR).contains("Aturan Dasar CSS"))
    }

    @Test
    fun testCatalogJsonContainsFrontEndCourses() {
        val dir = CourseCatalogParser.findLocalCatalogDirectory()
        assertNotNull("Local catalog directory must exist", dir)

        val catalogData = CourseCatalogParser.loadCatalogFromDirectory(dir!!)
        val feCourse = catalogData.courses.find { it.id == "fe-web-foundation-00" }
        assertNotNull("fe-web-foundation-00 must be parsed", feCourse)
        assertTrue("Catalog must contain at least 3 Front-End courses", catalogData.courses.size >= 3)
        assertNotNull("fe-css-02 must be parsed", catalogData.courses.find { it.id == "fe-css-02" })
        assertNotNull("fe-js-03 must be parsed", catalogData.courses.find { it.id == "fe-js-03" })
    }
}
