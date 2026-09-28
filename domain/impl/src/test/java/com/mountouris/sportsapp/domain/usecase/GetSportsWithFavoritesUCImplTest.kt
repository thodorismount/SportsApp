package com.mountouris.sportsapp.domain.usecase

import app.cash.turbine.test
import com.mountouris.sportsapp.domain.model.Sport
import com.mountouris.sportsapp.domain.model.SportEvent
import com.mountouris.sportsapp.domain.repository.SportsRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private const val SPORT_ID_FOOTBALL = "FOOT"
private const val SPORT_ID_BASKETBALL = "BASK"

internal class GetSportsWithFavoritesUCImplTest {

    // region Mocks

    private val repositoryMock: SportsRepository = mockk()

    // endregion

    // region Fields

    private val useCase: GetSportsWithFavoritesUC = GetSportsWithFavoritesUCImpl(repositoryMock)

    // endregion

    // region Tests

    @Test
    fun `execute emits failure when fetch sports fails`() = runTest {
        // given
        val exception = RuntimeException("Network error")
        coEvery { repositoryMock.fetchSports() } returns Result.failure(exception)
        // when / then
        useCase.execute().test {
            val result = awaitItem()
            assertTrue(result.isFailure)
            assertEquals(exception, result.exceptionOrNull())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `execute emits sports with no favorites when favorite ids are empty`() = runTest {
        // given
        val sports = listOf(
            sport(id = SPORT_ID_FOOTBALL, events = listOf(event(id = "1"), event(id = "2")))
        )
        coEvery { repositoryMock.fetchSports() } returns Result.success(sports)
        every { repositoryMock.observeFavoriteIds() } returns flowOf(emptySet())
        // when / then
        useCase.execute().test {
            val result = awaitItem().getOrThrow()
            assertEquals(1, result.size)
            result.first().events.forEach { assertFalse(it.isFavorite) }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `execute marks only matching event ids as favorite`() = runTest {
        // given
        val sports = listOf(
            sport(id = SPORT_ID_FOOTBALL, events = listOf(event(id = "1"), event(id = "2"), event(id = "3")))
        )
        coEvery { repositoryMock.fetchSports() } returns Result.success(sports)
        every { repositoryMock.observeFavoriteIds() } returns flowOf(setOf("1", "3"))
        // when / then
        useCase.execute().test {
            val events = awaitItem().getOrThrow().first().events
            assertEquals(3, events.size)
            assertTrue(events[0].isFavorite)
            assertFalse(events[1].isFavorite)
            assertTrue(events[2].isFavorite)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `execute applies favorites independently across multiple sports`() = runTest {
        // given
        val sports = listOf(
            sport(id = SPORT_ID_FOOTBALL, events = listOf(event(id = "1"), event(id = "2"))),
            sport(id = SPORT_ID_BASKETBALL, events = listOf(event(id = "3"), event(id = "4")))
        )
        coEvery { repositoryMock.fetchSports() } returns Result.success(sports)
        every { repositoryMock.observeFavoriteIds() } returns flowOf(setOf("2", "3"))
        // when / then
        useCase.execute().test {
            val result = awaitItem().getOrThrow()
            val footballEvents = result[0].events
            assertFalse(footballEvents[0].isFavorite)
            assertTrue(footballEvents[1].isFavorite)
            val basketballEvents = result[1].events
            assertTrue(basketballEvents[0].isFavorite)
            assertFalse(basketballEvents[1].isFavorite)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `execute emits updated favorites when observeFavoriteIds emits a new set`() = runTest {
        // given
        val sports = listOf(
            sport(id = SPORT_ID_FOOTBALL, events = listOf(event(id = "1"), event(id = "2")))
        )
        coEvery { repositoryMock.fetchSports() } returns Result.success(sports)
        every { repositoryMock.observeFavoriteIds() } returns flowOf(emptySet(), setOf("1"))
        // when / then
        useCase.execute().test {
            val firstEmission = awaitItem().getOrThrow().first().events
            assertFalse(firstEmission[0].isFavorite)
            assertFalse(firstEmission[1].isFavorite)

            val secondEmission = awaitItem().getOrThrow().first().events
            assertTrue(secondEmission[0].isFavorite)
            assertFalse(secondEmission[1].isFavorite)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `execute emits sports with no favorites when observeFavoriteIds throws`() = runTest {
        // given
        val sports = listOf(
            sport(id = SPORT_ID_FOOTBALL, events = listOf(event(id = "1"), event(id = "2")))
        )
        coEvery { repositoryMock.fetchSports() } returns Result.success(sports)
        every { repositoryMock.observeFavoriteIds() } returns flow { throw RuntimeException("DB error") }
        // when / then
        useCase.execute().test {
            val events = awaitItem().getOrThrow().first().events
            // then — catch block swallows the error and falls back to empty favorites
            assertEquals(2, events.size)
            events.forEach { assertFalse(it.isFavorite) }
            cancelAndIgnoreRemainingEvents()
        }
    }

    // endregion

    // region Helpers

    private fun sport(id: String, events: List<SportEvent> = emptyList()) =
        Sport(id = id, name = id, events = events)

    private fun event(id: String) =
        SportEvent(id = id, sportId = SPORT_ID_FOOTBALL, name = "Panathinaikos-Olympiakos", startTime = 1_700_000_000L)

    // endregion
}
