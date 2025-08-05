package com.example.riddlebattleoftheteam.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.core.content.edit
import com.example.riddlebattleoftheteam.domain.repository.LanguageRepository
import com.example.riddlebattleoftheteam.utils.Constants
import java.util.Locale
import javax.inject.Inject

class LanguageRepositoryImpl @Inject constructor(
    private val context: Context,
    private val prefs: SharedPreferences
) : LanguageRepository {

    override fun getLanguage(): String {
        return prefs.getString(Constants.LANGUAGE_KEY, null)
            ?: Locale.getDefault().language.takeIf { it in setOf(Constants.LANGUAGE_RU, Constants.LANGUAGE_EN) }
            ?: Constants.LANGUAGE_EN
    }

    override fun setLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        prefs.edit { putString(Constants.LANGUAGE_KEY, languageCode) }
    }
}