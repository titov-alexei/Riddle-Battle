package com.example.riddlebattleoftheteam.utils

import android.content.Context
import android.content.res.Configuration
import android.widget.Toast
import androidx.annotation.StringRes
import com.example.riddlebattleoftheteam.domain.usecases.LanguageUseCase
import java.util.Locale

fun Context.showLocalizedToast(@StringRes resId: Int, langUseCase: LanguageUseCase) {
    val config = Configuration(resources.configuration)
    config.setLocale(Locale(langUseCase.getCurrentLanguage()))
    val localizedContext = createConfigurationContext(config)
    Toast.makeText(localizedContext, localizedContext.getText(resId), Toast.LENGTH_SHORT).show()
}