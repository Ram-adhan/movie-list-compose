package com.example.moviedb.feature.movie.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import com.example.moviedb.R
import com.example.moviedb.core.ui.components.ImagePlaceholder
import com.example.moviedb.domain.model.MovieDetail
import com.example.moviedb.domain.model.MovieReview
import com.example.moviedb.feature.movie.PosterAspect

@Composable
fun MovieDetailScreen(
    movieDetail: MovieDetail,
    reviews: List<MovieReview>?,
) {
    var isError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ImagePlaceholder(
            movieDetail.backdropPath,
            contentDescription = "Image",
            isError = isError,
            onState = { state ->
                isError = state is AsyncImagePainter.State.Error
            },
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(PosterAspect.BackdropExt.ratio)
        )

        Text(
            movieDetail.title,
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp)
        )

        Text(
            "Released at ${movieDetail.releaseDate}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(horizontal = 16.dp)
        )

        Text(
            "Description",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .padding(top = 36.dp)
                .padding(horizontal = 16.dp)
        )
        Text(
            movieDetail.description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(horizontal = 16.dp)
        )

        Text(
            "Reviews",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .padding(top = 36.dp)
                .padding(horizontal = 16.dp)
        )

        if (reviews?.isEmpty() == true) {
            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painterResource(R.drawable.ic_chat_search_light),
                    contentDescription = "Image",
                    modifier = Modifier.size(36.dp)
                )
                Text("No Review yet")
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .padding(horizontal = 16.dp)
                    .wrapContentSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                reviews?.forEach { review ->
                    var isError by remember { mutableStateOf(false) }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                    ) {
                        ImagePlaceholder(
                            review.avatarPath,
                            contentDescription = review.username,
                            isError = isError,
                            onState = { state ->
                                isError = state is AsyncImagePainter.State.Error
                            },
                            modifier = Modifier.size(32.dp)
                        )
                        Column(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .fillMaxWidth()
                        ) {
                            Text(review.username,
                                style = MaterialTheme.typography.titleMedium)

                            Text(review.content,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(top = 4.dp))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview_MovieDetailScreen() {
    MovieDetailScreen(
        MovieDetail(
            1L,
            "Title",
            "20 Januari 2020",
            "",
            "",
            "This is description"
        ),
        reviews = listOf(
            MovieReview(
                "username",
                "",
                "This is a review"
            ),
            MovieReview(
                "username",
                "",
                "This is a review"
            ),
            MovieReview(
                "username",
                "",
                "This is a review"
            ),
            MovieReview(
                "username",
                "",
                "This is a review"
            ),
            MovieReview(
                "username",
                "",
                "This is a review"
            )
        ),
    )
}