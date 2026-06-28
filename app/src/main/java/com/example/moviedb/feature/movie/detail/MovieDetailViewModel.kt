package com.example.moviedb.feature.movie.detail

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.moviedb.core.ui.theme.BaseViewModel
import com.example.moviedb.domain.MovieRepository
import com.example.moviedb.domain.model.ImageContext
import com.example.moviedb.domain.model.ImageSize
import com.example.moviedb.feature.movie.list.MovieListScreenState
import com.example.moviedb.feature.movie.list.MovieListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository
): BaseViewModel<MovieDetailState>(MovieDetailState()) {

    fun getDetail(id: Long) {
        viewModelScope.launch {
            stateField.update {
                it.copy(loadingKeys = it.loadingKeys + MovieDetailState.Companion.LoadingKeys.Detail)
            }
            movieRepository.getMovieDetail(id).onCompletion {
                stateField.update {
                    it.copy(loadingKeys = it.loadingKeys - MovieDetailState.Companion.LoadingKeys.Detail)
                }
            }.collect { response ->
                response.fold(
                    onSuccess = { result ->
                        val shareText = """
                            Check this movie!
                            
                            ${result.title}
                            
                            ${result.description}
                        """.trimIndent()
                        stateField.update {
                            it.copy(movieDetail = result, shareText = shareText)
                        }
                    },
                    onFailure = ::handleError
                )
            }
        }
    }

    fun getReview(id: Long) {
        viewModelScope.launch {
            stateField.update { it.copy(loadingKeys = it.loadingKeys + MovieDetailState.Companion.LoadingKeys.Review) }
            movieRepository.getMovieReview(id)
                .fold(
                    onSuccess = { result ->
                        Log.d("review", "$result")
                        stateField.update { it.copy(reviews = result) }
                    },
                    onFailure = ::handleError
                )
            stateField.update { it.copy(loadingKeys = it.loadingKeys - MovieDetailState.Companion.LoadingKeys.Review) }
        }
    }

    fun isInFavorite(id: Long) {
        viewModelScope.launch {
            movieRepository.isInFavorite(id)
                .fold(
                    onSuccess = {
                        stateField.update {
                            it.copy(
                                isFavorite = true
                            )
                        }
                    },
                    onFailure = {
                        if (stateField.value.isFavorite) {
                            stateField.update {
                                it.copy(
                                    isFavorite = false
                                )
                            }
                        }
                    }
                )
        }
    }

    fun toggleToFavorite() {
        val movieDetail = stateField.value.movieDetail ?: return
        viewModelScope.launch {
            if (stateField.value.isFavorite) {
                movieRepository.removeFromFavorite(movieDetail.id)
            } else {
                movieRepository.addToFavorite(movieDetail)
            }

            isInFavorite(movieDetail.id)
        }
    }

    private fun handleError(t: Throwable) {
        if (stateField.value.error != null) return

        stateField.update {
            it.copy(error = "Oops" to t.message.orEmpty())
        }
    }

    fun dismissError() {
        stateField.update {
            it.copy(error = null)
        }
    }
}