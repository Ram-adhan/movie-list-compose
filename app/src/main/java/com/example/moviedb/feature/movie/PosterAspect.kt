package com.example.moviedb.feature.movie

enum class PosterAspect(val ratio: Float) {
    Poster(2f / 3f),
    Backdrop(16f / 9f),
    BackdropExt(16f / 11f),
}