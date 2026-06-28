package com.example.moviedb.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import com.example.moviedb.R

@Composable
fun ImagePlaceholder(
    imageUrl: String,
    contentDescription: String?,
    isError: Boolean,
    onState: (AsyncImagePainter.State) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            onState = onState
        )

        if (isError) {
            Icon(
                painterResource(R.drawable.ic_img_box_fill_light),
                contentDescription = "Failed to load",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(32.dp),
                tint = Color.Gray
            )
        }
    }
}