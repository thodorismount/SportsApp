package com.mountouris.sportsapp.presentation.mapper

import com.mountouris.sportsapp.domain.model.Sport
import com.mountouris.sportsapp.domain.model.SportEvent
import com.mountouris.sportsapp.presentation.model.SportType
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

private const val SPORT_ID_FOOTBALL = "FOOT"
private const val SPORT_NAME_FOOTBALL = "Football"
private const val EVENT_ID = "EVT_1"
private const val HOME_TEAM = "Panathinaikos"
private const val AWAY_TEAM = "Olympiakos"
private const val START_TIME = 1_700_000_000L

internal class UiMappersTest {

    // region Tests

    @Test
    fun `Sport toUiModel maps id and name correctly`() {
        // given
        val sport = Sport(id = SPORT_ID_FOOTBALL, name = SPORT_NAME_FOOTBALL, events = emptyList())
        // when
        val model = sport.toUiModel()
        // then
        assertEquals(SPORT_ID_FOOTBALL, model.id)
        assertEquals(SPORT_NAME_FOOTBALL, model.name)
    }

    @Test
    fun `Sport toUiModel assigns correct icon for each known sport id`() {
        // given / when / then
        SportType.entries.filter { it != SportType.UNKNOWN }.forEach { sportType ->
            val model = Sport(id = sportType.name, name = sportType.name, events = emptyList()).toUiModel()
            assertEquals(sportType.iconRes, model.icon)
        }
    }

    @Test
    fun `Sport toUiModel assigns null icon for unknown sport id`() {
        // given / when
        val model = Sport(id = "UNKNOWN", name = "Unknown", events = emptyList()).toUiModel()
        // then
        assertNull(model.icon)
    }

    @Test
    fun `Sport toUiModel maps events`() {
        // given
        val event = SportEvent(
            id = EVENT_ID,
            sportId = SPORT_ID_FOOTBALL,
            homeTeam = HOME_TEAM,
            awayTeam = AWAY_TEAM,
            startTime = START_TIME
        )
        val sport = Sport(id = SPORT_ID_FOOTBALL, name = SPORT_NAME_FOOTBALL, events = listOf(event))
        // when
        val model = sport.toUiModel()
        // then
        assertEquals(1, model.events.size)
        assertEquals(EVENT_ID, model.events.first().id)
    }

    @Test
    fun `SportEvent toUiModel maps homeTeam to competitor1 and awayTeam to competitor2`() {
        // given
        val event = SportEvent(
            id = EVENT_ID,
            sportId = SPORT_ID_FOOTBALL,
            homeTeam = HOME_TEAM,
            awayTeam = AWAY_TEAM,
            startTime = START_TIME
        )
        // when
        val model = event.toUiModel()
        // then
        assertEquals(HOME_TEAM, model.competitor1)
        assertEquals(AWAY_TEAM, model.competitor2)
    }

    @Test
    fun `SportEvent toUiModel maps remaining fields correctly`() {
        // given
        val event = SportEvent(
            id = EVENT_ID,
            sportId = SPORT_ID_FOOTBALL,
            homeTeam = HOME_TEAM,
            awayTeam = AWAY_TEAM,
            startTime = START_TIME,
            isFavorite = true
        )
        // when
        val model = event.toUiModel()
        // then
        assertEquals(EVENT_ID, model.id)
        assertEquals(SPORT_ID_FOOTBALL, model.sportId)
        assertEquals(START_TIME, model.startTime)
        assertTrue(model.isFavorite)
    }

    // endregion
}
