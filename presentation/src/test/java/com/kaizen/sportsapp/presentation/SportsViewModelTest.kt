package com.kaizen.sportsapp.presentation

import com.kaizen.sportsapp.domain.model.Sport
import com.kaizen.sportsapp.domain.usecase.GetSportsWithFavoritesUC
import com.kaizen.sportsapp.domain.usecase.ToggleFavoriteUC
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.net.ConnectException
import java.net.UnknownHostException
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

private const val SPORT_ID_FOOTBALL = "FOOT"
private const val SPORT_NAME_FOOTBALL = "Football"
private const val EVENT_ID = "EVENT_ID"

@OptIn(ExperimentalCoroutinesApi::class)
internal class SportsViewModelTest {

    // region Mocks

    private val getSportsWithFavoritesUC: GetSportsWithFavoritesUC = mockk()
    private val toggleFavoriteUC: ToggleFavoriteUC = mockk(relaxed = true)

    // endregion

    // region Setup

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // endregion

    // region Tests

    @Test
    fun `initial state has isLoading true before any emission`() {
        // given
        every { getSportsWithFavoritesUC.execute() } returns MutableSharedFlow()
        // when
        val viewModel = SportsViewModel(getSportsWithFavoritesUC, toggleFavoriteUC)
        // then
        assertTrue(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `loadSports on success clears loading and populates sports`() {
        // given
        val domainSports = listOf(sport())
        every { getSportsWithFavoritesUC.execute() } returns flowOf(Result.success(domainSports))
        // when
        val viewModel = SportsViewModel(getSportsWithFavoritesUC, toggleFavoriteUC)
        val state = viewModel.uiState.value
        // then
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertEquals(1, state.sports.size)
        assertEquals(SPORT_ID_FOOTBALL, state.sports.first().id)
    }

    @Test
    fun `loadSports on UnknownHostException sets no internet error message`() {
        // given
        every { getSportsWithFavoritesUC.execute() } returns flowOf(Result.failure(UnknownHostException()))
        // when
        val viewModel = SportsViewModel(getSportsWithFavoritesUC, toggleFavoriteUC)
        val state = viewModel.uiState.value
        // then
        assertFalse(state.isLoading)
        assertEquals("No internet connection. Please check your network.", state.errorMessage)
    }

    @Test
    fun `loadSports on ConnectException sets no internet error message`() {
        // given
        every { getSportsWithFavoritesUC.execute() } returns flowOf(Result.failure(ConnectException()))
        // when
        val viewModel = SportsViewModel(getSportsWithFavoritesUC, toggleFavoriteUC)
        val state = viewModel.uiState.value
        // then
        assertFalse(state.isLoading)
        assertEquals("No internet connection. Please check your network.", state.errorMessage)
    }

    @Test
    fun `loadSports on unknown exception sets generic error message`() {
        // given
        every { getSportsWithFavoritesUC.execute() } returns flowOf(Result.failure(RuntimeException("unexpected")))
        // when
        val viewModel = SportsViewModel(getSportsWithFavoritesUC, toggleFavoriteUC)
        val state = viewModel.uiState.value
        // then
        assertFalse(state.isLoading)
        assertEquals("An unexpected error occurred. Please try again.", state.errorMessage)
    }

    @Test
    fun `retry resets loading state and reloads sports`() {
        // given
        val reloadedSports = listOf(sport())
        every { getSportsWithFavoritesUC.execute() } returnsMany listOf(
            flowOf(Result.failure(RuntimeException("error"))),
            flowOf(Result.success(reloadedSports))
        )
        val viewModel = SportsViewModel(getSportsWithFavoritesUC, toggleFavoriteUC)
        // when
        viewModel.retry()
        val state = viewModel.uiState.value
        // then
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertEquals(1, state.sports.size)
    }

    @Test
    fun `toggleExpanded flips sport expanded state`() {
        // given
        every { getSportsWithFavoritesUC.execute() } returns flowOf(Result.success(listOf(sport())))
        val viewModel = SportsViewModel(getSportsWithFavoritesUC, toggleFavoriteUC)
        assertTrue(viewModel.uiState.value.sports.first().isExpanded)
        // when
        viewModel.toggleExpanded(SPORT_ID_FOOTBALL)
        // then
        assertFalse(viewModel.uiState.value.sports.first().isExpanded)
    }

    @Test
    fun `toggleFavoritesFilter flips sport showFavoritesOnly state`() {
        // given
        every { getSportsWithFavoritesUC.execute() } returns flowOf(Result.success(listOf(sport())))
        val viewModel = SportsViewModel(getSportsWithFavoritesUC, toggleFavoriteUC)
        assertFalse(viewModel.uiState.value.sports.first().showFavoritesOnly)
        // when
        viewModel.toggleFavoritesFilter(SPORT_ID_FOOTBALL)
        // then
        assertTrue(viewModel.uiState.value.sports.first().showFavoritesOnly)
    }

    @Test
    fun `toggleFavorite delegates to use case with correct event id`() {
        // given
        every { getSportsWithFavoritesUC.execute() } returns MutableSharedFlow()
        val viewModel = SportsViewModel(getSportsWithFavoritesUC, toggleFavoriteUC)
        // when
        viewModel.toggleFavorite(EVENT_ID)
        // then
        coVerify(exactly = 1) { toggleFavoriteUC.execute(EVENT_ID) }
    }

    // endregion

    // region Helpers

    private fun sport(id: String = SPORT_ID_FOOTBALL, name: String = SPORT_NAME_FOOTBALL) =
        Sport(id = id, name = name, events = emptyList())

    // endregion
}
