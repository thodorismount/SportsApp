package com.mountouris.sportsapp.domain.usecase

import com.mountouris.sportsapp.domain.model.Sport
import com.mountouris.sportsapp.domain.repository.SportsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class GetSportsWithFavoritesUCImpl(
    private val repository: SportsRepository
) : GetSportsWithFavoritesUC {

    override fun execute(): Flow<Result<List<Sport>>> = flow {
        repository.fetchSports().fold(
            onFailure = { emit(Result.failure(it)) },
            onSuccess = { sports ->
                emitAll(
                    repository.observeFavoriteIds()
                        .catch { emit(emptySet()) }
                        .map { favoriteIds -> Result.success(sports.withFavorites(favoriteIds)) }
                )
            }
        )
    }

    private fun List<Sport>.withFavorites(favoriteIds: Set<String>): List<Sport> =
        map { sport ->
            sport.copy(
                events = sport.events.map { event ->
                    event.copy(isFavorite = event.id in favoriteIds)
                }
            )
        }
}
