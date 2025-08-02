package com.example.riddlebattleoftheteam.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route : String, val icon : ImageVector) {
    object Home : BottomNavItem("home", Icons.Default.Home)
    object Settings : BottomNavItem("settings", Icons.Default.Settings)
    object About : BottomNavItem("about", Icons.Default.Info)
}