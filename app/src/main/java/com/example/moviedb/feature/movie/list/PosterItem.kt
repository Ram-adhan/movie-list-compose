package com.example.moviedb.feature.movie.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import com.example.moviedb.core.ui.components.ImagePlaceholder
import com.example.moviedb.core.ui.components.shimmerBrush
import com.example.moviedb.feature.movie.PosterAspect

@Composable
fun PosterItem(
    imageUrl: String,
    title: String,
    subtitle: String,
    imageAspect: PosterAspect,
    modifier: Modifier = Modifier
) {
    var isLoading by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
    ) {
        ImagePlaceholder(
            imageUrl,
            title,
            isError,
            onState = { state ->
                isLoading = state is AsyncImagePainter.State.Loading
                isError = state is AsyncImagePainter.State.Error
            },
            modifier = Modifier.fillMaxWidth()
                .aspectRatio(imageAspect.ratio)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    brush = shimmerBrush(showShimmer = isLoading)
                )
        )

        Text(
            title,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
                .padding(top = 8.dp, bottom = 4.dp)
        )

        Text(
            subtitle,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 8.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun Preview_PosterItem() {
    PosterItem(
        "",
        "Title",
        "Subtitle",
        PosterAspect.Poster
    )
}