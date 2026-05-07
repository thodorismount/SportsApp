package com.kaizen.sportsapp.domain.usecase

import com.kaizen.sportsapp.domain.model.Sport
import com.kaizen.sportsapp.domain.repository.SportsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

interface GetSportsWithFavoritesUC {
    fun execute(): Flow<Result<List<Sport>>>
}

internal class GetSportsWithFavoritesUCImpl(
    private val repository: SportsRepository
) : GetSportsWithFavoritesUC {

    override fun execute(): Flow<Result<List<Sport>>> = flow {
        val result = repository.fetchSports()
        if (result.isFailure) {
            emit(Result.failure(result.exceptionOrNull()!!))
            return@flow
        }
        val sports = result.getOrThrow()
        emitAll(
            repository.observeFavoriteIds().map { favoriteIds ->
                Result.success(sports.map { sport ->
                    sport.copy(
                        events = sport.events.map { event ->
                            event.copy(isFavorite = event.id in favoriteIds)
                        }
                    )
                })
            }
        )
    }
}
