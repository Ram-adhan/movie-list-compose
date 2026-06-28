package com.example.moviedb.core

import android.content.Context
import kotlinx.serialization.json.Json

val appJson by lazy {
    Json {
        allowStructuredMapKeys = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
}