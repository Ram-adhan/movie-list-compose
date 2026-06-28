package com.example.moviedb.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun BaseLoader(
    showLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    if (showLoading) {
        Box(
            modifier = modifier.then(Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
            ),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}