package com.example.moviedb.core.di

import android.content.Context
import androidx.room.Room
import com.example.moviedb.core.AppDatabase
import com.example.moviedb.data.entity.MovieFavoriteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app_database"
    ).build()

    @Provides
    @Singleton
    fun provideMovieFavoriteDao(appDatabase: AppDatabase): MovieFavoriteDao = appDatabase.movieFavoriteDao()
}