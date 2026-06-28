package com.example.moviedb.feature.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.moviedb.AppApplication
import com.example.moviedb.feature.movie.detail.MovieDetailNav
import com.example.moviedb.feature.movie.detail.movieDetailEntry
import com.example.moviedb.feature.movie.favorited.MovieFavoriteNav
import com.example.moviedb.feature.movie.favorited.movieFavoriteEntry
import com.example.moviedb.feature.movie.list.MovieListNav
import com.example.moviedb.feature.movie.list.movieListEntry

@Composable
fun MainAppNavigation() {
    val backStack = rememberNavBackStack(MovieListNav)

    fun popCurrent() {
        backStack.removeAt(backStack.lastIndex)
    }

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        transitionSpec = {
            slideInHorizontally(
                animationSpec = tween(500),
                initialOffsetX = { i -> i}
            ) togetherWith scaleOut(
                animationSpec = tween(500),
                targetScale = 0.8f
            )
        },
        popTransitionSpec = {
            scaleIn(
                animationSpec = tween(500),
                initialScale = 0.8f
            ) togetherWith slideOutHorizontally(
                animationSpec = tween(500),
                targetOffsetX = { i -> i }
            )
        },
        entryProvider = entryProvider {
            movieListEntry(
                onFavoriteClick = {
                    backStack.add(MovieFavoriteNav)
                },
                toMovieDetail = {
                    backStack.add(MovieDetailNav(it))
                }
            )
            movieDetailEntry(
                onBack = ::popCurrent
            )
            movieFavoriteEntry(
                toDetail = {
                    backStack.add(MovieDetailNav(it))
                },
                onBack = ::popCurrent
            )
        },
        onBack = ::popCurrent
    )
}