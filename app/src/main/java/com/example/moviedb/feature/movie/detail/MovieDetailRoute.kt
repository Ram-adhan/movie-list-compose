package com.example.moviedb.feature.movie.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.moviedb.R
import com.example.moviedb.core.GlobalConstants
import com.example.moviedb.core.ui.components.AppBar
import com.example.moviedb.core.ui.components.BaseLoader
import com.example.moviedb.core.ui.components.ErrorBottomSheet
import com.example.moviedb.core.ui.theme.Purple40
import com.example.moviedb.feature.movie.list.MovieListScreen
import com.example.moviedb.feature.movie.list.MovieListViewModel
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailNav(val id: Long): NavKey

fun EntryProviderScope<NavKey>.movieDetailEntry(
    onBack: () -> Unit,
) {
    entry<MovieDetailNav> {
        val viewModel: MovieDetailViewModel = hiltViewModel()
        MovieDetailRoute(viewModel, id = it.id, onBack)
    }
}

@Composable
fun MovieDetailRoute(
    viewModel: MovieDetailViewModel,
    id: Long,
    onBack: () -> Unit,
) {
    val uiState = viewModel.state.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    val context = LocalContext.current

    val scrollFraction by remember {
        derivedStateOf {
            (scrollState.value.coerceAtMost(300) / 300f)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getDetail(id)
        viewModel.getReview(id)
        viewModel.isInFavorite(id)
    }

    Scaffold { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            uiState.value.movieDetail?.let {
                Column(modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                ) {
                    MovieDetailScreen(
                        it,
                        uiState.value.reviews,
                    )
                    Spacer(Modifier.height(innerPadding.calculateBottomPadding() + 52.dp))
                }
            }
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(end = 16.dp)
                    .padding(bottom = innerPadding.calculateBottomPadding()),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(viewModel::toggleToFavorite) {
                    Icon(
                        if (uiState.value.isFavorite) {
                            painterResource(R.drawable.ic_favorite_fill)
                        } else {
                            painterResource(R.drawable.ic_favorite_outline)
                        },
                        contentDescription = "Favorite")
                }
                IconButton(onClick = {
                    GlobalConstants.shareText(context, uiState.value.shareText)
                }) {
                    Icon(painterResource(R.drawable.ic_send_external),
                        contentDescription = "Favorite")
                }
            }
            AppBar(uiState.value.movieDetail?.title ?: "Movie Detail",
                scrollFraction = scrollFraction,
                backgroundColor = Purple40,
                contentColor = Color.White,
                onBackClick = onBack,
            )

            uiState.value.error?.let {
                ErrorBottomSheet(
                    title = it.first,
                    description = it.second,
                    onDismiss = { viewModel.dismissError() },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            BaseLoader(uiState.value.isLoading)
        }
    }
}