package com.kaizen.sportsapp.domain.model

data class SportEvent(
    val id: String,
    val sportId: String,
    val name: String,
    val startTime: Long,
    val isFavorite: Boolean = false
)
