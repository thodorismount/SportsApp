package com.kaizen.sportsapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaizen.sportsapp.domain.usecase.GetSportsWithFavoritesUC
import com.kaizen.sportsapp.domain.usecase.ToggleFavoriteUC
import com.kaizen.sportsapp.presentation.mapper.toUiModel
import com.kaizen.sportsapp.presentation.model.SportsScreenState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import java.net.ConnectException
import java.net.UnknownHostException

class SportsViewModel(
    private val getSportsWithFavorites: GetSportsWithFavoritesUC,
    private val toggleFavoriteUC: ToggleFavoriteUC
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SportsScreenState(currentTimeSeconds = System.currentTimeMillis() / 1000)
    )
    val uiState: StateFlow<SportsScreenState> = _uiState.asStateFlow()

    init {
        loadSports()
        startTicker()
    }

    // region Data loading

    private var sportsJob: Job? = null

    private fun loadSports() {
        sportsJob?.cancel()
        sportsJob = viewModelScope.launch {
            getSportsWithFavorites.execute().collect { result ->
                result.fold(
                    onSuccess = { sports ->
                        val preserved = _uiState.value.sports.associateBy { it.id }
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                errorMessage = null,
                                sports = sports.map { sport ->
                                    val prev = preserved[sport.id]
                                    sport.toUiModel().copy(
                                        isExpanded = prev?.isExpanded ?: true,
                                        showFavoritesOnly = prev?.showFavoritesOnly ?: false
                                    )
                                }
                            )
                        }
                    },
                    onFailure = { throwable ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = when (throwable) {
                                    is UnknownHostException,
                                    is ConnectException -> "No internet connection. Please check your network."
                                    is SerializationException -> "Failed to parse server response."
                                    else -> "An unexpected error occurred. Please try again."
                                }
                            )
                        }
                    }
                )
            }
        }
    }

    fun retry() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        loadSports()
    }

    // endregion

    // region Ticker

    private fun startTicker() {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                _uiState.update { it.copy(currentTimeSeconds = it.currentTimeSeconds + 1) }
            }
        }
    }

    // endregion

    // region UI interactions

    fun toggleExpanded(sportId: String) {
        _uiState.update { state ->
            state.copy(
                sports = state.sports.map { sport ->
                    if (sport.id == sportId) sport.copy(isExpanded = !sport.isExpanded) else sport
                }
            )
        }
    }

    fun toggleFavoritesFilter(sportId: String) {
        _uiState.update { state ->
            state.copy(
                sports = state.sports.map { sport ->
                    if (sport.id == sportId) sport.copy(showFavoritesOnly = !sport.showFavoritesOnly) else sport
                }
            )
        }
    }

    fun toggleFavorite(eventId: String) {
        viewModelScope.launch {
            toggleFavoriteUC.execute(eventId)
        }
    }

    // endregion
}
