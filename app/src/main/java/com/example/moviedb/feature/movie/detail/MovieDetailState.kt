package com.example.moviedb.feature.movie.detail

import com.example.moviedb.core.ui.ViewState
import com.example.moviedb.domain.model.MovieDetail
import com.example.moviedb.domain.model.MovieReview

data class MovieDetailState(
    val movieDetail: MovieDetail? = null,
    val reviews: List<MovieReview>? = null,
    val loadingKeys: Set<LoadingKeys> = emptySet(),
    val shareText: String = "",
    val isFavorite: Boolean = false,
    val error: Pair<String, String>? = null
): ViewState {
    companion object {
        enum class LoadingKeys {
            Detail, Review
        }
    }

    val isLoading get() = loadingKeys.isNotEmpty()
}
