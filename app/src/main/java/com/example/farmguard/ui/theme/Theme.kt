package com.example.farmguard.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = FarmBrightGreen,
    secondary = FarmDeepGreen,
    tertiary = FarmBlue,
    background = Color(0xFF0F172A),
    surface = Color(0xFF1E293B),
    onPrimary = FarmWhite,
    onBackground = FarmWhite,
    onSurface = FarmWhite
)

private val LightColorScheme = lightColorScheme(
    primary = FarmDeepGreen,
    secondary = FarmBrightGreen,
    tertiary = FarmBlue,
    background = FarmBackground,
    surface = FarmWhite,
    onPrimary = FarmWhite,
    onBackground = FarmTextBlack,
    onSurface = FarmTextBlack
)

@Composable
fun FarmGuardTheme(
    darkTheme: Boolean = ThemeState.isDarkMode,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
