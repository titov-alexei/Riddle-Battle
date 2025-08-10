package com.example.riddlebattleoftheteam.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.riddlebattleoftheteam.R
import com.example.riddlebattleoftheteam.presentation.components.Logo
import com.example.riddlebattleoftheteam.presentation.screens.About
import com.example.riddlebattleoftheteam.presentation.screens.Home
import com.example.riddlebattleoftheteam.presentation.screens.SettingsScreen
import com.example.riddlebattleoftheteam.presentation.screens.GamePreparationScreen
import com.example.riddlebattleoftheteam.presentation.screens.GameScreen
import com.example.riddlebattleoftheteam.presentation.screens.GameResultsScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavController() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in listOf(
        Screen.Home.route,
        Screen.Settings.route,
        Screen.About.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                Navigation(navController)
            }
        }
    ) {
        Image(  //background
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop  //Равномерное увеличение изображения
        )
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Box(
                modifier = Modifier
                    .weight(1f),
                contentAlignment = Alignment.Center
            ){
                if (showBottomBar) {
                    Logo()
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
            )
        }

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
        ) {
            composable (Screen.Home.route) {
                Home(navController)
            }
            composable (Screen.Settings.route) {
                SettingsScreen()
            }
            composable (Screen.About.route) {
                About()
            }

            // Игровые экраны (без нижней панели)
            composable(Screen.GamePreparationScreen.route) {
                GamePreparationScreen(navController)
            }
            composable(Screen.GameScreen.route) {
                GameScreen(navController)
            }
            composable(Screen.GameResultScreen.route) {
                GameResultsScreen(navController)
            }

        }
    }
}