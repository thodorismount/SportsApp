package com.mountouris.sportsapp.data.repository

import com.mountouris.sportsapp.domain.model.Sport
import com.mountouris.sportsapp.domain.repository.SportsRepository
import com.mountouris.sportsapp.local.LocalFavoritesSource
import com.mountouris.sportsapp.remote.SportsApiSource
import kotlinx.coroutines.flow.Flow

class SportsRepositoryImpl(
    private val remoteSource: SportsApiSource,
    private val localSource: LocalFavoritesSource
) : SportsRepository {

    override suspend fun fetchSports(): Result<List<Sport>> = runCatching {
        remoteSource.fetchSports()
    }

    override fun observeFavoriteIds(): Flow<Set<String>> =
        localSource.observeFavoriteIds()

    override suspend fun toggleFavorite(eventId: String) =
        localSource.toggleFavorite(eventId)
}
