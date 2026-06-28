package com.example.moviedb.data.local

import android.content.Context
import com.example.moviedb.data.MovieApi
import com.example.moviedb.data.dto.Configuration
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.days

@Singleton
class TmDBConfigStore @Inject constructor(
    @ApplicationContext context: Context,
    private val movieApi: MovieApi
) {
    companion object {
        private val TTL = 7.days.inWholeMilliseconds
    }
    private val dataStore = context.tmdbMovieConfig

    suspend fun ensureLoaded(): Configuration {
        val cached = dataStore.data.first()
        val isEmpty = cached.images?.secureBaseUrl == null

        return if (isEmpty || isStale(cached)) refresh(cached) else cached
    }

    private fun isStale(configuration: Configuration): Boolean {
        return System.currentTimeMillis() - configuration.lastUpdate > TTL
    }

    private suspend fun refresh(current: Configuration): Configuration {
        val remoteResult = runCatching {
            movieApi.getConfiguration()
        }.mapCatching { result ->
            dataStore.updateData { result.copy(lastUpdate = System.currentTimeMillis()) }
        }.getOrDefault(current)

        return remoteResult
    }
}