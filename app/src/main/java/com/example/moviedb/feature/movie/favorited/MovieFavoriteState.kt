package com.example.moviedb.feature.movie.favorited

import com.example.moviedb.core.ui.ViewState
import com.example.moviedb.domain.model.MovieDetail

data class MovieFavoriteState(
    val list: List<MovieDetail>? = null,
): ViewState
