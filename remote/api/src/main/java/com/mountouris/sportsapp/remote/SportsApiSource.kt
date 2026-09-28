package com.mountouris.sportsapp.remote

import com.mountouris.sportsapp.domain.model.Sport

/**
 * Abstraction for the remote sports data source.
 * Implementations are responsible for network communication and DTO mapping.
 */
interface SportsApiSource {

    /**
     * Fetches and returns the full list of sports with their events.
     * Throws on network or serialization failure.
     */
    suspend fun fetchSports(): List<Sport>
}
