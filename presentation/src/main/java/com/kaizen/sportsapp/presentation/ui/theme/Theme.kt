package com.kaizen.sportsapp.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val KaizenDarkColorScheme = darkColorScheme(
    primary = Gold,
    onPrimary = Black,
    secondary = PrimaryBlue,
    onSecondary = White,
    tertiary = AccentRed,
    onTertiary = White,
    background = Black,
    onBackground = White,
    surface = DarkGray,
    onSurface = White,
    error = AccentRed,
    onError = White
)

@Composable
fun SportsAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = KaizenDarkColorScheme,
        typography = Typography,
        content = content
    )
}
