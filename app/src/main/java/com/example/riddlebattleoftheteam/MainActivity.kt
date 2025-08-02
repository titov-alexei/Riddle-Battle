package com.example.riddlebattleoftheteam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.riddlebattleoftheteam.presentation.navigation.NavController
import com.example.riddlebattleoftheteam.ui.theme.RiddleBattleOfTheTeamTheme
import com.example.riddlebattleoftheteam.utils.LanguageHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageHelper(this).getCurrentLanguage() // Применяем сохранённый язык

        hideSystemNavigationBar() //Скрытие системной навигации снизу в приложении

        setContent {
            RiddleBattleOfTheTeamTheme {
                NavController()
            }
        }
    }

    private fun hideSystemNavigationBar() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.apply {
            hide(WindowInsetsCompat.Type.navigationBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemNavigationBar()
    }
}