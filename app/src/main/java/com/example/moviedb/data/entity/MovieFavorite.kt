package com.example.moviedb.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_favorites")
data class MovieFavorite(
    @PrimaryKey
    val id: Long,
    val title: String,
    val releaseDate: String,
    val overview: String,
    val posterPath: String,
    val backdropPath: String,
)
