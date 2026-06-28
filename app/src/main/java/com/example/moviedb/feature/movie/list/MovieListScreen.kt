package com.example.moviedb.feature.movie.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moviedb.core.ui.components.Skeleton
import com.example.moviedb.domain.model.Movie
import com.example.moviedb.feature.movie.PosterAspect

@Composable
fun MovieListScreen(
    popularMovies: MovieListState,
    topRatedMovies: MovieListState,
    nowPlayingMovies: MovieListState,
    toMovieDetail: (id: Long) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            "Popular Movies",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(horizontal = 16.dp).padding(top = 16.dp, bottom = 8.dp)
        )
        BannerMovieList(
            list = (popularMovies as? MovieListState.Finish)?.list.orEmpty(),
            isLoading = popularMovies is MovieListState.Loading,
            toMovieDetail
        )
        Text(
            "Top Rated Movies",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(horizontal = 16.dp).padding(top = 16.dp, bottom = 8.dp)
        )
        PosterMovieList(
            list = (topRatedMovies as? MovieListState.Finish)?.list.orEmpty(),
            isLoading = topRatedMovies is MovieListState.Loading,
            toMovieDetail
        )
        Text(
            "Now Playing",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(horizontal = 16.dp).padding(top = 16.dp, bottom = 8.dp)
        )
        PosterMovieList(
            list = (nowPlayingMovies as? MovieListState.Finish)?.list.orEmpty(),
            isLoading = nowPlayingMovies is MovieListState.Loading,
            toMovieDetail
        )

    }
}

@Composable
fun BannerMovieList(
    list: List<Movie>,
    isLoading: Boolean,
    toMovieDetail: (id: Long) -> Unit
) {
    LazyRow(
        modifier = Modifier.height(210.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        if (!isLoading) {
            items(
                list
                    .filterNot { it.imageLink == null }
            ) { movie ->
                BannerItem(
                    modifier = Modifier.fillMaxHeight().aspectRatio(PosterAspect.Backdrop.ratio)
                        .clickable {
                            toMovieDetail.invoke(movie.id)
                        },
                    imageUrl = movie.imageLink!!, title = movie.title
                )
            }
        } else {
            items(count = 5) {
                Skeleton(
                    modifier = Modifier.fillMaxHeight().aspectRatio(PosterAspect.Backdrop.ratio),
                )
            }
        }
    }
}

@Composable
fun PosterMovieList(
    list: List<Movie>,
    isLoading: Boolean,
    toMovieDetail: (id: Long) -> Unit
) {
    val width = 140.dp
    LazyRow(
        modifier = Modifier.height(300.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        if (!isLoading) {
            items(
                list.filter { it.imageLink != null }
            ) { movie ->
                PosterItem(
                    modifier = Modifier.width(width)
                        .clickable {
                            toMovieDetail.invoke(movie.id)
                        },
                    imageUrl = movie.imageLink!!, title = movie.title,
                    subtitle = movie.releaseDate,
                    imageAspect = PosterAspect.Poster
                )
            }
        } else {
            items(count = 5) {
                Skeleton(
                    modifier = Modifier.width(width).aspectRatio(PosterAspect.Poster.ratio),
                )
            }
        }
    }
}