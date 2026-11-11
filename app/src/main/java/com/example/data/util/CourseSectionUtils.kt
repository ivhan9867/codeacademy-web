package com.example.data.util

import com.example.data.CourseCatalog
import com.example.data.models.Course
import com.example.data.models.ModuleSection

/**
 * Single source of truth for course module section titles and subtitles.
 * Provides custom titles defined per course (via Course.sectionTitles catalog JSON),
 * with special fallbacks for Front-End and Python, and generic fallbacks for other courses.
 */
object CourseSectionUtils {

    /**
     * Default generic section title when course-specific title is not provided.
     */
    fun defaultSectionTitle(section: ModuleSection): String {
        return when (section) {
            ModuleSection.PERSIAPAN -> "Persiapan"
            ModuleSection.PENDAHULUAN -> "Pendahuluan"
            ModuleSection.INTI -> "Materi Inti"
            ModuleSection.LANJUTAN -> "Materi Lanjutan"
            ModuleSection.CSS_DASAR -> "Dasar CSS"
            ModuleSection.PENDALAMAN_CSS -> "Pendalaman CSS"
            ModuleSection.EVALUASI -> "Evaluasi"
        }
    }

    /**
     * Returns section title for a Course instance.
     * Looks up in course.sectionTitles first, then falls back to courseId-based resolution.
     */
    fun sectionTitle(course: Course?, section: ModuleSection): String {
        if (course != null) {
            val fromMap = course.sectionTitles[section.name]
                ?: course.sectionTitles[section.name.uppercase()]
                ?: course.sectionTitles[section.name.lowercase()]
            if (!fromMap.isNullOrBlank()) {
                return fromMap
            }
            return sectionTitle(course.id, section)
        }
        return defaultSectionTitle(section)
    }

    /**
     * Returns section title based on courseId or trackId.
     */
    fun sectionTitle(courseIdOrTrackId: String?, section: ModuleSection): String {
        // 1. Try finding in CourseCatalog if catalog is loaded
        val catalogCourse = CourseCatalog.courses.firstOrNull {
            it.id.equals(courseIdOrTrackId, ignoreCase = true) || it.trackId.equals(courseIdOrTrackId, ignoreCase = true)
        }
        if (catalogCourse != null) {
            val fromMap = catalogCourse.sectionTitles[section.name]
                ?: catalogCourse.sectionTitles[section.name.uppercase()]
                ?: catalogCourse.sectionTitles[section.name.lowercase()]
            if (!fromMap.isNullOrBlank()) {
                return fromMap
            }
        }

        // 2. Fallbacks for known courses / tracks
        val id = courseIdOrTrackId?.lowercase() ?: ""
        return when {
            id == "fe-web-foundation-00" || id == "front-end" -> when (section) {
                ModuleSection.PERSIAPAN -> "Persiapan"
                ModuleSection.PENDAHULUAN -> "Pendahuluan"
                ModuleSection.INTI -> "Pengenalan HTML"
                ModuleSection.LANJUTAN -> "Pendalaman HTML"
                ModuleSection.CSS_DASAR -> "Dasar CSS"
                ModuleSection.PENDALAMAN_CSS -> "Pendalaman CSS"
                ModuleSection.EVALUASI -> "Evaluasi"
            }
            id == "py-fund-01" || id == "python" -> when (section) {
                ModuleSection.PERSIAPAN -> "Persiapan"
                ModuleSection.PENDAHULUAN -> "Dasar-Dasar Python"
                ModuleSection.INTI -> "Alur Program & Perulangan"
                ModuleSection.LANJUTAN -> "Struktur Data & Fungsi"
                ModuleSection.EVALUASI -> "Proyek & Evaluasi"
                ModuleSection.CSS_DASAR -> "Dasar CSS"
                ModuleSection.PENDALAMAN_CSS -> "Pendalaman CSS"
            }
            else -> defaultSectionTitle(section)
        }
    }

    /**
     * Returns section subtitle. Front-End retains its specific HTML/CSS subtitles,
     * while other courses receive appropriate clean generic subtitles.
     */
    fun sectionSubtitle(course: Course?, section: ModuleSection): String {
        val id = course?.id?.lowercase() ?: ""
        val isWebFoundation = id == "fe-web-foundation-00"

        return if (isWebFoundation) {
            when (section) {
                ModuleSection.PERSIAPAN -> "Persiapan & Pengaturan Alat"
                ModuleSection.PENDAHULUAN -> "Materi Pengantar & Silabus"
                ModuleSection.INTI -> "Materi Inti & Praktik"
                ModuleSection.LANJUTAN -> "Variasi Elemen & Latihan Lanjutan"
                ModuleSection.CSS_DASAR -> "Variasi Styling & Aturan Dasar CSS"
                ModuleSection.PENDALAMAN_CSS -> "Selector, Formatting Teks, Box Model & Layout"
                ModuleSection.EVALUASI -> "Uji pemahaman komprehensif untuk klaim sertifikat"
            }
        } else {
            when (section) {
                ModuleSection.PERSIAPAN -> "Persiapan & Pengaturan Alat"
                ModuleSection.PENDAHULUAN -> "Materi Pengantar & Silabus"
                ModuleSection.INTI -> "Materi Inti & Praktik"
                ModuleSection.LANJUTAN -> "Penerapan Lanjutan & Studi Kasus"
                ModuleSection.CSS_DASAR -> "Materi Styling & Desain"
                ModuleSection.PENDALAMAN_CSS -> "Tata Letak & Gaya Lanjutan"
                ModuleSection.EVALUASI -> "Uji pemahaman komprehensif untuk klaim sertifikat"
            }
        }
    }

    /**
     * Resolves resource ID for section title based on courseId and section.
     */
    fun getSectionTitleResId(courseId: String?, section: ModuleSection): Int {
        val id = courseId?.lowercase() ?: ""
        return when {
            id == "fe-web-foundation-00" || id == "front-end" -> when (section) {
                ModuleSection.PERSIAPAN -> com.example.R.string.section_title_persiapan
                ModuleSection.PENDAHULUAN -> com.example.R.string.section_title_pendahuluan
                ModuleSection.INTI -> com.example.R.string.section_title_fe_intro_html
                ModuleSection.LANJUTAN -> com.example.R.string.section_title_fe_deep_html
                ModuleSection.CSS_DASAR -> com.example.R.string.section_title_css_dasar
                ModuleSection.PENDALAMAN_CSS -> com.example.R.string.section_title_pendalaman_css
                ModuleSection.EVALUASI -> com.example.R.string.section_title_evaluasi
            }
            id == "fe-css-02" -> when (section) {
                ModuleSection.PERSIAPAN -> com.example.R.string.section_title_persiapan
                ModuleSection.PENDAHULUAN -> com.example.R.string.section_title_fe_why_modern
                ModuleSection.INTI -> com.example.R.string.section_title_fe_flexbox
                ModuleSection.LANJUTAN -> com.example.R.string.section_title_fe_grid
                ModuleSection.EVALUASI -> com.example.R.string.section_title_proyek_evaluasi
                else -> defaultSectionTitleResId(section)
            }
            id == "fe-js-03" -> when (section) {
                ModuleSection.PERSIAPAN -> com.example.R.string.section_title_persiapan
                ModuleSection.PENDAHULUAN -> com.example.R.string.section_title_fe_js_basics
                ModuleSection.INTI -> com.example.R.string.section_title_fe_js_logic
                ModuleSection.LANJUTAN -> com.example.R.string.section_title_fe_js_functions
                ModuleSection.EVALUASI -> com.example.R.string.section_title_proyek_evaluasi
                else -> defaultSectionTitleResId(section)
            }
            id == "be-node-01" || id == "back-end" -> when (section) {
                ModuleSection.PERSIAPAN -> com.example.R.string.section_title_persiapan
                ModuleSection.PENDAHULUAN -> com.example.R.string.section_title_be_understand_node
                ModuleSection.INTI -> com.example.R.string.section_title_be_modules_async
                ModuleSection.LANJUTAN -> com.example.R.string.section_title_be_server_npm
                ModuleSection.EVALUASI -> com.example.R.string.section_title_proyek_evaluasi
                else -> defaultSectionTitleResId(section)
            }
            id == "py-fund-01" || id == "python" -> when (section) {
                ModuleSection.PERSIAPAN -> com.example.R.string.section_title_persiapan
                ModuleSection.PENDAHULUAN -> com.example.R.string.section_title_py_basics
                ModuleSection.INTI -> com.example.R.string.section_title_py_flow
                ModuleSection.LANJUTAN -> com.example.R.string.section_title_py_structs
                ModuleSection.EVALUASI -> com.example.R.string.section_title_proyek_evaluasi
                else -> defaultSectionTitleResId(section)
            }
            id == "c-fund-01" || id == "c-lang" -> when (section) {
                ModuleSection.PERSIAPAN -> com.example.R.string.section_title_persiapan
                ModuleSection.PENDAHULUAN -> com.example.R.string.section_title_c_basics
                ModuleSection.INTI -> com.example.R.string.section_title_c_flow
                ModuleSection.LANJUTAN -> com.example.R.string.section_title_c_pointers
                ModuleSection.EVALUASI -> com.example.R.string.section_title_proyek_evaluasi
                else -> defaultSectionTitleResId(section)
            }
            id == "cpp-fund-01" || id == "cpp-lang" -> when (section) {
                ModuleSection.PERSIAPAN -> com.example.R.string.section_title_persiapan
                ModuleSection.PENDAHULUAN -> com.example.R.string.section_title_cpp_basics
                ModuleSection.INTI -> com.example.R.string.section_title_cpp_stl
                ModuleSection.LANJUTAN -> com.example.R.string.section_title_cpp_oop
                ModuleSection.EVALUASI -> com.example.R.string.section_title_proyek_evaluasi
                else -> defaultSectionTitleResId(section)
            }
            else -> defaultSectionTitleResId(section)
        }
    }

    fun defaultSectionTitleResId(section: ModuleSection): Int = when (section) {
        ModuleSection.PERSIAPAN -> com.example.R.string.section_title_persiapan
        ModuleSection.PENDAHULUAN -> com.example.R.string.section_title_pendahuluan
        ModuleSection.INTI -> com.example.R.string.section_title_inti
        ModuleSection.LANJUTAN -> com.example.R.string.section_title_lanjutan
        ModuleSection.CSS_DASAR -> com.example.R.string.section_title_css_dasar
        ModuleSection.PENDALAMAN_CSS -> com.example.R.string.section_title_pendalaman_css
        ModuleSection.EVALUASI -> com.example.R.string.section_title_evaluasi
    }

    fun getSectionSubtitleResId(courseId: String?, section: ModuleSection): Int {
        val id = courseId?.lowercase() ?: ""
        val isWebFoundation = id == "fe-web-foundation-00"

        return if (isWebFoundation) {
            when (section) {
                ModuleSection.PERSIAPAN -> com.example.R.string.section_sub_persiapan_tools
                ModuleSection.PENDAHULUAN -> com.example.R.string.section_sub_pengantar_silabus
                ModuleSection.INTI -> com.example.R.string.section_sub_inti_praktik
                ModuleSection.LANJUTAN -> com.example.R.string.section_sub_variasi_elemen
                ModuleSection.CSS_DASAR -> com.example.R.string.section_sub_variasi_styling
                ModuleSection.PENDALAMAN_CSS -> com.example.R.string.section_sub_selector_box_model
                ModuleSection.EVALUASI -> com.example.R.string.section_sub_evaluasi_sertifikat
            }
        } else {
            when (section) {
                ModuleSection.PERSIAPAN -> com.example.R.string.section_sub_persiapan_tools
                ModuleSection.PENDAHULUAN -> com.example.R.string.section_sub_pengantar_silabus
                ModuleSection.INTI -> com.example.R.string.section_sub_inti_praktik
                ModuleSection.LANJUTAN -> com.example.R.string.section_sub_penerapan_lanjutan
                ModuleSection.CSS_DASAR -> com.example.R.string.section_sub_styling_desain
                ModuleSection.PENDALAMAN_CSS -> com.example.R.string.section_sub_tata_letak_lanjutan
                ModuleSection.EVALUASI -> com.example.R.string.section_sub_evaluasi_sertifikat
            }
        }
    }

    @androidx.compose.runtime.Composable
    fun localizedSectionTitle(course: Course?, section: ModuleSection): String {
        val resId = getSectionTitleResId(course?.id, section)
        return androidx.compose.ui.res.stringResource(resId)
    }

    @androidx.compose.runtime.Composable
    fun localizedSectionTitle(courseIdOrTrackId: String?, section: ModuleSection): String {
        val resId = getSectionTitleResId(courseIdOrTrackId, section)
        return androidx.compose.ui.res.stringResource(resId)
    }

    fun localizedSectionTitle(course: Course?, section: ModuleSection, context: android.content.Context): String {
        val resId = getSectionTitleResId(course?.id, section)
        return context.getString(resId)
    }

    fun localizedSectionTitle(courseIdOrTrackId: String?, section: ModuleSection, context: android.content.Context): String {
        val resId = getSectionTitleResId(courseIdOrTrackId, section)
        return context.getString(resId)
    }

    @androidx.compose.runtime.Composable
    fun localizedSectionSubtitle(course: Course?, section: ModuleSection): String {
        val resId = getSectionSubtitleResId(course?.id, section)
        return androidx.compose.ui.res.stringResource(resId)
    }

    fun localizedSectionSubtitle(course: Course?, section: ModuleSection, context: android.content.Context): String {
        val resId = getSectionSubtitleResId(course?.id, section)
        return context.getString(resId)
    }
}
