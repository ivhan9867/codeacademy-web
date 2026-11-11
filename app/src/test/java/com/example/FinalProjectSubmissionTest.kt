package com.example

import com.example.data.CourseCatalog
import com.example.data.models.Course
import com.example.data.models.CourseModule
import com.example.data.models.ModuleSection
import com.example.data.models.ProjectSubmissionStatus
import com.example.data.models.SectionQuiz
import com.example.data.util.CourseUnlockUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FinalProjectSubmissionTest {

    private val sampleCourse = Course(
        id = "test-course-01",
        trackId = "front-end",
        title = "Test Course",
        description = "Test Description",
        level = "Pemula",
        durationMinutes = 60,
        modules = listOf(
            CourseModule(
                id = "m1",
                courseId = "test-course-01",
                title = "Module 1",
                order = 1,
                section = ModuleSection.INTI,
                contentMarkdown = "Content 1",
                codeExample = "",
                codeLanguage = "html"
            ),
            CourseModule(
                id = "m2",
                courseId = "test-course-01",
                title = "Module 2",
                order = 2,
                section = ModuleSection.INTI,
                contentMarkdown = "Content 2",
                codeExample = "",
                codeLanguage = "html"
            )
        ),
        sectionQuizzes = listOf(
            SectionQuiz(
                id = "sq1",
                courseId = "test-course-01",
                section = ModuleSection.INTI,
                title = "Quiz Inti"
            )
        )
    )

    @Test
    fun allDefaultCoursesHaveFinalProjectSpecAndFourRubrics() {
        val courses = CourseCatalog.defaultCourses
        assertEquals(7, courses.size)
        for (course in courses) {
            val spec = course.finalProject
            assertNotNull("Course ${course.id} must have a FinalProjectSpec", spec)
            assertTrue("Course ${course.id} finalProject title must not be blank", spec!!.title.isNotBlank())
            assertTrue("Course ${course.id} finalProject scenario must not be blank", spec.scenarioMarkdown.isNotBlank())
            assertEquals("Course ${course.id} must have 4 rubric items", 4, spec.rubrics.size)
            assertEquals(100, spec.rubrics.sumOf { it.weightPercent })
        }
    }

    @Test
    fun finalProjectUnlockRequiresModulesQuizzesAndFinalQuizPass() {
        // Not all modules completed -> locked
        assertFalse(
            CourseUnlockUtils.isFinalProjectUnlocked(
                course = sampleCourse,
                completedModuleIds = setOf("m1"),
                passedQuizIds = setOf("sq1"),
                finalQuizPassed = true
            )
        )

        // Modules & section quizzes completed, but final quiz not passed -> locked
        assertFalse(
            CourseUnlockUtils.isFinalProjectUnlocked(
                course = sampleCourse,
                completedModuleIds = setOf("m1", "m2"),
                passedQuizIds = setOf("sq1"),
                finalQuizPassed = false
            )
        )

        // All modules, section quizzes, and final quiz passed -> unlocked
        assertTrue(
            CourseUnlockUtils.isFinalProjectUnlocked(
                course = sampleCourse,
                completedModuleIds = setOf("m1", "m2"),
                passedQuizIds = setOf("sq1"),
                finalQuizPassed = true
            )
        )
    }

    @Test
    fun certificateUnlockRequiresApprovedSubmissionStatus() {
        for (status in ProjectSubmissionStatus.values()) {
            val unlocked = CourseUnlockUtils.isCertificateUnlocked(
                finalProjectUnlocked = true,
                submissionStatus = status
            )
            if (status == ProjectSubmissionStatus.APPROVED) {
                assertTrue("Certificate should be unlocked when status is APPROVED", unlocked)
            } else {
                assertFalse("Certificate should remain locked when status is $status", unlocked)
            }
        }
    }

    @Test
    fun computeFinalCertificateScoreCombinesQuizAndProjectWeights() {
        // 40% of 80 (32) + 60% of 90 (54) = 86
        assertEquals(86, CourseUnlockUtils.computeFinalCertificateScore(finalQuizScore = 80, facilitatorProjectScore = 90))
        // 40% of 100 (40) + 60% of 100 (60) = 100
        assertEquals(100, CourseUnlockUtils.computeFinalCertificateScore(finalQuizScore = 100, facilitatorProjectScore = 100))
    }
}
