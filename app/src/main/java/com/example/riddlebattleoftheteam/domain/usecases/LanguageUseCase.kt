package com.example.riddlebattleoftheteam.domain.usecases

import android.content.Context
import android.content.res.Configuration
import com.example.riddlebattleoftheteam.domain.repository.LanguageRepository
import java.util.Locale
import javax.inject.Inject

class LanguageUseCase @Inject constructor(
    private val languageRepository: LanguageRepository
) {
    fun getCurrentLanguage(): String = languageRepository.getLanguage()

    fun setLanguage(languageCode: String) {
        languageRepository.setLanguage(languageCode)
    }

    fun wrapContext(context: Context): Context {
        val locale = Locale(getCurrentLanguage())
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }
}