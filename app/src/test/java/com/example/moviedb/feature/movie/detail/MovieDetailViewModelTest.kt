package com.example.moviedb.feature.movie.detail

import app.cash.turbine.test
import com.example.moviedb.MainDispatcherRule
import com.example.moviedb.domain.MovieRepository
import com.example.moviedb.domain.model.MovieDetail
import com.example.moviedb.domain.model.MovieReview
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK(relaxed = true)
    lateinit var movieRepository: MovieRepository
    private lateinit var viewModel: MovieDetailViewModel

    private val movieDetail = MovieDetail(
        id = 1L,
        title = "Inception",
        description = "A mind-bending heist thriller.",
        releaseDate = "25 01 2005",
        posterPath = "",
        backdropPath = ""
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = MovieDetailViewModel(movieRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getDetail - sets Detail loading key then clears it on completion`() = runTest {
        every { movieRepository.getMovieDetail(1L) } returns flowOf(Result.success(movieDetail))

        viewModel.state.test {
            assertEquals(MovieDetailState(), awaitItem())

            viewModel.getDetail(1L)

            val loadingState = awaitItem()
            assertTrue(MovieDetailState.Companion.LoadingKeys.Detail in loadingState.loadingKeys)

            val successState = awaitItem()
            assertTrue(MovieDetailState.Companion.LoadingKeys.Detail in successState.loadingKeys)
            assertEquals(movieDetail, successState.movieDetail)

            val completedState = awaitItem()
            assertFalse(MovieDetailState.Companion.LoadingKeys.Detail in completedState.loadingKeys)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getDetail - success builds correct shareText`() = runTest {
        every { movieRepository.getMovieDetail(1L) } returns flowOf(Result.success(movieDetail))

        viewModel.getDetail(1L)
        advanceUntilIdle()

        val expectedShareText = """
            Check this movie!
            
            ${movieDetail.title}
            
            ${movieDetail.description}
        """.trimIndent()

        assertEquals(expectedShareText, viewModel.state.value.shareText)
    }

    @Test
    fun `getDetail - failure sets error and clears loading key`() = runTest {
        val exception = RuntimeException("network down")
        every { movieRepository.getMovieDetail(1L) } returns flowOf(Result.failure(exception))

        viewModel.getDetail(1L)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("Oops" to "network down", state.error)
        assertFalse(MovieDetailState.Companion.LoadingKeys.Detail in state.loadingKeys)
        assertNull(state.movieDetail)
    }

    @Test
    fun `getDetail - does not overwrite existing error`() = runTest {
        val firstError = RuntimeException("first error")
        val secondError = RuntimeException("second error")

        every { movieRepository.getMovieDetail(1L) } returns flowOf(Result.failure(firstError))
        viewModel.getDetail(1L)
        advanceUntilIdle()

        every { movieRepository.getMovieDetail(1L) } returns flowOf(Result.failure(secondError))
        viewModel.getDetail(1L)
        advanceUntilIdle()

        assertEquals("Oops" to "first error", viewModel.state.value.error)
    }

    @Test
    fun `getReview - sets Review loading key then clears it on success`() = runTest {
        val reviews = listOf(MovieReview("Alice", content = "Great movie!", avatarPath = ""))
        coEvery { movieRepository.getMovieReview(1L) } returns Result.success(reviews)

        viewModel.state.test {
            assertEquals(MovieDetailState(), awaitItem())

            viewModel.getReview(1L)

            val loadingState = awaitItem()
            assertTrue(MovieDetailState.Companion.LoadingKeys.Review in loadingState.loadingKeys)

            val successState = awaitItem()
            assertEquals(reviews, successState.reviews)
            assertTrue(MovieDetailState.Companion.LoadingKeys.Review in successState.loadingKeys)

            val completeState = awaitItem()
            assertFalse(MovieDetailState.Companion.LoadingKeys.Review in completeState.loadingKeys)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getReview - failure sets error and clears loading key`() = runTest {
        val exception = RuntimeException("server error")
        coEvery { movieRepository.getMovieReview(1L) } returns Result.failure(exception)

        viewModel.getReview(1L)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("Oops" to "server error", state.error)
        assertFalse(MovieDetailState.Companion.LoadingKeys.Review in state.loadingKeys)
        assertNull(state.reviews)
    }

    @Test
    fun `isInFavorite - success sets isFavorite true`() = runTest {
        coEvery { movieRepository.isInFavorite(1L) } returns Result.success(movieDetail)

        viewModel.isInFavorite(1L)
        advanceUntilIdle()

        assertTrue(viewModel.state.value.isFavorite)
    }

    @Test
    fun `isInFavorite - failure sets isFavorite false when it was true`() = runTest {
        coEvery { movieRepository.isInFavorite(1L) } returns Result.success(movieDetail)
        viewModel.isInFavorite(1L)
        advanceUntilIdle()
        assertTrue(viewModel.state.value.isFavorite)

        coEvery { movieRepository.isInFavorite(1L) } returns Result.failure(RuntimeException("not found"))
        viewModel.isInFavorite(1L)
        advanceUntilIdle()

        assertFalse(viewModel.state.value.isFavorite)
    }

    @Test
    fun `isInFavorite - failure does not set error (silently handled)`() = runTest {
        coEvery { movieRepository.isInFavorite(1L) } returns Result.failure(RuntimeException("not found"))

        viewModel.isInFavorite(1L)
        advanceUntilIdle()

        assertNull(viewModel.state.value.error)
    }

    @Test
    fun `toggleToFavorite - does nothing if movieDetail is null`() = runTest {
        viewModel.toggleToFavorite()
        advanceUntilIdle()

        coVerify(exactly = 0) { movieRepository.addToFavorite(any()) }
        coVerify(exactly = 0) { movieRepository.removeFromFavorite(any()) }
    }

    @Test
    fun `toggleToFavorite - adds to favorite when not currently favorite`() = runTest {
        every { movieRepository.getMovieDetail(1L) } returns flowOf(Result.success(movieDetail))
        viewModel.getDetail(1L)
        advanceUntilIdle()
        assertFalse(viewModel.state.value.isFavorite)

        coEvery { movieRepository.addToFavorite(movieDetail) } returns Result.success(true)
        coEvery { movieRepository.isInFavorite(movieDetail.id) } returns Result.success(movieDetail)

        viewModel.toggleToFavorite()
        advanceUntilIdle()

        coVerify(exactly = 1) { movieRepository.addToFavorite(movieDetail) }
        coVerify(exactly = 0) { movieRepository.removeFromFavorite(any()) }
        assertTrue(viewModel.state.value.isFavorite)
    }

    @Test
    fun `toggleToFavorite - removes from favorite when currently favorite`() = runTest {
        every { movieRepository.getMovieDetail(1L) } returns flowOf(Result.success(movieDetail))
        viewModel.getDetail(1L)
        advanceUntilIdle()

        coEvery { movieRepository.isInFavorite(movieDetail.id) } returns Result.success(movieDetail)
        viewModel.isInFavorite(movieDetail.id)
        advanceUntilIdle()
        assertTrue(viewModel.state.value.isFavorite)

        coEvery { movieRepository.removeFromFavorite(movieDetail.id) } returns Result.success(true)
        coEvery { movieRepository.isInFavorite(movieDetail.id) } returns Result.failure(NullPointerException())

        viewModel.toggleToFavorite()
        advanceUntilIdle()

        coVerify(exactly = 1) { movieRepository.removeFromFavorite(movieDetail.id) }
        coVerify(exactly = 0) { movieRepository.addToFavorite(any()) }
        assertFalse(viewModel.state.value.isFavorite)
    }

    @Test
    fun `dismissError - clears error from state`() = runTest {
        every { movieRepository.getMovieDetail(1L) } returns flowOf(Result.failure(RuntimeException("boom")))
        viewModel.getDetail(1L)
        advanceUntilIdle()
        assertNotNull(viewModel.state.value.error)

        viewModel.dismissError()

        assertNull(viewModel.state.value.error)
    }
}