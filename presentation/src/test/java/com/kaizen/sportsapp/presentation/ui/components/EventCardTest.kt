package com.kaizen.sportsapp.presentation.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaizen.sportsapp.presentation.model.EventModel
import com.kaizen.sportsapp.presentation.ui.theme.SportsAppTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EventCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val baseTime = 1_700_000_000L

    private fun setContent(
        competitor1: String = "Team A",
        competitor2: String = "Team B",
        startTime: Long = baseTime + 3_600,
        isFavorite: Boolean = false,
        onFavoriteClick: () -> Unit = {}
    ) {
        composeTestRule.setContent {
            SportsAppTheme {
                EventCard(
                    event = EventModel(
                        id = "1",
                        sportId = "FOOT",
                        competitor1 = competitor1,
                        competitor2 = competitor2,
                        startTime = startTime,
                        isFavorite = isFavorite
                    ),
                    currentTimeSeconds = baseTime,
                    onFavoriteClick = onFavoriteClick
                )
            }
        }
    }

    @Test
    fun showsFormattedCountdown() {
        setContent(startTime = baseTime + 3_661) // 1h 1m 1s remaining
        composeTestRule.onNodeWithText("01:01:01").assertIsDisplayed()
    }

    @Test
    fun showsBothCompetitorNames() {
        setContent(competitor1 = "PAOK", competitor2 = "Olympiakos")
        composeTestRule.onNodeWithText("PAOK").assertIsDisplayed()
        composeTestRule.onNodeWithText("Olympiakos").assertIsDisplayed()
    }

    @Test
    fun showsVsLabel() {
        setContent()
        composeTestRule.onNodeWithText("VS").assertIsDisplayed()
    }

    @Test
    fun showsAddToFavoritesDescriptionWhenNotFavorite() {
        setContent(isFavorite = false)
        composeTestRule.onNodeWithContentDescription("Add to favorites").assertIsDisplayed()
    }

    @Test
    fun showsRemoveFromFavoritesDescriptionWhenFavorite() {
        setContent(isFavorite = true)
        composeTestRule.onNodeWithContentDescription("Remove from favorites").assertIsDisplayed()
    }

    @Test
    fun clickingFavoriteButtonInvokesCallback() {
        var clicked = false
        setContent(isFavorite = false, onFavoriteClick = { clicked = true })
        composeTestRule.onNodeWithContentDescription("Add to favorites").performClick()
        assertTrue(clicked)
    }
}
