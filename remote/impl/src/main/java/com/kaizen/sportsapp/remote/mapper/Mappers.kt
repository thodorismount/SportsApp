package com.kaizen.sportsapp.remote.mapper

import com.kaizen.sportsapp.domain.model.Sport
import com.kaizen.sportsapp.domain.model.SportEvent
import com.kaizen.sportsapp.remote.dto.EventDto
import com.kaizen.sportsapp.remote.dto.SportDto

internal fun SportDto.toDomain() = Sport(
    id = id,
    name = name,
    events = events.map { it.toDomain() }
)

internal fun EventDto.toDomain() = SportEvent(
    id = id,
    sportId = sportId,
    name = name,
    startTime = startTime
)
