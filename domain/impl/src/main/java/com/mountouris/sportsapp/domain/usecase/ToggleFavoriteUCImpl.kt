package com.mountouris.sportsapp.domain.usecase

import com.mountouris.sportsapp.domain.repository.SportsRepository

class ToggleFavoriteUCImpl(
    private val repository: SportsRepository
) : ToggleFavoriteUC {

    override suspend fun execute(eventId: String) = repository.toggleFavorite(eventId)
}
