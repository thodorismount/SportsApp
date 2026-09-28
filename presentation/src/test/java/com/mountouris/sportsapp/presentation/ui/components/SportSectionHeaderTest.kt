package com.mountouris.sportsapp.presentation.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mountouris.sportsapp.presentation.model.SportModel
import com.mountouris.sportsapp.presentation.ui.theme.SportsAppTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SportSectionHeaderTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun setContent(
        name: String = "Football",
        isExpanded: Boolean = true,
        showFavoritesOnly: Boolean = false,
        onFavoritesFilterToggle: () -> Unit = {},
        onExpandToggle: () -> Unit = {}
    ) {
        composeTestRule.setContent {
            SportsAppTheme {
                SportSectionHeader(
                    sport = SportModel(
                        id = "FOOT",
                        name = name,
                        events = emptyList(),
                        icon = null,
                        isExpanded = isExpanded,
                        showFavoritesOnly = showFavoritesOnly
                    ),
                    onFavoritesFilterToggle = onFavoritesFilterToggle,
                    onExpandToggle = onExpandToggle
                )
            }
        }
    }

    @Test
    fun showsSportName() {
        setContent(name = "Basketball")
        composeTestRule.onNodeWithText("Basketball").assertIsDisplayed()
    }

    @Test
    fun showsCollapseDescriptionWhenExpanded() {
        setContent(isExpanded = true)
        composeTestRule.onNodeWithContentDescription("Collapse").assertIsDisplayed()
    }

    @Test
    fun showsExpandDescriptionWhenCollapsed() {
        setContent(isExpanded = false)
        composeTestRule.onNodeWithContentDescription("Expand").assertIsDisplayed()
    }

    @Test
    fun clickingExpandToggleInvokesCallback() {
        var clicked = false
        setContent(isExpanded = true, onExpandToggle = { clicked = true })
        composeTestRule.onNodeWithContentDescription("Collapse").performClick()
        assertTrue(clicked)
    }

    @Test
    fun showsShowAllEventsDescriptionWhenFavoritesFilterActive() {
        setContent(showFavoritesOnly = true)
        composeTestRule.onNodeWithContentDescription("Show all events").assertIsDisplayed()
    }

    @Test
    fun clickingFavoritesFilterToggleInvokesCallback() {
        var clicked = false
        setContent(showFavoritesOnly = false, onFavoritesFilterToggle = { clicked = true })
        composeTestRule.onNodeWithContentDescription("Show favorites only").performClick()
        assertTrue(clicked)
    }
}
