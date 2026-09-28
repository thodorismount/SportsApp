package com.mountouris.sportsapp.remote.mapper

import com.mountouris.sportsapp.domain.model.Sport
import com.mountouris.sportsapp.domain.model.SportEvent
import com.mountouris.sportsapp.remote.dto.EventDto
import com.mountouris.sportsapp.remote.dto.SportDto

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
