package com.example.riddlebattleoftheteam.utils

import android.content.Context
import android.content.SharedPreferences
import java.util.Locale
import androidx.core.content.edit

class LanguageHelper(private val context: Context) {

    companion object {
        private const val PREFS_NAME = "AppSettings"
        private const val LANGUAGE_KEY = "AppLanguage"
    }

    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)


    // Сохраняем выбранный язык
    fun setLanguage(languageCode: String) {
        sharedPref.edit { putString(LANGUAGE_KEY, languageCode) }
        updateAppLocale(languageCode)
    }

    // Получаем сохранённый язык (или системный по умолчанию)
    fun getCurrentLanguage(): String {
        return sharedPref.getString(LANGUAGE_KEY, Locale.getDefault().language) ?: "en"
    }

    // Применяем язык ко всему приложению
    private fun updateAppLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val resources = context.resources
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}