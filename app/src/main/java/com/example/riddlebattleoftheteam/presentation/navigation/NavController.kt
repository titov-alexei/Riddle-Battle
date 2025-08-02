package com.example.riddlebattleoftheteam.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.riddlebattleoftheteam.R
import com.example.riddlebattleoftheteam.presentation.components.Logo
import com.example.riddlebattleoftheteam.presentation.screens.About
import com.example.riddlebattleoftheteam.presentation.screens.Home
import com.example.riddlebattleoftheteam.presentation.screens.Settings

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavController() {

    val navController = rememberNavController()
    Scaffold(
        bottomBar = { Navigation(navController) }
    ) {
        Image(  //background
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop  //Равномерное увеличение изображения
        )
        Column( //The window was divided into 2 parts
            modifier = Modifier.fillMaxSize(),
        ) {
            Box(//Upper part for logo
                modifier = Modifier
                    .weight(1f),
                contentAlignment = Alignment.Center
            ){
                Logo()
            }
            Box(//Bottom part
                modifier = Modifier
                    .weight(1f)
            )
        }

        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
        ) {
            composable (BottomNavItem.Home.route) {
                Home()
            }
            composable (BottomNavItem.Settings.route) {
                Settings()
            }
            composable (BottomNavItem.About.route) {
                About()
            }
        }
    }



}