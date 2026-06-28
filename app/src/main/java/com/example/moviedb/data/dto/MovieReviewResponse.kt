package com.example.moviedb.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieReviewResponse (
    val author: String? = null,

    @SerialName("author_details")
    val authorDetails: AuthorDetails? = null,

    val content: String? = null,

    @SerialName("created_at")
    val createdAt: String? = null,

    val id: String? = null,

    @SerialName("updated_at")
    val updatedAt: String? = null,

    val url: String? = null
)

@Serializable
data class AuthorDetails (
    val name: String? = null,
    val username: String? = null,

    @SerialName("avatar_path")
    val avatarPath: String? = null,

    val rating: Double? = null
)
