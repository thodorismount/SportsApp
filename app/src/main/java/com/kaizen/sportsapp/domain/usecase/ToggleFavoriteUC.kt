package com.kaizen.sportsapp.domain.usecase

import com.kaizen.sportsapp.domain.repository.SportsRepository

interface ToggleFavoriteUC {
    suspend fun execute(eventId: String)
}

internal class ToggleFavoriteUCImpl(
    private val repository: SportsRepository
) : ToggleFavoriteUC {

    override suspend fun execute(eventId: String) = repository.toggleFavorite(eventId)
}
