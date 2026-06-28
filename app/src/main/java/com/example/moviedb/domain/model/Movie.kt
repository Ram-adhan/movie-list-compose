package com.example.moviedb.domain.model

data class Movie(
    val id: Long,
    val title: String,
    val imageLink: String?,
    val releaseDate: String,
)
