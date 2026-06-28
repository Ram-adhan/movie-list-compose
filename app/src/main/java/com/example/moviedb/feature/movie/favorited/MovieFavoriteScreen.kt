package com.example.moviedb.feature.movie.favorited

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import com.example.moviedb.R
import com.example.moviedb.core.ui.components.ImagePlaceholder
import com.example.moviedb.core.ui.components.shimmerBrush
import com.example.moviedb.domain.model.MovieDetail
import com.example.moviedb.feature.movie.PosterAspect

@Composable
fun MovieFavoriteScreen(
    list: List<MovieDetail>,
    toDetail: (id: Long) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (list.isEmpty()) {
            Row(
                modifier = Modifier.align(Alignment.Center),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painterResource(R.drawable.ic_chat_search_light),
                    contentDescription = "Image",
                    modifier = Modifier.size(36.dp)
                )
                Text("No Favorite yet")
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(list) { movie ->
                var isError by remember { mutableStateOf(false) }
                var isLoading by remember { mutableStateOf(false) }

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .clickable{ toDetail(movie.id) },
                ) {
                    ImagePlaceholder(
                        imageUrl = movie.posterPath,
                        contentDescription = movie.title,
                        isError = isError,
                        onState = { state ->
                            isError = state is AsyncImagePainter.State.Error
                            isLoading = state is AsyncImagePainter.State.Loading
                        },
                        modifier = Modifier
                            .height(160.dp)
                            .aspectRatio(PosterAspect.Poster.ratio)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                brush = shimmerBrush(showShimmer = isLoading)
                            )
                    )
                    Column(
                        modifier = Modifier.weight(1f).padding(start = 16.dp)
                    ) {
                        Text(
                            movie.title,
                            style = MaterialTheme.typography.headlineSmall,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            "Release date ${movie.releaseDate}",
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                        )
                        Text(
                            movie.description,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 5,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview_MovieFavoriteScreen() {
    MovieFavoriteScreen(
        toDetail = {},
        list = listOf(
            MovieDetail(
                id = 1,
                title = "Title",
                releaseDate = "20 Jan 2021",
                posterPath = "",
                backdropPath = "",
                "this is description a very very looong long description for lorem ipsum dolor sit amet asdkj asdlkja alskjdl alskjdlasd alskdj"
            ),
            MovieDetail(
                id = 2,
                title = "Title",
                releaseDate = "20 Jan 2021",
                posterPath = "",
                backdropPath = "",
                "this is description a very very looong long description for lorem ipsum dolor sit amet asdkj asdlkja alskjdl alskjdlasd alskdj"
            )
        )
    )
}