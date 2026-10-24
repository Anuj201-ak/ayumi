package com.ayumi.app.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Ayumi brand palette
val Washi = Color(0xFFF6F1E7)      // warm washi-paper background
val Ink = Color(0xFF2A241D)        // soft ink text
val Persimmon = Color(0xFFE86A33)  // accent
val Matcha = Color(0xFF7C9A5B)     // important / done
val Navy = Color(0xFF1F2A4D)       // deep navy

private val AyumiColors = lightColorScheme(
    primary = Persimmon,
    secondary = Matcha,
    background = Washi,
    surface = Color.White,
    onPrimary = Color.White,
    onBackground = Ink,
    onSurface = Ink,
)

@Composable
fun AyumiTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AyumiColors,
        content = content
    )
}
