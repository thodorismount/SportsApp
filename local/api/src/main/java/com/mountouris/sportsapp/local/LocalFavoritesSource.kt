package com.mountouris.sportsapp.local

import kotlinx.coroutines.flow.Flow

/**
 * Abstraction for the local favorites data source.
 * Implementations are responsible for persisting and observing favorite event IDs.
 */
interface LocalFavoritesSource {

    /**
     * Emits the current set of favorite event IDs and any subsequent changes.
     */
    fun observeFavoriteIds(): Flow<Set<String>>

    /**
     * Adds the event to favorites if not already present, removes it otherwise.
     * @param eventId the unique identifier of the event to toggle.
     */
    suspend fun toggleFavorite(eventId: String)
}
