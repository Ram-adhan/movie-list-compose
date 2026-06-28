package com.example.moviedb.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginatedResult<T>(
    @SerialName("dates") val dates: DateRange? = null,
    @SerialName("page") val page: Int? = null,
    @SerialName("results") val results: List<T> = listOf(),
    @SerialName("total_pages") val totalPages: Int? = null,
    @SerialName("total_results") val totalResults: Int? = null
)
