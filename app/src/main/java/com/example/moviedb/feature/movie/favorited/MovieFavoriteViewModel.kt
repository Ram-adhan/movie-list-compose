package com.example.moviedb.feature.movie.favorited

import androidx.lifecycle.viewModelScope
import com.example.moviedb.core.ui.theme.BaseViewModel
import com.example.moviedb.domain.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.compose
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieFavoriteViewModel @Inject constructor(
    private val movieRepository: MovieRepository
): BaseViewModel<MovieFavoriteState>(MovieFavoriteState()) {

    fun getFavorites() {
        viewModelScope.launch {
            movieRepository.getAllFavorite().collect { result ->
                stateField.update { it.copy(list = result) }
            }
        }
    }

}