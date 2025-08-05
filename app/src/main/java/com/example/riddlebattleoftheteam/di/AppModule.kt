package com.example.riddlebattleoftheteam.di


import android.content.Context
import android.content.SharedPreferences
import com.example.riddlebattleoftheteam.data.repository.LanguageRepositoryImpl
import com.example.riddlebattleoftheteam.domain.repository.LanguageRepository
import com.example.riddlebattleoftheteam.domain.usecases.LanguageUseCase
import com.example.riddlebattleoftheteam.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(
            Constants.PREFS_NAME,
            Context.MODE_PRIVATE
        )
    }

    @Provides
    fun provideLanguageRepository(
        prefs: SharedPreferences,
        @ApplicationContext context: Context
    ): LanguageRepository {
        return LanguageRepositoryImpl(context, prefs)
    }

    @Provides
    fun provideLanguageUseCase(
        repository: LanguageRepository
    ): LanguageUseCase {
        return LanguageUseCase(repository)
    }
}