package com.ayumi.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ayumi.app.R

// Stitch design tokens (Ayumi "quiet metrics" palette)
val Cream = Color(0xFFFFF8F4)          // background / surface
val Ink = Color(0xFF201B14)           // on-background ink
val InkSoft = Color(0xFF44483D)        // on-surface-variant
val Matcha = Color(0xFF445C2B)         // primary (deep matcha)
val MatchaBright = Color(0xFF5C7541)   // primary-container
val Persimmon = Color(0xFFA63B01)      // secondary (deep persimmon)
val PersimmonBright = Color(0xFFFE7A42)
val Peach = Color(0xFFFFDBCE)          // secondary-fixed chip
val PeachLight = Color(0xFFFEF2E6)     // surface-container-low
val Mint = Color(0xFFDDFBBA)           // on-tertiary-container chip
val MintDeep = Color(0xFFCCEDA6)
val CardWhite = Color(0xFFFFFFFF)      // surface-container-lowest
val Sand = Color(0xFFF8ECE1)           // surface-container
val SandDeep = Color(0xFFECE0D5)       // surface-container-highest
val Outline = Color(0xFF75796C)

val Manrope = FontFamily(
    Font(R.font.manrope_variable, FontWeight.Normal),
    Font(R.font.manrope_variable, FontWeight.Medium),
    Font(R.font.manrope_variable, FontWeight.SemiBold),
    Font(R.font.manrope_variable, FontWeight.Bold)
)

val Jakarta = FontFamily(
    Font(R.font.plusjakartasans_semibold, FontWeight.SemiBold),
    Font(R.font.plusjakartasans_bold, FontWeight.Bold)
)

val AyumiTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = Jakarta, fontWeight = FontWeight.Bold,
        fontSize = 28.sp, lineHeight = 34.sp, color = Ink
    ),
    headlineSmall = TextStyle(
        fontFamily = Jakarta, fontWeight = FontWeight.Bold,
        fontSize = 22.sp, lineHeight = 28.sp, color = Ink
    ),
    titleLarge = TextStyle(
        fontFamily = Jakarta, fontWeight = FontWeight.SemiBold,
        fontSize = 19.sp, lineHeight = 25.sp, color = Ink
    ),
    titleMedium = TextStyle(
        fontFamily = Manrope, fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp, lineHeight = 22.sp, color = Ink
    ),
    bodyLarge = TextStyle(
        fontFamily = Manrope, fontWeight = FontWeight.Normal,
        fontSize = 16.sp, lineHeight = 24.sp, color = Ink
    ),
    bodyMedium = TextStyle(
        fontFamily = Manrope, fontWeight = FontWeight.Normal,
        fontSize = 14.sp, lineHeight = 20.sp, color = Ink
    ),
    bodySmall = TextStyle(
        fontFamily = Manrope, fontWeight = FontWeight.Normal,
        fontSize = 13.sp, lineHeight = 18.sp, color = InkSoft
    ),
    labelLarge = TextStyle(
        fontFamily = Manrope, fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp, lineHeight = 20.sp, color = Ink
    ),
    labelSmall = TextStyle(
        fontFamily = Manrope, fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp, lineHeight = 15.sp, color = InkSoft
    )
)

private val AyumiLightColors = lightColorScheme(
    primary = Matcha,
    onPrimary = Color.White,
    primaryContainer = MatchaBright,
    secondary = Persimmon,
    onSecondary = Color.White,
    secondaryContainer = Peach,
    background = Cream,
    onBackground = Ink,
    surface = Cream,
    onSurface = Ink,
    surfaceVariant = SandDeep,
    onSurfaceVariant = InkSoft,
    outline = Outline
)

@Composable
fun AyumiTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AyumiLightColors,
        typography = AyumiTypography,
        content = content
    )
}
