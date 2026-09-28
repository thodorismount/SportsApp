package com.mountouris.sportsapp.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val AppDarkColorScheme = darkColorScheme(
    primary = Gold,
    onPrimary = Black,
    secondary = PrimaryBlue,
    onSecondary = White,
    tertiary = AccentRed,
    onTertiary = White,
    background = DeepDark,
    onBackground = White,
    surface = DarkGray,
    onSurface = White,
    onSurfaceVariant = MidGray,
    error = AccentRed,
    onError = White
)

private val AppLightColorScheme = lightColorScheme(
    primary = Gold,
    onPrimary = Black,
    secondary = PrimaryBlue,
    onSecondary = White,
    tertiary = AccentRed,
    onTertiary = White,
    background = White,
    onBackground = Black,
    surface = LightGray,
    onSurface = Black,
    error = AccentRed,
    onError = White
)

@Composable
fun SportsAppTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) AppDarkColorScheme else AppLightColorScheme,
        typography = Typography,
        content = content
    )
}
