package com.mountouris.sportsapp.domain.model

data class SportEvent(
    val id: String,
    val sportId: String,
    val homeTeam: String,
    val awayTeam: String,
    val startTime: Long,
    val isFavorite: Boolean = false
)
