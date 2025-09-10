package com.example.newpokedex.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color // Asegúrate de que Color esté importado si lo usas directamente aquí
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ASUMIENDO que los colores base como RedPokemonPrimary, etc., están en Color.kt
// O defínelos aquí si prefieres.
private val LightPokedexColorScheme = lightColorScheme(
    primary = Color(0xFFE53935), // Un rojo Pokémon clásico
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFCDD2),
    onPrimaryContainer = Color(0xFF4C0003),
    secondary = Color(0xFF2962FF),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDDE1FF),
    onSecondaryContainer = Color(0xFF001550),
    tertiary = Color(0xFFFFC107),
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFFFFECB3),
    onTertiaryContainer = Color(0xFF2C1A00),
    error = Color(0xFFB00020),
    onError = Color.White,
    background = Color(0xFFF5F5F5),
    onBackground = Color(0xFF1C1B1F),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1C1B1F)
    // ... completa los demás si es necesario
)

private val DarkPokedexColorScheme = darkColorScheme(
    primary = Color(0xFFFFB3B1), // Rojo más suave para modo oscuro
    onPrimary = Color(0xFF68000A),
    primaryContainer = Color(0xFF930015),
    onPrimaryContainer = Color(0xFFFFDAD7),
    secondary = Color(0xFFB5C4FF),
    onSecondary = Color(0xFF00277F),
    secondaryContainer = Color(0xFF003CAA),
    onSecondaryContainer = Color(0xFFDDE1FF),
    tertiary = Color(0xFFFFDDB3),
    onTertiary = Color(0xFF4D2B00),
    tertiaryContainer = Color(0xFF6B4000),
    onTertiaryContainer = Color(0xFFFFECB3),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    background = Color(0xFF1F1F1F),
    onBackground = Color(0xFFE6E1E5),
    surface = Color(0xFF2B2B2B),
    onSurface = Color(0xFFE6E1E5)
    // ... completa los demás si es necesario
)

@Composable
fun NewPokedexTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkPokedexColorScheme
        else -> LightPokedexColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}