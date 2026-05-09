package com.kaizen.sportsapp.data.repository

import com.kaizen.sportsapp.domain.model.Sport
import com.kaizen.sportsapp.domain.repository.SportsRepository
import com.kaizen.sportsapp.local.LocalFavoritesSource
import com.kaizen.sportsapp.remote.SportsApiSource
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
