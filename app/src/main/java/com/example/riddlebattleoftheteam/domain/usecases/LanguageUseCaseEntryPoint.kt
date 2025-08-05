package com.example.riddlebattleoftheteam.domain.usecases

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

//Точка входа для инициализации languageUseCase в MainActivity
@EntryPoint
@InstallIn(SingletonComponent::class)
interface LanguageUseCaseEntryPoint {
    val languageUseCase: LanguageUseCase
}