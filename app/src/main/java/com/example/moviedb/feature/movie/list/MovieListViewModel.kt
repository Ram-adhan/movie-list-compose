package com.example.moviedb.feature.movie.list

import androidx.lifecycle.viewModelScope
import com.example.moviedb.core.ui.theme.BaseViewModel
import com.example.moviedb.domain.MovieRepository
import com.example.moviedb.domain.model.ImageContext
import com.example.moviedb.domain.model.ImageSize
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieRepository: MovieRepository
): BaseViewModel<MovieListScreenState>(MovieListScreenState()) {

    fun loadAll() {
        viewModelScope.launch {
            stateField.update {
                it.copy(
                    popularMovies = MovieListState.Loading,
                    topRatedMovies = MovieListState.Loading,
                    nowPlaying = MovieListState.Loading,
                )
            }
            launch { loadPopularMovies() }
            launch { loadTopRatedMovies() }
            launch { loadNowPlayingMovies() }
        }
    }

    private suspend fun loadPopularMovies() {
        movieRepository.getPopularMovies(ImageContext.BACKDROP, ImageSize.MEDIUM).collect { response ->
            response.onSuccess { result ->
                stateField.update { it.copy(popularMovies = MovieListState.Finish(result)) }
            }
        }
    }

    private suspend fun loadTopRatedMovies() {
        movieRepository.getTopRatedMovies(ImageSize.MEDIUM).collect { response ->
            response.onSuccess { result ->
                stateField.update { it.copy(topRatedMovies = MovieListState.Finish(result)) }
            }
        }
    }

    private suspend fun loadNowPlayingMovies() {
        movieRepository.getNowPlayingMovies(ImageSize.MEDIUM).collect { response ->
            response.onSuccess { result ->
                stateField.update { it.copy(nowPlaying = MovieListState.Finish(result)) }
            }
        }
    }

}