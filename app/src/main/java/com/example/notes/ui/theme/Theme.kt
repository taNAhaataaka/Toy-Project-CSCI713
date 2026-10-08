package com.example.notes.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val HighContrastDarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFFF00),
    onPrimary = Color.Black,
    secondary = Color(0xFF00FFFF),
    onSecondary = Color.Black,
    tertiary = Color(0xFFFF80FF),
    onTertiary = Color.Black,
    background = Color.Black,
    onBackground = Color.White,
    surface = Color.Black,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF1A1A1A),
    onSurfaceVariant = Color.White,
    outline = Color.White,
    error = Color(0xFFFF6B6B),
    onError = Color.Black
)

private val HighContrastLightColorScheme = lightColorScheme(
    primary = Color(0xFF000000),
    onPrimary = Color.White,
    secondary = Color(0xFF0033CC),
    onSecondary = Color.White,
    tertiary = Color(0xFF5A007A),
    onTertiary = Color.White,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = Color(0xFFE5E5E5),
    onSurfaceVariant = Color.Black,
    outline = Color(0xFF333333),
    error = Color(0xFFB00020),
    onError = Color.White
)

private val NdsuColorScheme = lightColorScheme(
    primary = Color(0xFFFFC425),
    onPrimary = Color.Black,
    secondary = Color(0xFF00583D),
    onSecondary = Color.White,
    tertiary = Color(0xFF00583D),
    onTertiary = Color.White,
    background = Color(0xFF00583D),
    onBackground = Color.White,
    surface = Color(0xFF003524),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF003524),
    onSurfaceVariant = Color.White,
    surfaceContainerLowest = Color(0xFF003524),
    surfaceContainerLow = Color(0xFF003524),
    surfaceContainer = Color(0xFF003524),
    surfaceContainerHigh = Color(0xFF003524),
    surfaceContainerHighest = Color(0xFF003524),
    outline = Color(0xFFFFC425)
)

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun NotesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    highContrast: Boolean = false,
    ndsuColors: Boolean = false,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        highContrast && darkTheme -> HighContrastDarkColorScheme
        highContrast -> HighContrastLightColorScheme
        ndsuColors -> NdsuColorScheme
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}