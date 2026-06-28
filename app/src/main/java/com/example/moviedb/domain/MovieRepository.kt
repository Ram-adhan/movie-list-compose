package com.example.moviedb.domain

import com.example.moviedb.domain.model.ImageContext
import com.example.moviedb.domain.model.ImageSize
import com.example.moviedb.domain.model.Movie
import com.example.moviedb.domain.model.MovieDetail
import com.example.moviedb.domain.model.MovieReview
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getPopularMovies(imageContext: ImageContext, imageSize: ImageSize): Flow<Result<List<Movie>>>
    suspend fun getTopRatedMovies(imageSize: ImageSize): Flow<Result<List<Movie>>>
    suspend fun getNowPlayingMovies(imageSize: ImageSize): Flow<Result<List<Movie>>>
    suspend fun getMovieDetail(id: Long): Flow<Result<MovieDetail>>
    suspend fun getMovieReview(id: Long): Result<List<MovieReview>>
    suspend fun addToFavorite(movieDetail: MovieDetail): Result<Boolean>
    suspend fun removeFromFavorite(id: Long): Result<Boolean>
    suspend fun isInFavorite(id: Long): Result<MovieDetail>
    suspend fun getAllFavorite(): Flow<List<MovieDetail>>
}