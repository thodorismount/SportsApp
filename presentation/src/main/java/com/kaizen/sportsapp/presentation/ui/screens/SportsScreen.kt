package com.kaizen.sportsapp.presentation.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.kaizen.sportsapp.presentation.R
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kaizen.sportsapp.presentation.SportsViewModel
import com.kaizen.sportsapp.presentation.model.EventModel
import com.kaizen.sportsapp.presentation.ui.theme.SportsAppTheme
import com.kaizen.sportsapp.presentation.model.SportsScreenState
import com.kaizen.sportsapp.presentation.model.SportModel
import com.kaizen.sportsapp.presentation.ui.components.ErrorView
import com.kaizen.sportsapp.presentation.ui.components.EventCard
import com.kaizen.sportsapp.presentation.ui.components.SportSectionHeader
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SportsScreen(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    viewModel: SportsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val error = uiState.errorMessage

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.sports_screen_title),
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                },
                actions = {
                    IconButton(onClick = onToggleTheme) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Filled.WbSunny else Icons.Filled.NightsStay,
                            contentDescription = stringResource(if (isDarkTheme) R.string.cd_switch_to_light else R.string.cd_switch_to_dark),
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )

                error != null -> ErrorView(
                    message = error,
                    onRetry = viewModel::retry
                )

                uiState.sports.isEmpty() -> Text(
                    text = stringResource(R.string.no_events_available),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.align(Alignment.Center)
                )

                else -> SportsList(
                    uiState = uiState,
                    onToggleExpanded = viewModel::toggleExpanded,
                    onToggleFavoritesFilter = viewModel::toggleFavoritesFilter,
                    onToggleFavorite = viewModel::toggleFavorite
                )
            }
        }
    }
}

@Composable
private fun SportsList(
    uiState: SportsScreenState,
    onToggleExpanded: (sportId: String) -> Unit,
    onToggleFavoritesFilter: (sportId: String) -> Unit,
    onToggleFavorite: (eventId: String) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        uiState.sports.forEach { sport ->
            item(key = "header_${sport.id}") {
                SportSectionHeader(
                    sport = sport,
                    onFavoritesFilterToggle = { onToggleFavoritesFilter(sport.id) },
                    onExpandToggle = { onToggleExpanded(sport.id) }
                )
            }
            item(key = "events_${sport.id}") {
                SportEventRow(
                    sport = sport,
                    currentTimeSeconds = uiState.currentTimeSeconds,
                    onToggleFavorite = onToggleFavorite
                )
            }
        }
    }
}

@Composable
private fun SportEventRow(
    sport: SportModel,
    currentTimeSeconds: Long,
    onToggleFavorite: (eventId: String) -> Unit
) {
    AnimatedVisibility(visible = sport.isExpanded) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = sport.displayedEvents, key = { it.id }) { event ->
                EventCard(
                    event = event,
                    currentTimeSeconds = currentTimeSeconds,
                    onFavoriteClick = { onToggleFavorite(event.id) }
                )
            }
        }
    }
}

// region Previews

private const val previewTime = 1_700_000_000L

private val previewSports = listOf(
    SportModel(
        id = "FOOT",
        name = "Football",
        icon = R.drawable.ic_sport_soccer,
        events = listOf(
            EventModel("1", "FOOT", "PAOK", "Olympiakos", previewTime + 3_600, isFavorite = true),
            EventModel(
                "2",
                "FOOT",
                "Man United",
                "Chelsea",
                previewTime + 7_200,
                isFavorite = false
            )
        ),
        isExpanded = true,
        showFavoritesOnly = false
    ),
    SportModel(
        id = "BASK",
        name = "Basketball",
        icon = R.drawable.ic_sport_basketball,
        events = listOf(
            EventModel("3", "BASK", "Lakers", "Celtics", previewTime + 1_800, isFavorite = false)
        ),
        isExpanded = false,
        showFavoritesOnly = false
    )
)

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun SportsScreenLoadingPreview() {
    SportsAppTheme(darkTheme = true) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun SportsScreenErrorPreview() {
    SportsAppTheme(darkTheme = true) {
        ErrorView(
            message = "No internet connection. Please check your network.",
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun SportsScreenContentDarkPreview() {
    SportsAppTheme(darkTheme = true) {
        SportsList(
            uiState = SportsScreenState(
                isLoading = false,
                sports = previewSports,
                currentTimeSeconds = previewTime
            ),
            onToggleExpanded = {},
            onToggleFavoritesFilter = {},
            onToggleFavorite = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun SportsScreenContentLightPreview() {
    SportsAppTheme(darkTheme = false) {
        SportsList(
            uiState = SportsScreenState(
                isLoading = false,
                sports = previewSports,
                currentTimeSeconds = previewTime
            ),
            onToggleExpanded = {},
            onToggleFavoritesFilter = {},
            onToggleFavorite = {}
        )
    }
}

// endregion
