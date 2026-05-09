package com.kaizen.sportsapp.domain.usecase

import com.kaizen.sportsapp.domain.repository.SportsRepository

class ToggleFavoriteUCImpl(
    private val repository: SportsRepository
) : ToggleFavoriteUC {

    override suspend fun execute(eventId: String) = repository.toggleFavorite(eventId)
}
