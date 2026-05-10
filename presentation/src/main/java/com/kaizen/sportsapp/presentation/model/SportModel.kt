package com.kaizen.sportsapp.presentation.model

import androidx.annotation.DrawableRes

/**
 * UI model for a sport section, holding display state alongside the event list.
 *
 * @property displayedEvents Returns all events or only favorites depending on [showFavoritesOnly].
 */
data class SportModel(
    val id: String,
    val name: String,
    val events: List<EventModel>,
    @DrawableRes val icon: Int?,
    val isExpanded: Boolean = true,
    val showFavoritesOnly: Boolean = false
) {
    val displayedEvents: List<EventModel>
        get() = if (showFavoritesOnly) events.filter { it.isFavorite } else events
}
