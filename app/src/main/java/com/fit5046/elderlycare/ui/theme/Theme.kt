package com.fit5046.elderlycare.ui.theme

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFFB39DDB),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFF1E9FF),
    secondary = Color(0xFF8E7CC3),
    onSecondary = Color.White,
    tertiary = Color(0xFFD7C8F3),
    background = Color(0xFFFBF7FF),
    surface = Color.White,
    onSurface = Color(0xFF1A1C1E)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFD1C3F7),
    secondary = Color(0xFFB8A8E6),
    tertiary = Color(0xFFE7DDF8)
)

@Composable
fun ElderlyCareTheme(content: @Composable () -> Unit) {
    val colors = LightColors

    MaterialTheme(colorScheme = colors) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }
}
