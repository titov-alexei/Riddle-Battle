package com.example.riddlebattleoftheteam.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.riddlebattleoftheteam.BuildConfig
import com.example.riddlebattleoftheteam.data.repository.LanguageRepositoryImpl
import com.example.riddlebattleoftheteam.domain.repository.LanguageRepository
import com.example.riddlebattleoftheteam.domain.usecases.LanguageUseCase
import com.example.riddlebattleoftheteam.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.example.riddlebattleoftheteam.data.api.RiddleApiService
import com.example.riddlebattleoftheteam.data.db.AppDatabase
import com.example.riddlebattleoftheteam.data.repository.RiddleRepositoryImpl
import com.example.riddlebattleoftheteam.data.repository.TeamRepositoryImpl
import com.example.riddlebattleoftheteam.domain.repository.RiddleRepository
import com.example.riddlebattleoftheteam.domain.repository.TeamRepository

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            })
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("X-Api-Key", BuildConfig.API_KEY)
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideRiddleApiService(retrofit: Retrofit): RiddleApiService {
        return retrofit.create(RiddleApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "riddle-db"
        )
            .addMigrations(AppDatabase.MIGRATION_1_2)
            .fallbackToDestructiveMigrationOnDowngrade()
            //.fallbackToDestructiveMigration() //удалит все данные при изменении схемы
            .build()
    }

    @Provides
    @Singleton
    fun provideRiddleRepository(
        api: RiddleApiService,
        db: AppDatabase
    ): RiddleRepository {
        return RiddleRepositoryImpl(api, db)
    }

    @Provides
    @Singleton
    fun provideTeamRepository(db: AppDatabase): TeamRepository {
        return TeamRepositoryImpl(db)
    }


    //For change language
    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(
            Constants.PREFS_NAME,
            Context.MODE_PRIVATE
        )
    }

    @Provides
    @Singleton
    fun provideLanguageRepository(
        prefs: SharedPreferences,
        @ApplicationContext context: Context
    ): LanguageRepository {
        return LanguageRepositoryImpl(context, prefs)
    }

    @Provides
    @Singleton
    fun provideLanguageUseCase(
        repository: LanguageRepository
    ): LanguageUseCase {
        return LanguageUseCase(repository)
    }
}