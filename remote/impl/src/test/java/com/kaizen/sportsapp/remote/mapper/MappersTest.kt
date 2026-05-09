package com.kaizen.sportsapp.remote.mapper

import com.kaizen.sportsapp.remote.dto.EventDto
import com.kaizen.sportsapp.remote.dto.SportDto
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

private const val SPORT_ID_FOOTBALL = "FOOT"
private const val SPORT_NAME_FOOTBALL = "Football"
private const val EVENT_ID = "EVT_1"
private const val EVENT_NAME = "Panathinaikos-Olympiakos"
private const val START_TIME = 1_700_000_000L

internal class MappersTest {

    // region Tests

    @Test
    fun `EventDto toDomain maps all fields correctly`() {
        // given
        val dto = EventDto(id = EVENT_ID, sportId = SPORT_ID_FOOTBALL, name = EVENT_NAME, startTime = START_TIME)
        // when
        val domain = dto.toDomain()
        // then
        assertEquals(EVENT_ID, domain.id)
        assertEquals(SPORT_ID_FOOTBALL, domain.sportId)
        assertEquals(EVENT_NAME, domain.name)
        assertEquals(START_TIME, domain.startTime)
        assertFalse(domain.isFavorite)
    }

    @Test
    fun `SportDto toDomain maps id, name, and empty events list correctly`() {
        // given
        val dto = SportDto(id = SPORT_ID_FOOTBALL, name = SPORT_NAME_FOOTBALL, events = emptyList())
        // when
        val domain = dto.toDomain()
        // then
        assertEquals(SPORT_ID_FOOTBALL, domain.id)
        assertEquals(SPORT_NAME_FOOTBALL, domain.name)
        assertEquals(0, domain.events.size)
    }

    @Test
    fun `SportDto toDomain maps events recursively`() {
        // given
        val eventDtos = listOf(
            EventDto(id = "1", sportId = SPORT_ID_FOOTBALL, name = EVENT_NAME, startTime = START_TIME),
            EventDto(id = "2", sportId = SPORT_ID_FOOTBALL, name = EVENT_NAME, startTime = START_TIME)
        )
        val dto = SportDto(id = SPORT_ID_FOOTBALL, name = SPORT_NAME_FOOTBALL, events = eventDtos)
        // when
        val domain = dto.toDomain()
        // then
        assertEquals(2, domain.events.size)
        assertEquals("1", domain.events[0].id)
        assertEquals("2", domain.events[1].id)
    }

    // endregion
}
