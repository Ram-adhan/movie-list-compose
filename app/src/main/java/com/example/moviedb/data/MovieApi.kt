package com.example.moviedb.data

import com.example.moviedb.data.dto.Configuration
import com.example.moviedb.data.dto.MovieDetailResponse
import com.example.moviedb.data.dto.MovieResponse
import com.example.moviedb.data.dto.MovieReviewResponse
import com.example.moviedb.data.dto.PaginatedResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("movie/popular")
    suspend fun getPopularMovies(): PaginatedResult<MovieResponse>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(): PaginatedResult<MovieResponse>

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(): PaginatedResult<MovieResponse>

    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(
        @Path("movie_id") movieId: Long,
        @Query("page") page: Int? = null
    ): PaginatedResult<MovieReviewResponse>

    @GET("configuration")
    suspend fun getConfiguration(): Configuration

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(@Path("movie_id") id: Long): MovieDetailResponse
}