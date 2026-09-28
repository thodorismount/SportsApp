package com.mountouris.sportsapp.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class EventDto(
    val id: String,
    val sportId: String,
    val homeTeam: String,
    val awayTeam: String,
    val startTime: Long
)
