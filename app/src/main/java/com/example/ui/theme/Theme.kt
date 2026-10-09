package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = WarmGoldSecondary,
    onPrimary = Color.Black,
    primaryContainer = DeepMaroonLight,
    onPrimaryContainer = Color.White,
    secondary = WarmGoldVariant,
    onSecondary = Color.Black,
    secondaryContainer = DeepMaroonDark,
    onSecondaryContainer = WarmGoldSecondary,
    background = DarkBackground,
    onBackground = TextPrimaryDark,
    surface = DarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = Color(0xFF3B1B23),
    onSurfaceVariant = TextSecondaryDark,
    outline = Color(0xFF8A6B72)
)

private val LightColorScheme = lightColorScheme(
    primary = DeepMaroonPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFCE4E8),
    onPrimaryContainer = DeepMaroonDark,
    secondary = WarmGoldSecondary,
    onSecondary = Color.Black,
    secondaryContainer = LightGoldContainer,
    onSecondaryContainer = Color(0xFF4A3B00),
    background = CreamBackground,
    onBackground = TextPrimaryLight,
    surface = CreamSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = Color(0xFFF9EFEF),
    onSurfaceVariant = TextSecondaryLight,
    outline = Color(0xFFD4C1C5)
)

@Composable
fun BamBamJankiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
