package com.example.moviedb.data

import com.example.moviedb.core.GlobalConstants
import com.example.moviedb.core.GlobalConstants.CAROUSEL_DATE_FORMAT
import com.example.moviedb.core.toFormattedDate
import com.example.moviedb.data.dto.MovieDetailResponse
import com.example.moviedb.data.dto.MovieResponse
import com.example.moviedb.data.dto.MovieReviewResponse
import com.example.moviedb.data.entity.MovieFavorite
import com.example.moviedb.domain.model.Movie
import com.example.moviedb.domain.model.MovieDetail
import com.example.moviedb.domain.model.MovieReview
import kotlin.random.Random

fun MovieResponse.toDomainModel(imageLink: String): Movie = Movie(
    id = id ?: Random.nextLong(),
    title = title ?: "-",
    imageLink = imageLink,
    releaseDate = releaseDate?.toFormattedDate(
        GlobalConstants.MOVIE_DATE_RESPONSE_FORMAT,
        CAROUSEL_DATE_FORMAT
    ) ?: "-"
)

fun MovieDetailResponse.toDomainModel(posterLink: String, backdropLink: String): MovieDetail =
    MovieDetail(
        id = id!!,
        title = title ?: "-",
        releaseDate = releaseDate?.toFormattedDate(
            GlobalConstants.MOVIE_DATE_RESPONSE_FORMAT,
            CAROUSEL_DATE_FORMAT
        ) ?: "-",
        posterPath = posterLink,
        backdropPath = backdropLink,
        description = overview ?: "-"
    )

fun MovieReviewResponse.toDomainModel(): MovieReview = MovieReview(
    username = author ?: "-",
    avatarPath = authorDetails?.avatarPath?.replaceFirst("/", "") ?: "",
    content = content.orEmpty()
)

fun MovieFavorite.toDomainModelDetail(posterLink: String, backdropLink: String): MovieDetail = MovieDetail(
    id = id,
    title = title,
    releaseDate = releaseDate,
    posterPath = posterLink,
    backdropPath = backdropLink,
    description = overview
)