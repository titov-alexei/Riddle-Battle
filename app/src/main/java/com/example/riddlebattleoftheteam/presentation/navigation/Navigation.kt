package com.example.riddlebattleoftheteam.presentation.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.riddlebattleoftheteam.ui.theme.Dimens
import com.example.riddlebattleoftheteam.ui.theme.LightGreen


@Composable
fun Navigation(navController: NavHostController) {

    val listMenuItem = listOf(
        Screen.Home,
        Screen.Settings,
        Screen.About,
    )

    NavigationBar(
        modifier = Modifier
            .height(height = Dimens.NavHeight)  //Высота всего navbar
            .padding(bottom = Dimens.BottomPadding), //Отступ от нижнего края экрана
        containerColor = Color.Transparent,
        contentColor = Color.White,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        listMenuItem.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.route,
                        modifier = Modifier
                            .size(Dimens.IconSize)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = LightGreen,      // Цвет выбранной иконки
                    unselectedIconColor = Color.White,     // Цвет невыбранной иконки
                    indicatorColor = Color.Transparent    // Фон выбранного пункта (опционально)
                ),
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}