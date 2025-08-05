package com.example.riddlebattleoftheteam.domain.repository

interface LanguageRepository {
    fun getLanguage(): String
    fun setLanguage(languageCode: String)
}