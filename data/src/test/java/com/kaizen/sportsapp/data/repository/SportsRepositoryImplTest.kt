package com.kaizen.sportsapp.data.repository

import app.cash.turbine.test
import com.kaizen.sportsapp.domain.model.Sport
import com.kaizen.sportsapp.local.LocalFavoritesSource
import com.kaizen.sportsapp.remote.SportsApiSource
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private const val EVENT_ID = "EVT_1"
private const val SPORT_ID_FOOTBALL = "FOOT"

internal class SportsRepositoryImplTest {

    // region Mocks

    private val remoteSourceMock: SportsApiSource = mockk()
    private val localSourceMock: LocalFavoritesSource = mockk()

    // endregion

    // region Fields

    private val repository = SportsRepositoryImpl(remoteSourceMock, localSourceMock)

    // endregion

    // region Tests

    @Test
    fun `fetchSports returns success wrapping remote result`() = runTest {
        // given
        val sports = listOf(Sport(id = SPORT_ID_FOOTBALL, name = "Football", events = emptyList()))
        coEvery { remoteSourceMock.fetchSports() } returns sports
        // when
        val result = repository.fetchSports()
        // then
        assertTrue(result.isSuccess)
        assertEquals(sports, result.getOrThrow())
    }

    @Test
    fun `fetchSports returns failure when remote source throws`() = runTest {
        // given
        val exception = RuntimeException("Network error")
        coEvery { remoteSourceMock.fetchSports() } throws exception
        // when
        val result = repository.fetchSports()
        // then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `observeFavoriteIds delegates to local source`() = runTest {
        // given
        val favoriteIds = setOf(EVENT_ID)
        every { localSourceMock.observeFavoriteIds() } returns flowOf(favoriteIds)
        // when / then
        repository.observeFavoriteIds().test {
            assertEquals(favoriteIds, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggleFavorite delegates to local source with correct event id`() = runTest {
        // given
        coJustRun { localSourceMock.toggleFavorite(EVENT_ID) }
        // when
        repository.toggleFavorite(EVENT_ID)
        // then
        coVerify(exactly = 1) { localSourceMock.toggleFavorite(EVENT_ID) }
    }

    // endregion
}
