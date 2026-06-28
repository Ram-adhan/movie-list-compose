package com.example.moviedb.core.di

import com.example.moviedb.data.MovieRepositoryImpl
import com.example.moviedb.domain.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideMovieRepository(impl: MovieRepositoryImpl): MovieRepository
}