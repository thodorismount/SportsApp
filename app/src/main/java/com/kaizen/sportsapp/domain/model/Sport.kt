package com.kaizen.sportsapp.domain.model

data class Sport(
    val id: String,
    val name: String,
    val events: List<SportEvent>
)
