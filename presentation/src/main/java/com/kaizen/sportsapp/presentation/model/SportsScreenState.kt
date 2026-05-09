package com.kaizen.sportsapp.presentation.model

/**
 * Represents the complete UI state of the sports screen.
 *
 * @property currentTimeSeconds Unix timestamp in seconds, updated every second by the ViewModel
 * ticker to drive countdown timers without recomposing the entire screen.
 */
data class SportsScreenState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val sports: List<SportModel> = emptyList(),
    val currentTimeSeconds: Long = 0L
)
