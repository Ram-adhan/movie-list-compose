package com.example.moviedb.feature.movie.list

import androidx.compose.runtime.Immutable
import com.example.moviedb.core.ui.ViewState
import com.example.moviedb.domain.model.Movie

data class MovieListScreenState(
    val popularMovies: MovieListState = MovieListState.Idle,
    val topRatedMovies: MovieListState = MovieListState.Idle,
    val nowPlaying: MovieListState = MovieListState.Idle,
): ViewState

sealed interface MovieListState {
    data object Idle: MovieListState
    data object Loading: MovieListState
    @Immutable
    data class Finish(val list: List<Movie>): MovieListState
}
