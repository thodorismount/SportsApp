package com.mountouris.sportsapp.presentation.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mountouris.sportsapp.presentation.model.EventModel
import com.mountouris.sportsapp.presentation.model.SportModel
import com.mountouris.sportsapp.presentation.model.SportsScreenState
import com.mountouris.sportsapp.presentation.ui.theme.SportsAppTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SportsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val baseTime = 1_700_000_000L

    private fun setScreen(
        uiState: SportsScreenState,
        isDarkTheme: Boolean = true,
        onRetry: () -> Unit = {},
        onToggleExpanded: (String) -> Unit = {},
        onToggleFavoritesFilter: (String) -> Unit = {},
        onToggleFavorite: (String) -> Unit = {}
    ) {
        composeTestRule.setContent {
            SportsAppTheme(darkTheme = isDarkTheme) {
                SportsScreen(
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = {},
                    uiState = uiState,
                    onToggleExpanded = onToggleExpanded,
                    onToggleFavoritesFilter = onToggleFavoritesFilter,
                    onToggleFavorite = onToggleFavorite,
                    onRetry = onRetry
                )
            }
        }
    }

    @Test
    fun showsLoadingIndicatorWhenLoading() {
        setScreen(SportsScreenState(isLoading = true))
        composeTestRule.onNodeWithTag(TestTags.LOADING_INDICATOR).assertIsDisplayed()
    }

    @Test
    fun showsEmptyStateWhenSportsLoadedEmpty() {
        setScreen(SportsScreenState(isLoading = false, sports = emptyList()))
        composeTestRule.onNodeWithText("No events available").assertIsDisplayed()
    }

    @Test
    fun showsErrorMessageOnErrorState() {
        setScreen(SportsScreenState(isLoading = false, errorMessage = "No internet connection."))
        composeTestRule.onNodeWithText("No internet connection.").assertIsDisplayed()
    }

    @Test
    fun clickingRetryOnErrorStateInvokesCallback() {
        var retried = false
        setScreen(
            uiState = SportsScreenState(isLoading = false, errorMessage = "Error"),
            onRetry = { retried = true }
        )
        composeTestRule.onNodeWithText("Retry").performClick()
        assertTrue(retried)
    }

    @Test
    fun showsSportNamesWhenContentIsLoaded() {
        setScreen(SportsScreenState(isLoading = false, sports = twoSports()))
        composeTestRule.onNodeWithText("Football").assertIsDisplayed()
        composeTestRule.onNodeWithText("Basketball").assertIsDisplayed()
    }

    @Test
    fun showsThemeToggleButtonInTopBar() {
        setScreen(SportsScreenState(isLoading = false, sports = emptyList()), isDarkTheme = true)
        composeTestRule.onNodeWithContentDescription("Switch to light theme").assertIsDisplayed()
    }

    @Test
    fun clickingExpandToggleInvokesCallback() {
        var expandedSportId = ""
        setScreen(
            uiState = SportsScreenState(isLoading = false, sports = twoSports()),
            onToggleExpanded = { expandedSportId = it }
        )
        composeTestRule.onAllNodesWithContentDescription("Collapse").onFirst().performClick()
        assertTrue(expandedSportId.isNotEmpty())
    }

    @Test
    fun clickingFavoritesFilterInvokesCallback() {
        var filteredSportId = ""
        setScreen(
            uiState = SportsScreenState(isLoading = false, sports = twoSports()),
            onToggleFavoritesFilter = { filteredSportId = it }
        )
        composeTestRule.onAllNodesWithContentDescription("Show favorites only").onFirst().performClick()
        assertTrue(filteredSportId.isNotEmpty())
    }

    // region Helpers

    private fun twoSports() = listOf(
        SportModel(
            id = "FOOT",
            name = "Football",
            icon = null,
            events = listOf(
                EventModel("1", "FOOT", "PAOK", "Olympiakos", baseTime + 3_600, isFavorite = false)
            ),
            isExpanded = true,
            showFavoritesOnly = false
        ),
        SportModel(
            id = "BASK",
            name = "Basketball",
            icon = null,
            events = listOf(
                EventModel("2", "BASK", "Lakers", "Celtics", baseTime + 7_200, isFavorite = false)
            ),
            isExpanded = true,
            showFavoritesOnly = false
        )
    )

    // endregion
}
