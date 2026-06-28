package com.example.moviedb.domain.model

data class MovieDetail(
    val id: Long,
    val title: String,
    val releaseDate: String,
    val posterPath: String,
    val backdropPath: String,
    val description: String,
)
