package com.kaizen.sportsapp.presentation.model

/**
 * UI model for a single event card.
 * Competitor names are pre-split from the raw "Competitor1-Competitor2" format.
 */
data class EventModel(
    val id: String,
    val sportId: String,
    val competitor1: String,
    val competitor2: String,
    val startTime: Long,
    val isFavorite: Boolean
)
