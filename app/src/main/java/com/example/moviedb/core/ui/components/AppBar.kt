package com.example.moviedb.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moviedb.R
import com.example.moviedb.core.ui.theme.Purple40
import com.example.moviedb.core.ui.theme.darken

@Composable
fun AppBar(
    title: String,
    modifier: Modifier = Modifier,
    scrollFraction: Float = 1f,
    onBackClick: (() -> Unit)? = null,
    leftView: (@Composable (contentColor: Color) -> Unit)? = null,
    backgroundColor: Color = Purple40,
    contentColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Column{
        Box(
            modifier.background(backgroundColor)
                .fillMaxWidth()
                .statusBarsPadding()
        )
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(0.7f),
                            Color.Transparent,
                        ),
                    )
                )
                .background(backgroundColor.copy(alpha = scrollFraction))
                .height(56.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            onBackClick?.let {
                IconButton(onClick = it) {
                    Icon(
                        painterResource(R.drawable.chevron_left_lg),
                        contentDescription = "Back",
                        tint = contentColor
                    )
                }
            }

            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = contentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp, end = 16.dp) // balances the leading IconButton's width visually
            )

            leftView?.invoke(contentColor)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview_AppBar() {
    AppBar(
        title = "title",
        onBackClick = {},
        scrollFraction = 0f
    )
}