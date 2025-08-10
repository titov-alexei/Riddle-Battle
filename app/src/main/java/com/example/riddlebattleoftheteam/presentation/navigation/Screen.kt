package com.example.riddlebattleoftheteam.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route : String, val icon : ImageVector) {
    object Home : Screen("home", Icons.Default.Home)
    object Settings : Screen("settings", Icons.Default.Settings)
    object About : Screen("about", Icons.Default.Info)

    object GamePreparationScreen : Screen("game_preparation", Icons.Default.Build)
    object GameScreen : Screen("game_screen", Icons.Default.PlayArrow)
    object GameResultScreen : Screen("game_result", Icons.Default.CheckCircle)


}