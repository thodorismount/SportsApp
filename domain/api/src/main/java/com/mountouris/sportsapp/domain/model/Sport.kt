package com.mountouris.sportsapp.domain.model

/**
 * Domain model representing a sport category and its upcoming events.
 */
data class Sport(
    val id: String,
    val name: String,
    val events: List<SportEvent>
)
