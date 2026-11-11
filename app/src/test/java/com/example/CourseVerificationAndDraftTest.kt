package com.example

import com.example.data.models.CourseModule
import com.example.data.models.ModuleSection
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CourseVerificationAndDraftTest {

    @Test
    fun testModuleVerificationRulesDefaultEmpty() {
        val module = CourseModule(
            id = "test-mod-1",
            courseId = "fe-web-00",
            order = 1,
            title = "Test Module",
            readingMinutes = 3,
            content = "Content"
        )
        assertTrue(module.verificationRules.isEmpty())
        assertFalse(module.isProjectContinuation)
    }

    @Test
    fun testModuleWithVerificationRulesMatchesStudentCode() {
        val rules = listOf("<h1>", "<h2>", "<p>")
        val module = CourseModule(
            id = "web-foundation-inti-4",
            courseId = "fe-web-foundation-00",
            order = 19,
            title = "Latihan: Menandai Konten dengan Elemen",
            readingMinutes = 7,
            content = "...",
            verificationRules = rules,
            isProjectContinuation = true
        )

        assertEquals(3, module.verificationRules.size)
        assertTrue(module.isProjectContinuation)

        // Incomplete code: only has h1
        val incompleteCode = "<h1>Profil Rani</h1>"
        val passedIncomplete = rules.all { rule ->
            incompleteCode.contains(rule, ignoreCase = true)
        }
        assertFalse("Incomplete code should not pass all verification rules", passedIncomplete)

        // Complete student code
        val completeCode = """
            <!DOCTYPE html>
            <html>
              <body>
                <h1>Profil Rani</h1>
                <h2>Tentang Saya</h2>
                <p>Belajar HTML</p>
              </body>
            </html>
        """.trimIndent()
        val passedComplete = rules.all { rule ->
            completeCode.contains(rule, ignoreCase = true)
        }
        assertTrue("Complete student code should satisfy all verification rules", passedComplete)
    }

    @Test
    fun testCaseInsensitiveRuleMatching() {
        val rules = listOf("<!doctype html>", "<title>")
        val studentCode = "<!DOCTYPE html><html><head><TITLE>Profil</TITLE></head></html>"

        val allMatched = rules.all { rule ->
            studentCode.contains(rule, ignoreCase = true)
        }
        assertTrue("Verification rule matching should be case-insensitive for HTML tags", allMatched)
    }

    @Test
    fun testLearningObjectivesAndRemediationDataIntegrity() {
        val course = com.example.data.CourseCatalog.courses.find { it.id == "fe-web-foundation-00" }
        org.junit.Assert.assertNotNull("Course fe-web-foundation-00 should exist", course)
        org.junit.Assert.assertTrue("Course should have non-empty cheatSheetMarkdown", !course?.cheatSheetMarkdown.isNullOrBlank())

        val inti1 = course?.modules?.find { it.id == "web-foundation-inti-1" }
        org.junit.Assert.assertNotNull(inti1)
        org.junit.Assert.assertTrue("web-foundation-inti-1 should have learningObjectives", inti1?.learningObjectives?.isNotEmpty() == true)

        val pendQuiz = course?.sectionQuizzes?.find { it.id == "web-foundation-quiz-pend" }
        org.junit.Assert.assertNotNull(pendQuiz)
        val q1 = pendQuiz?.questionPool?.find { it.id == 1 }
        org.junit.Assert.assertEquals("web-foundation-pend-2", q1?.sourceModuleId)
    }

    @Test
    fun testCssDasarSectionQuizIntegrity() {
        val course = com.example.data.CourseCatalog.courses.find { it.id == "fe-web-foundation-00" }
        org.junit.Assert.assertNotNull(course)

        val cssQuiz = course?.sectionQuizzes?.find { it.id == "web-foundation-quiz-css-dasar" }
        org.junit.Assert.assertNotNull("web-foundation-quiz-css-dasar must exist in sectionQuizzes", cssQuiz)
        assertEquals("fe-web-foundation-00", cssQuiz?.courseId)
        assertEquals(ModuleSection.CSS_DASAR, cssQuiz?.section)
        assertEquals("Kuis Dasar CSS", cssQuiz?.title)
        assertEquals(5, cssQuiz?.questionsPerAttempt)
        assertEquals(80, cssQuiz?.passingPercent)
        assertEquals(15, cssQuiz?.durationMinutes)
        assertEquals(20, cssQuiz?.questionPool?.size)

        val moduleIds = (1..7).map { "web-foundation-css-$it" }.toSet()
        val foundModuleIds = mutableSetOf<String>()

        cssQuiz?.questionPool?.forEach { q ->
            assertEquals("Each question must have 4 options", 4, q.options.size)
            assertTrue("correctIndex must be between 0 and 3", q.correctIndex in 0..3)
            assertTrue("question text must not be blank", q.question.isNotBlank())
            assertTrue("explanation must not be blank", q.explanation.isNotBlank())
            assertNotNull("sourceModuleId must not be null", q.sourceModuleId)
            assertTrue("sourceModuleId must be in css-1..7: ${q.sourceModuleId}", q.sourceModuleId in moduleIds)
            foundModuleIds.add(q.sourceModuleId!!)
        }

        assertEquals("Questions must cover all 7 CSS Dasar modules", moduleIds, foundModuleIds)
    }

    @Test
    fun testMigratedModulesCountAndOrderIntegrity() {
        val course = com.example.data.CourseCatalog.courses.find { it.id == "fe-web-foundation-00" }
        org.junit.Assert.assertNotNull("Course fe-web-foundation-00 must exist", course)

        val modules = course?.modules ?: emptyList()
        // Exactly 106 modules (49 HTML + 7 CSS Dasar + 50 Pendalaman CSS) loaded from Markdown assets
        assertEquals("Course fe-web-foundation-00 must have exactly 106 modules", 106, modules.size)
        assertEquals("Course durationMinutes must equal total readingMinutes of all 106 modules", 573, course?.durationMinutes)
        assertEquals("Sum of all modules readingMinutes must equal 573", 573, modules.sumOf { it.readingMinutes })

        // Verify strictly increasing order and non-blank content/title
        modules.forEachIndexed { index, mod ->
            assertTrue("Module ${mod.id} order must be positive", mod.order > 0)
            if (index > 0) {
                assertTrue("Module ${mod.id} order (${mod.order}) must be strictly greater than previous (${modules[index - 1].order})", mod.order > modules[index - 1].order)
            }
            assertTrue("Module ${mod.id} content must not be blank", mod.content.isNotBlank())
            assertTrue("Module ${mod.id} title must not be blank", mod.title.isNotBlank())
        }

        // Verify key modules across sections
        val prep1 = modules.first()
        assertEquals("web-foundation-prep-1", prep1.id)
        assertEquals(ModuleSection.PERSIAPAN, prep1.section)

        val pend1 = modules.find { it.id == "web-foundation-pend-1" }
        org.junit.Assert.assertNotNull(pend1)
        assertEquals(9, pend1?.order)
        assertEquals(ModuleSection.PENDAHULUAN, pend1?.section)

        val inti4 = modules.find { it.id == "web-foundation-inti-4" }
        org.junit.Assert.assertNotNull(inti4)
        assertEquals(19, inti4?.order)
        assertEquals(ModuleSection.INTI, inti4?.section)
        assertEquals(3, inti4?.verificationRules?.size)
        assertTrue(inti4?.isProjectContinuation == true)

        val lastHtmlMod = modules.find { it.id == "web-foundation-lanjutan-26" }
        org.junit.Assert.assertNotNull(lastHtmlMod)
        assertEquals(49, lastHtmlMod?.order)
        assertEquals(ModuleSection.LANJUTAN, lastHtmlMod?.section)

        val lastCssDasarMod = modules.find { it.id == "web-foundation-css-7" }
        org.junit.Assert.assertNotNull(lastCssDasarMod)
        assertEquals(56, lastCssDasarMod?.order)
        assertEquals(ModuleSection.CSS_DASAR, lastCssDasarMod?.section)

        val pcss29 = modules.find { it.id == "web-foundation-pcss-29" }
        org.junit.Assert.assertNotNull(pcss29)
        assertEquals(85, pcss29?.order)
        assertEquals(ModuleSection.PENDALAMAN_CSS, pcss29?.section)

        val pcss36 = modules.find { it.id == "web-foundation-pcss-36" }
        org.junit.Assert.assertNotNull(pcss36)
        assertEquals(92, pcss36?.order)
        assertEquals(ModuleSection.PENDALAMAN_CSS, pcss36?.section)

        val pcss43 = modules.find { it.id == "web-foundation-pcss-43" }
        org.junit.Assert.assertNotNull(pcss43)
        assertEquals(99, pcss43?.order)
        assertEquals(ModuleSection.PENDALAMAN_CSS, pcss43?.section)

        val lastMod = modules.last()
        assertEquals("web-foundation-pcss-50", lastMod.id)
        assertEquals(106, lastMod.order)
        assertEquals(ModuleSection.PENDALAMAN_CSS, lastMod.section)
    }

    @Test
    fun testCourseContentParserErrorHandlingGraceful() {
        // Corrupt / missing front-matter should return null and not crash
        val invalidMd = """
            # Just Markdown without front-matter
            Hello world
        """.trimIndent()
        val parsedInvalid = com.example.data.CourseContentParser.parseModule("test/invalid.md", invalidMd)
        org.junit.Assert.assertNull("Invalid markdown without frontmatter should return null", parsedInvalid)

        val incompleteFrontMatter = """
            ---
            id: only-id
            ---
            Content here
        """.trimIndent()
        val parsedIncomplete = com.example.data.CourseContentParser.parseModule("test/incomplete.md", incompleteFrontMatter)
        org.junit.Assert.assertNull("Incomplete frontmatter missing required fields should return null", parsedIncomplete)
    }

    @Test
    fun testGlossaryRegistryParsingAndLookup() {
        val glossaryMap = com.example.data.GlossaryRegistry.getGlossaryForCourse("fe-web-foundation-00")
        assertTrue("Glossary map for fe-web-foundation-00 should not be empty", glossaryMap.isNotEmpty())

        // Verify key terms
        val elemenItem = glossaryMap["elemen"]
        org.junit.Assert.assertNotNull("Term 'elemen' must exist in glossary", elemenItem)
        assertTrue("Definition of 'elemen' should describe HTML building unit", elemenItem?.definition?.contains("unit pembangun HTML", ignoreCase = true) == true)

        val selectorItem = glossaryMap["selector"]
        org.junit.Assert.assertNotNull("Term 'selector' must exist in glossary", selectorItem)
        assertTrue("Definition of 'selector' should describe CSS target marker", selectorItem?.definition?.contains("penanda target", ignoreCase = true) == true)

        val atributItem = glossaryMap["atribut"]
        org.junit.Assert.assertNotNull("Term 'atribut' must exist in glossary", atributItem)
        assertTrue("Definition of 'atribut' should describe additional info", atributItem?.definition?.contains("informasi tambahan", ignoreCase = true) == true)

        val domTreeItem = glossaryMap["dom tree"] ?: glossaryMap["dom"]
        org.junit.Assert.assertNotNull("Term 'DOM Tree' must exist in glossary", domTreeItem)

        // Verify safe null/empty handling
        val emptyGlossary = com.example.data.GlossaryRegistry.getGlossaryForCourse(null)
        assertTrue(emptyGlossary.isEmpty())

        val unknownGlossary = com.example.data.GlossaryRegistry.getGlossaryForCourse("non-existent-course")
        assertTrue(unknownGlossary.isEmpty())
    }

    @Test
    fun testGlossaryInlineWholeWordMatching() {
        val regex = com.example.data.GlossaryRegistry.getGlossaryRegexForCourse("fe-web-foundation-00")
        org.junit.Assert.assertNotNull("Glossary regex should be built for fe-web-foundation-00", regex)

        val validText = "Kamu sedang belajar menulis elemen dan atribut di Playground."
        val matches = regex!!.findAll(validText).map { it.value.lowercase() }.toList()

        assertTrue(matches.contains("elemen"))
        assertTrue(matches.contains("atribut"))
        assertTrue(matches.contains("playground"))

        // Whole-word boundary verification: should NOT match substrings inside other words
        val invalidText = "keelemenan dan atributnya bukanlah kata murni"
        val invalidMatches = regex.findAll(invalidText).toList()
        assertTrue("Substring matches inside words like 'keelemenan' must be rejected", invalidMatches.none { it.value.equals("elemen", ignoreCase = true) })
    }
}
