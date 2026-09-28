package com.mountouris.sportsapp.domain.usecase

/**
 * Toggles the favorite state of a sport event.
 * Adds the event to favorites if not present, removes it otherwise.
 */
interface ToggleFavoriteUC {

    /**
     * @param eventId the unique identifier of the event to toggle.
     */
    suspend fun execute(eventId: String)
}
