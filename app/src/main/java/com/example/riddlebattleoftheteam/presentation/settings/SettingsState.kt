package com.example.riddlebattleoftheteam.presentation.settings

data class SettingsState(
    val currentLanguage: String = "",
    val isLoading: Boolean = false,
    val isLanguageChanged: Boolean = false,
    val showSuccessMessage: Boolean = false,
    val error: String? = null
)
