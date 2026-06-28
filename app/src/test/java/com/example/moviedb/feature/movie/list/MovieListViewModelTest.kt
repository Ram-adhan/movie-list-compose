package com.example.moviedb.feature.movie.list

import app.cash.turbine.test
import com.example.moviedb.MainDispatcherRule
import com.example.moviedb.domain.MovieRepository
import com.example.moviedb.domain.model.Movie
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException

@OptIn(ExperimentalCoroutinesApi::class)
class MovieListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    lateinit var underTest: MovieListViewModel

    @MockK(relaxed = true)
    lateinit var movieRepository: MovieRepository


    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        underTest = MovieListViewModel(movieRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `loadAll sets all state to Loading`() = runTest {
        underTest.state.test {
            assertEquals(MovieListScreenState(), awaitItem())

            underTest.loadAll()

            val state2 = awaitItem()
            assert(state2.popularMovies == MovieListState.Loading)
            assert(state2.topRatedMovies == MovieListState.Loading)
            assert(state2.nowPlaying == MovieListState.Loading)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadAll eventually loads all three categories successfully`() = runTest {
        val popular = listOf(
            Movie(1, "Title1", "https://some.url/img.jpg", "22 Jan 2025"),
            Movie(2, "Title2", "https://some.url/img.jpg", "22 Jan 2025")
        )
        val topRated = listOf(
            Movie(3, "Title3", "https://some.url/img.jpg", "22 Jan 2025"),
            Movie(4, "Title4", "https://some.url/img.jpg", "22 Jan 2025")
        )
        val nowPlaying = listOf(
            Movie(5, "Title5", "https://some.url/img.jpg", "22 Jan 2025"),
            Movie(6, "Title6", "https://some.url/img.jpg", "22 Jan 2025")
        )
        every { movieRepository.getPopularMovies(any(), any()) } returns flowOf(
            Result.success(
                popular
            )
        )

        every {
            movieRepository.getTopRatedMovies(any())
        } returns flowOf(
            Result.success(topRated)
        )

        every {
            movieRepository.getNowPlayingMovies(any())
        } returns flowOf(
            Result.success(nowPlaying)
        )

        underTest.loadAll()
        advanceUntilIdle()

        val finalState = underTest.state.value
        assertEquals(popular, (finalState.popularMovies as MovieListState.Finish).list)
        assertEquals(topRated, (finalState.topRatedMovies as MovieListState.Finish).list)
        assertEquals(nowPlaying, (finalState.nowPlaying as MovieListState.Finish).list)
    }

    @Test
    fun `loadAll - one failure does not affect other categories`() = runTest {

        val topRated = listOf(
            Movie(3, "Title3", "https://some.url/img.jpg", "22 Jan 2025"),
            Movie(4, "Title4", "https://some.url/img.jpg", "22 Jan 2025")
        )
        val nowPlaying = listOf(
            Movie(5, "Title5", "https://some.url/img.jpg", "22 Jan 2025"),
            Movie(6, "Title6", "https://some.url/img.jpg", "22 Jan 2025")
        )
        every { movieRepository.getPopularMovies(any(), any()) } returns flowOf(
            Result.failure(Throwable("Network Error"))
        )

        every {
            movieRepository.getTopRatedMovies(any())
        } returns flowOf(
            Result.success(topRated)
        )

        every {
            movieRepository.getNowPlayingMovies(any())
        } returns flowOf(
            Result.success(nowPlaying)
        )

        underTest.loadAll()
        advanceUntilIdle()

        val finalState = underTest.state.value
        assertEquals(MovieListState.Loading, finalState.popularMovies)
        assertEquals(topRated, (finalState.topRatedMovies as MovieListState.Finish).list)
        assertEquals(nowPlaying, (finalState.nowPlaying as MovieListState.Finish).list)
    }

}