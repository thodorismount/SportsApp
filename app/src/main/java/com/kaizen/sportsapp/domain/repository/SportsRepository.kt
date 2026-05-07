package com.kaizen.sportsapp.domain.repository

import com.kaizen.sportsapp.domain.model.Sport
import kotlinx.coroutines.flow.Flow

interface SportsRepository {
    suspend fun fetchSports(): Result<List<Sport>>
    fun observeFavoriteIds(): Flow<Set<String>>
    suspend fun toggleFavorite(eventId: String)
}
