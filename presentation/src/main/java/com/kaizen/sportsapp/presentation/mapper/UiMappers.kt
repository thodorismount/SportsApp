package com.kaizen.sportsapp.presentation.mapper

import com.kaizen.sportsapp.domain.model.Sport
import com.kaizen.sportsapp.domain.model.SportEvent
import com.kaizen.sportsapp.presentation.model.EventModel
import com.kaizen.sportsapp.presentation.model.SportModel

/** Maps a [Sport] domain model to its UI representation. */
fun Sport.toUiModel() = SportModel(
    id = id,
    name = name,
    events = events.map { it.toUiModel() }
)

/**
 * Maps a [SportEvent] domain model to its UI representation.
 * Splits the raw event name on "-" to extract the two competitor names.
 */
fun SportEvent.toUiModel(): EventModel {
    val parts = name.split("-", limit = 2)
    return EventModel(
        id = id,
        sportId = sportId,
        competitor1 = parts[0].trim(),
        competitor2 = parts.getOrElse(1) { "" }.trim(),
        startTime = startTime,
        isFavorite = isFavorite
    )
}
