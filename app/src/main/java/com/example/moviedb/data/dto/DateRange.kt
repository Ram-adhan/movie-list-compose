package com.example.moviedb.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DateRange(
    @SerialName("minimum") val minimum: String? = null,
    @SerialName("maximum") val maximum: String? = null,
)
