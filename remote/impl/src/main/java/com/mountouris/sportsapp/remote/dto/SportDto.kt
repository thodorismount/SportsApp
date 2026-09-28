package com.mountouris.sportsapp.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SportDto(
    val id: String,
    val name: String,
    val events: List<EventDto>
)
