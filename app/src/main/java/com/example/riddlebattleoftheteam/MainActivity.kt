package com.example.riddlebattleoftheteam

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.riddlebattleoftheteam.domain.usecases.LanguageUseCase
import com.example.riddlebattleoftheteam.domain.usecases.LanguageUseCaseEntryPoint
import com.example.riddlebattleoftheteam.presentation.navigation.NavController
import com.example.riddlebattleoftheteam.ui.theme.RiddleBattleOfTheTeamTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var languageUseCase: LanguageUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hideSystemNavigationBar() //Скрытие системной навигации снизу в приложении

        setContent {
            RiddleBattleOfTheTeamTheme {
                NavController()
            }
        }
    }

    //Вызывается перед созданием Actvity и устанавливает язык из sharedPreference
    override fun attachBaseContext(context: Context) {
        val contextWithInjection = EntryPointAccessors.fromApplication(
            context.applicationContext,
            LanguageUseCaseEntryPoint::class.java
        ).languageUseCase.wrapContext(context)

        super.attachBaseContext(contextWithInjection)
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