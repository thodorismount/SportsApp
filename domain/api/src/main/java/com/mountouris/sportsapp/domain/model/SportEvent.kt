package com.mountouris.sportsapp.domain.model

/**
 * Domain model representing a single sport event.
 *
 * @property startTime Unix timestamp in seconds when the event starts.
 * @property name Raw event name in "Competitor1-Competitor2" format.
 * @property isFavorite Whether the user has marked this event as a favorite.
 */
data class SportEvent(
    val id: String,
    val sportId: String,
    val name: String,
    val startTime: Long,
    val isFavorite: Boolean = false
)
