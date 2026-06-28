package com.example.moviedb.feature.movie.favorited

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.example.moviedb.core.ui.theme.Purple40
import kotlinx.serialization.Serializable

@Serializable
data object MovieFavoriteNav: NavKey

fun EntryProviderScope<NavKey>.movieFavoriteEntry(
    toDetail: (Long) -> Unit,
    onBack: () -> Unit,
) {
    entry<MovieFavoriteNav> {
        val viewModel: MovieFavoriteViewModel = hiltViewModel()
        MovieFavoriteRoute(viewModel, toDetail, onBack)
    }
}

@Composable
fun MovieFavoriteRoute(
    viewModel: MovieFavoriteViewModel,
    toDetail: (Long) -> Unit,
    onBack: () -> Unit,
) {
    val uiState = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getFavorites()
    }

    Scaffold(
        topBar = {
            AppBar("Favorite",
                contentColor = Color.White,
                onBackClick = onBack,
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            MovieFavoriteScreen(uiState.value.list.orEmpty(), toDetail)
        }
    }
}