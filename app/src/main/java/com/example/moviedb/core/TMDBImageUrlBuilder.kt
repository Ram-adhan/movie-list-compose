package com.example.moviedb.core

import com.example.moviedb.data.dto.MovieResponse
import com.example.moviedb.data.local.TmDBConfigStore
import com.example.moviedb.domain.model.ImageContext
import com.example.moviedb.domain.model.ImageSize
import javax.inject.Inject

class TMDBImageUrlBuilder @Inject constructor(
    private val tmDBConfigStore: TmDBConfigStore
) {
    suspend fun resolveUrl(url: String, type: ImageContext, size: ImageSize): String {
        if (url.isBlank()) return url
        val size: String = when (type) {
            ImageContext.POSTER -> posterDesiredWidth(size)
            ImageContext.BACKDROP -> backdropDesiredWidth(size)
        }

        val baseUrl = tmDBConfigStore.ensureLoaded().images?.secureBaseUrl

        return "${baseUrl}$size$url"
    }

    private fun posterDesiredWidth(size: ImageSize): String = when (size) {
        ImageSize.SMALL -> "w185"
        ImageSize.MEDIUM -> "w342"
        ImageSize.BIG -> "w500"
        ImageSize.ORIGINAL -> "original"
    }

    private fun backdropDesiredWidth(size: ImageSize): String = when (size) {
        ImageSize.SMALL, ImageSize.MEDIUM -> "w780"
        ImageSize.BIG -> "w1280"
        ImageSize.ORIGINAL -> "original"
    }
}