package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.ManageSearch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.Topic
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.models.ModuleSection
import com.example.data.models.SearchResultItem
import com.example.data.util.CourseSectionUtils
import com.example.ui.MainViewModel
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.TealDark
import com.example.ui.theme.TealLight
import com.example.ui.theme.TealPrimary

/**
 * Screen that allows students to search for learning modules across all available courses.
 * Features live debounced search, highlighted keyword snippets, course/section badges,
 * loading state while assets load, and welcoming popular search chips.
 */
@Composable
fun SearchScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onNavigateToModule: (courseId: String, moduleId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.moduleSearchQuery.collectAsStateWithLifecycle()
    val searchResults by viewModel.moduleSearchResults.collectAsStateWithLifecycle()
    val isCoursesLoading by viewModel.isCoursesLoading.collectAsStateWithLifecycle()

    BackHandler {
        onBack()
    }

    SearchScreenContent(
        query = searchQuery,
        onQueryChange = { viewModel.setModuleSearchQuery(it) },
        results = searchResults,
        isLoading = isCoursesLoading,
        onBack = onBack,
        onResultClick = onNavigateToModule,
        modifier = modifier
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchScreenContent(
    query: String,
    onQueryChange: (String) -> Unit,
    results: List<SearchResultItem>,
    isLoading: Boolean,
    onBack: () -> Unit,
    onResultClick: (courseId: String, moduleId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val isDark = isSystemInDarkTheme()

    val popularKeywords = remember {
        listOf(
            "HTML", "Heading", "Elemen", "Atribut", "Form",
            "CSS", "Flexbox", "JavaScript", "DOM Tree", "Glosarium", "Doctype", "Tabel"
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0.dp),
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier
                                .testTag("search_back_button")
                                .size(44.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = androidx.compose.ui.res.stringResource(com.example.R.string.search_back_cd),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        OutlinedTextField(
                            value = query,
                            onValueChange = onQueryChange,
                            placeholder = {
                                Text(
                                    text = androidx.compose.ui.res.stringResource(com.example.R.string.search_input_placeholder),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = if (query.isNotBlank()) {
                                        if (isDark) CyanAccent else TealPrimary
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    }
                                )
                            },
                            trailingIcon = {
                                AnimatedVisibility(
                                    visible = query.isNotEmpty(),
                                    enter = fadeIn(),
                                    exit = fadeOut()
                                ) {
                                    IconButton(
                                        onClick = { onQueryChange("") },
                                        modifier = Modifier
                                            .testTag("search_clear_button")
                                            .size(36.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = androidx.compose.ui.res.stringResource(com.example.R.string.search_clear_cd),
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = if (isDark) CyanAccent else TealPrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                            ),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                            modifier = Modifier
                                .weight(1f)
                                .focusRequester(focusRequester)
                                .testTag("search_input_field")
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    // Loading State
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = if (isDark) CyanAccent else TealPrimary,
                            modifier = Modifier
                                .size(48.dp)
                                .testTag("search_loading_indicator")
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.search_loading_title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.search_loading_desc),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                query.trim().isEmpty() -> {
                    // Empty Query / Suggestions State
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(20.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                                ),
                                border = BorderStroke(
                                    1.dp,
                                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(52.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (isDark) TealLight.copy(alpha = 0.2f) else TealPrimary.copy(alpha = 0.12f)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ManageSearch,
                                            contentDescription = null,
                                            tint = if (isDark) CyanAccent else TealPrimary,
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = androidx.compose.ui.res.stringResource(com.example.R.string.search_intro_title),
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = androidx.compose.ui.res.stringResource(com.example.R.string.search_intro_desc),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(bottom = 12.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Tag,
                                        contentDescription = null,
                                        tint = if (isDark) CyanAccent else TealPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = androidx.compose.ui.res.stringResource(com.example.R.string.search_popular_topics_title),
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    popularKeywords.forEach { keyword ->
                                        SuggestionChip(
                                            onClick = { onQueryChange(keyword) },
                                            label = {
                                                Text(
                                                    text = keyword,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            },
                                            shape = RoundedCornerShape(10.dp),
                                            colors = SuggestionChipDefaults.suggestionChipColors(
                                                containerColor = MaterialTheme.colorScheme.surface,
                                                labelColor = MaterialTheme.colorScheme.onSurface
                                            ),
                                            border = BorderStroke(
                                                1.dp,
                                                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
                                            ),
                                            modifier = Modifier.testTag("search_chip_$keyword")
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                results.isEmpty() -> {
                    // No Results Found State
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 32.dp, vertical = 48.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SearchOff,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.search_empty_title),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.search_empty_desc, query),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.Center,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            popularKeywords.take(4).forEach { keyword ->
                                SuggestionChip(
                                    onClick = { onQueryChange(keyword) },
                                    label = { Text(keyword, style = MaterialTheme.typography.bodySmall) },
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }
                        }
                    }
                }

                else -> {
                    // Results List State
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = androidx.compose.ui.res.stringResource(com.example.R.string.search_found_results, results.size),
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) CyanAccent else TealPrimary
                                )
                                Text(
                                    text = androidx.compose.ui.res.stringResource(com.example.R.string.search_keyword_label, query),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        items(
                            items = results,
                            key = { "${it.courseId}_${it.moduleId}" }
                        ) { item ->
                            SearchResultCard(
                                item = item,
                                query = query,
                                onClick = { onResultClick(item.courseId, item.moduleId) }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Card displaying individual search result with highlighted title, course name badge,
 * section tag, and matched snippet text.
 */
@Composable
fun SearchResultCard(
    item: SearchResultItem,
    query: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val highlightColor = if (isDark) CyanAccent else TealPrimary

    val sectionLabel = item.section?.let { section ->
        CourseSectionUtils.localizedSectionTitle(item.courseId, section)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("search_result_${item.courseId}_${item.moduleId}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Icon panah navigasi di pojok kanan atas dengan posisi independen
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = androidx.compose.ui.res.stringResource(com.example.R.string.search_open_module_cd),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(18.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 26.dp) // Ruang bernapas agar tidak bertabrakan dengan ikon panah di kanan atas
            ) {
                // Badge Course (Baris mandiri dengan batas ruang jelas dan ellipsis jika teks panjang)
                Surface(
                    color = (if (isDark) TealLight else TealPrimary).copy(alpha = 0.14f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LibraryBooks,
                            contentDescription = null,
                            tint = if (isDark) CyanAccent else TealPrimary,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = item.courseTitle,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) CyanAccent else TealPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Label Section terpisah di baris tersendiri sebagai chip mandiri
                if (sectionLabel != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = sectionLabel,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Module Title with Highlighted Query
                val highlightedTitle = remember(item.moduleTitle, query, highlightColor) {
                    buildHighlightedString(
                        text = item.moduleTitle,
                        query = query,
                        highlightColor = highlightColor,
                        isDark = isDark
                    )
                }

                Text(
                    text = highlightedTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 22.sp
                )

                // Snippet context if matched in content
                if (!item.snippet.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val highlightedSnippet = remember(item.snippet, query, highlightColor) {
                            buildHighlightedString(
                                text = item.snippet,
                                query = query,
                                highlightColor = highlightColor,
                                isDark = isDark
                            )
                        }

                        Text(
                            text = highlightedSnippet,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Builds an AnnotatedString that highlights all occurrences of [query] in [text]
 * with a bold font weight and theme accent color.
 */
fun buildHighlightedString(
    text: String,
    query: String,
    highlightColor: Color,
    isDark: Boolean
): AnnotatedString {
    val cleanQuery = query.trim()
    if (cleanQuery.isEmpty()) return AnnotatedString(text)

    return buildAnnotatedString {
        var cursor = 0
        val lowerText = text.lowercase()
        val lowerQuery = cleanQuery.lowercase()
        val queryLength = cleanQuery.length

        while (cursor < text.length) {
            val matchIndex = lowerText.indexOf(lowerQuery, cursor)
            if (matchIndex == -1) {
                append(text.substring(cursor))
                break
            }

            if (matchIndex > cursor) {
                append(text.substring(cursor, matchIndex))
            }

            pushStyle(
                SpanStyle(
                    fontWeight = FontWeight.ExtraBold,
                    color = highlightColor,
                    background = highlightColor.copy(alpha = if (isDark) 0.22f else 0.12f)
                )
            )
            append(text.substring(matchIndex, matchIndex + queryLength))
            pop()

            cursor = matchIndex + queryLength
        }
    }
}
