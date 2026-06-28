package com.example.moviedb.core

import android.content.Context
import android.content.Intent
import com.example.moviedb.data.dto.MovieResponse
import java.util.Collections.emptyList

object GlobalConstants {
    const val MOVIE_DATE_RESPONSE_FORMAT = "yyyy-MM-dd"
    const val CAROUSEL_DATE_FORMAT = "dd MMMM yyyy"

    fun shareText(context: Context, text: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }
}