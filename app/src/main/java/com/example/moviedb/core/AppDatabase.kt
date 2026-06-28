package com.example.moviedb.core

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moviedb.data.entity.MovieFavorite
import com.example.moviedb.data.entity.MovieFavoriteDao

@Database(
    entities = [MovieFavorite::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun movieFavoriteDao(): MovieFavoriteDao
}