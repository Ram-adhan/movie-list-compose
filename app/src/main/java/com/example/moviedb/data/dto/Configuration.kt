package com.example.moviedb.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Configuration(
    @SerialName("images") val images: Images? = null,
    @SerialName("change_keys") val changeKeys: List<String>? = null,
    val lastUpdate: Long = 0L
)


@Serializable
data class Images(
    @SerialName("secure_base_url") val secureBaseUrl: String? = null,
    @SerialName("backdrop_sizes") val backdropSizes: List<String> = listOf(),
    @SerialName("logo_sizes") val logoSizes: List<String> = listOf(),
    @SerialName("poster_sizes") val posterSizes: List<String> = listOf(),
    @SerialName("profile_sizes") val profileSizes: List<String> = listOf(),
    @SerialName("still_sizes") val stillSizes: List<String> = listOf()

)