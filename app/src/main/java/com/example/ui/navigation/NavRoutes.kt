package com.example.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Catalog : Screen("catalog", "Catalog", Icons.Default.LibraryBooks)
    object Progress : Screen("progress", "Progress", Icons.Default.BarChart)
    object Bookmarks : Screen("bookmarks", "Saved", Icons.Default.Bookmark)
    object Detail : Screen("detail", "Detail", Icons.Default.LibraryBooks)
    object Community : Screen("komunitas", "Komunitas", Icons.Default.Groups)
    object Search : Screen("search", "Cari Materi", Icons.Default.Search)
}
