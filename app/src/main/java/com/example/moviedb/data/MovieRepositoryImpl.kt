package com.example.moviedb.data

import com.example.moviedb.core.TMDBImageUrlBuilder
import com.example.moviedb.core.error.GenericError
import com.example.moviedb.data.dto.MovieDetailResponse
import com.example.moviedb.data.dto.MovieResponse
import com.example.moviedb.data.entity.MovieFavorite
import com.example.moviedb.data.entity.MovieFavoriteDao
import com.example.moviedb.domain.MovieRepository
import com.example.moviedb.domain.model.ImageContext
import com.example.moviedb.domain.model.ImageSize
import com.example.moviedb.domain.model.Movie
import com.example.moviedb.domain.model.MovieDetail
import com.example.moviedb.domain.model.MovieReview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.let

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieFavoriteDao: MovieFavoriteDao,
    private val imageUrlBuilder: TMDBImageUrlBuilder
): MovieRepository {
    private var _cachedPopularMovies: List<MovieResponse> = emptyList()
    private var _cachedTopRatedMovies: List<MovieResponse> = emptyList()
    private var _cachedNowPlayingMovies: List<MovieResponse> = emptyList()
    private val _cachedMoviesDetail: MutableMap<Long, MovieDetailResponse> = mutableMapOf()

    override fun getPopularMovies(
        imageContext: ImageContext,
        imageSize: ImageSize
    ): Flow<Result<List<Movie>>> = flow {
        if (_cachedPopularMovies.isNotEmpty()) {
            emit(
                Result.success(
                    _cachedPopularMovies.map {
                        val imageUrl = imageUrlBuilder.resolveUrl(
                            when(imageContext) {
                                ImageContext.POSTER -> it.posterPath
                                ImageContext.BACKDROP -> it.backdropPath
                            }.orEmpty(),
                            type = imageContext,
                            size = imageSize
                        )
                        it.toDomainModel(imageUrl)
                    }
                )
            )
        }
        val remoteResult = runCatching {
            movieApi.getPopularMovies()
        }.mapCatching { response ->
            _cachedPopularMovies = response.results
            val result = response.results.map {
                val imageUrl = imageUrlBuilder.resolveUrl(
                    when(imageContext) {
                        ImageContext.POSTER -> it.posterPath
                        ImageContext.BACKDROP -> it.backdropPath
                    }.orEmpty(),
                    type = imageContext,
                    size = imageSize
                )
                it.toDomainModel(imageUrl)
            }
            result
        }


        emit(remoteResult)
    }

    override fun getTopRatedMovies(imageSize: ImageSize): Flow<Result<List<Movie>>> = flow {
        if (_cachedTopRatedMovies.isNotEmpty()) {
            emit(
                Result.success(
                    _cachedTopRatedMovies.map {
                        val imageUrl = imageUrlBuilder.resolveUrl(
                            it.posterPath.orEmpty(),
                            type = ImageContext.POSTER,
                            size = imageSize
                        )
                        it.toDomainModel(imageUrl)
                    }
                )
            )
        }
        val remoteResult = runCatching {
            movieApi.getTopRatedMovies()
        }.mapCatching { response ->
            _cachedTopRatedMovies = response.results
            response.results.map {
                val imageUrl = imageUrlBuilder.resolveUrl(
                    it.posterPath.orEmpty(),
                    type = ImageContext.POSTER,
                    size = imageSize
                )
                it.toDomainModel(imageUrl)
            }
        }

        emit(remoteResult)
    }

    override fun getNowPlayingMovies(imageSize: ImageSize): Flow<Result<List<Movie>>> = flow {
        if (_cachedNowPlayingMovies.isNotEmpty()) {
            emit(
                Result.success(
                    _cachedNowPlayingMovies.map {
                        val imageUrl = imageUrlBuilder.resolveUrl(
                            it.posterPath.orEmpty(),
                            type = ImageContext.POSTER,
                            size = imageSize
                        )
                        it.toDomainModel(imageUrl)
                    }
                )
            )
        }
        val remoteResult = runCatching {
            movieApi.getNowPlayingMovies()
        }.mapCatching { response ->
            _cachedNowPlayingMovies = response.results
            response.results.map {
                val imageUrl = imageUrlBuilder.resolveUrl(
                    it.posterPath.orEmpty(),
                    type = ImageContext.POSTER,
                    size = imageSize
                )
                it.toDomainModel(imageUrl)
            }
        }

        emit(remoteResult)
    }

    override fun getMovieDetail(id: Long): Flow<Result<MovieDetail>> = flow {
        _cachedMoviesDetail[id]?.let { movie ->
            emit(
                Result.success(
                    movie.toDomainModel(
                        posterLink = imageUrlBuilder.resolveUrl(
                            movie.posterPath.orEmpty(),
                            type = ImageContext.POSTER,
                            size = ImageSize.MEDIUM
                        ),
                        backdropLink = imageUrlBuilder.resolveUrl(
                            movie.backdropPath.orEmpty(),
                            type = ImageContext.BACKDROP,
                            size = ImageSize.BIG
                        ),
                    )
                )
            )
        }

        val remoteResult: Result<MovieDetail> = runCatching {
            movieApi.getMovieDetail(id)
        }.mapCatching { response ->
            response.id?.let {
                _cachedMoviesDetail[it] = response
            }
            response.toDomainModel(
                posterLink = imageUrlBuilder.resolveUrl(
                    response.posterPath.orEmpty(),
                    type = ImageContext.POSTER,
                    size = ImageSize.MEDIUM
                ),
                backdropLink = imageUrlBuilder.resolveUrl(
                    response.backdropPath.orEmpty(),
                    type = ImageContext.BACKDROP,
                    size = ImageSize.BIG
                ),
            )
        }.recoverCatching {
            if (it is HttpException) throw GenericError()
            else throw it
        }

        emit(remoteResult)
    }

    override suspend fun getMovieReview(id: Long): Result<List<MovieReview>> {
        val remoteResult = runCatching {
            movieApi.getMovieReviews(id)
        }.mapCatching { response ->
            response.results.map { it.toDomainModel() }
        }.recoverCatching {
            if (it is HttpException) throw GenericError()
            else throw it
        }

        return remoteResult
    }

    override suspend fun isInFavorite(id: Long): Result<MovieDetail> {
        return runCatching {
            movieFavoriteDao.getFavorite(id)!!.toDomainModelDetail("", "")
        }
    }

    override suspend fun addToFavorite(movieDetail: MovieDetail): Result<Boolean> {
        return runCatching {
            val data = _cachedMoviesDetail[movieDetail.id]!!
            movieFavoriteDao.upsert(
                MovieFavorite(
                    id = data.id!!,
                    title = data.title!!,
                    releaseDate = data.releaseDate!!,
                    overview = data.overview!!,
                    posterPath = data.posterPath!!,
                    backdropPath = data.backdropPath!!
                )
            )
            true
        }
    }

    override suspend fun removeFromFavorite(id: Long): Result<Boolean> {
        return runCatching {
            movieFavoriteDao.deleteFavorite(id)
            true
        }
    }

    override  fun getAllFavorite(): Flow<List<MovieDetail>> = flow {
        emit(
            runCatching {
                movieFavoriteDao.getAllFavorite()
            }.mapCatching { result ->
                result.map {
                    it.toDomainModelDetail(
                        posterLink = imageUrlBuilder.resolveUrl(
                            it.posterPath,
                            type = ImageContext.POSTER,
                            size = ImageSize.MEDIUM
                        ),
                        backdropLink = imageUrlBuilder.resolveUrl(
                            it.backdropPath,
                            type = ImageContext.BACKDROP,
                            size = ImageSize.MEDIUM
                        )
                    )
                }
            }.getOrDefault(emptyList())
        )
    }
}