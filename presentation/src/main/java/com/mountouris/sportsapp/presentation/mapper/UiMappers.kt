package com.mountouris.sportsapp.presentation.mapper

import com.mountouris.sportsapp.domain.model.Sport
import com.mountouris.sportsapp.domain.model.SportEvent
import com.mountouris.sportsapp.presentation.model.EventModel
import com.mountouris.sportsapp.presentation.model.SportModel
import com.mountouris.sportsapp.presentation.model.sportTypeFromId

fun Sport.toUiModel() = SportModel(
    id = id,
    name = name,
    events = events.map { it.toUiModel() },
    icon = sportTypeFromId(id).iconRes
)

fun SportEvent.toUiModel() = EventModel(
    id = id,
    sportId = sportId,
    competitor1 = homeTeam,
    competitor2 = awayTeam,
    startTime = startTime,
    isFavorite = isFavorite
)
