package com.kaizen.sportsapp.domain.repository

import com.kaizen.sportsapp.domain.model.Sport
import kotlinx.coroutines.flow.Flow

/**
 * Abstraction for accessing sports and managing favorites.
 * Hides the origin of data from the rest of the domain.
 */
interface SportsRepository {

    /**
     * Fetches the full list of sports and their events from the remote source.
     * Returns a [Result] wrapping the list on success or the exception on failure.
     */
    suspend fun fetchSports(): Result<List<Sport>>

    /**
     * Emits the current set of favorite event IDs and any subsequent changes.
     */
    fun observeFavoriteIds(): Flow<Set<String>>

    /**
     * Adds the event to favorites if not already present, removes it otherwise.
     */
    suspend fun toggleFavorite(eventId: String)
}
