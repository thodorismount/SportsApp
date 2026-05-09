package com.kaizen.sportsapp.domain.usecase

import com.kaizen.sportsapp.domain.repository.SportsRepository
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

private const val EVENT_ID_FOOTBALL_MATCH = "FOOT_1"

internal class ToggleFavoriteUCImplTest {

    // region Mocks

    private val repositoryMock: SportsRepository = mockk(relaxed = true)

    // endregion

    // region Fields

    private val useCase: ToggleFavoriteUC = ToggleFavoriteUCImpl(repositoryMock)

    // endregion

    // region Tests

    @Test
    fun `execute delegates to repository with the correct event id`() = runTest {
        // when
        useCase.execute(EVENT_ID_FOOTBALL_MATCH)
        // then
        coVerify(exactly = 1) { repositoryMock.toggleFavorite(EVENT_ID_FOOTBALL_MATCH) }
    }

    // endregion
}
