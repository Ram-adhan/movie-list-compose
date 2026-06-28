package com.example.moviedb.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.example.moviedb.core.appJson
import com.example.moviedb.data.dto.Configuration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream

object ConfigurationDataStoreSerializer: Serializer<Configuration> {
    override suspend fun readFrom(input: InputStream): Configuration {
        return try {
            appJson.decodeFromString<Configuration>(input.readBytes().decodeToString())
        } catch (_: Exception) {
            defaultValue
        }
    }

    override suspend fun writeTo(
        t: Configuration,
        output: OutputStream
    ) {
        withContext(Dispatchers.IO) {
            output.write(appJson.encodeToString(t).encodeToByteArray())
        }
    }

    override val defaultValue: Configuration = Configuration()
}

val Context.tmdbMovieConfig: DataStore<Configuration> by dataStore(
    fileName = "tmdbConfig.json",
    serializer = ConfigurationDataStoreSerializer
)