package com.example.moviedb.feature.movie.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.moviedb.R
import com.example.moviedb.core.ui.components.AppBar
import com.example.moviedb.core.ui.theme.Purple40
import kotlinx.serialization.Serializable

@Serializable
data object MovieListNav: NavKey

fun EntryProviderScope<NavKey>.movieListEntry(
    onFavoriteClick: () -> Unit,
    toMovieDetail: (id: Long) -> Unit
) {
    entry<MovieListNav> {
        val viewModel: MovieListViewModel = hiltViewModel()
        MovieListRoute(viewModel, onFavoriteClick, toMovieDetail)
    }
}

@Composable
fun MovieListRoute(
    viewModel: MovieListViewModel,
    onFavoriteClick: () -> Unit,
    toMovieDetail: (id: Long) -> Unit,
) {
    val uiState = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadAll()
    }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            AppBar("Movie",
                contentColor = Color.White,
                leftView = { contentColor ->
                    IconButton(onClick = onFavoriteClick) {
                        Icon(
                            painterResource(R.drawable.ic_favorite_fill),
                            contentDescription = "Favorite",
                            tint = contentColor
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            MovieListScreen(
                popularMovies = uiState.value.popularMovies,
                topRatedMovies = uiState.value.topRatedMovies,
                nowPlayingMovies = uiState.value.nowPlaying,
                toMovieDetail = toMovieDetail
            )
        }
    }
}