package com.mountouris.sportsapp.presentation.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mountouris.sportsapp.presentation.ui.theme.SportsAppTheme
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ErrorViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun showsErrorMessage() {
        composeTestRule.setContent {
            SportsAppTheme {
                ErrorView(message = "No internet connection.")
            }
        }
        composeTestRule.onNodeWithText("No internet connection.").assertIsDisplayed()
    }

    @Test
    fun showsRetryButtonWhenCallbackProvided() {
        composeTestRule.setContent {
            SportsAppTheme {
                ErrorView(message = "Error", onRetry = {})
            }
        }
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()
    }

    @Test
    fun hidesRetryButtonWhenCallbackIsNull() {
        composeTestRule.setContent {
            SportsAppTheme {
                ErrorView(message = "Error", onRetry = null)
            }
        }
        val nodes = composeTestRule.onAllNodes(
            androidx.compose.ui.test.hasText("Retry")
        ).fetchSemanticsNodes()
        assertTrue(nodes.isEmpty())
    }

    @Test
    fun clickingRetryInvokesCallback() {
        var clicked = false
        composeTestRule.setContent {
            SportsAppTheme {
                ErrorView(message = "Error", onRetry = { clicked = true })
            }
        }
        composeTestRule.onNodeWithText("Retry").performClick()
        assertTrue(clicked)
    }
}
