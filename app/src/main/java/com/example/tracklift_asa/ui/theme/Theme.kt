package com.example.tracklift_asa.ui.theme

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Esquema de cores escuro personalizado para TrackLift
private val TrackLiftDarkColorScheme = darkColorScheme(
    primary = TrackLiftPrimary,
    onPrimary = TrackLiftOnPrimary,
    primaryContainer = TrackLiftPrimaryVariant,
    onPrimaryContainer = TrackLiftOnPrimary,
    
    secondary = TrackLiftSecondary,
    onSecondary = Color.White,
    secondaryContainer = TrackLiftSecondaryVariant,
    onSecondaryContainer = Color.White,
    
    tertiary = TrackLiftOrangeLight,
    onTertiary = TrackLiftOnPrimary,
    tertiaryContainer = TrackLiftOrangeDark,
    onTertiaryContainer = Color.White,
    
    background = TrackLiftBackground,
    onBackground = TrackLiftOnBackground,
    surface = TrackLiftSurface,
    onSurface = TrackLiftOnSurface,
    surfaceVariant = TrackLiftSurfaceVariant,
    onSurfaceVariant = TrackLiftOnSurfaceVariant,
    surfaceContainerHighest = TrackLiftSurfaceContainer,
    
    error = TrackLiftError,
    onError = Color.White,
    errorContainer = TrackLiftErrorLight,
    onErrorContainer = Color.White,
    
    outline = TrackLiftDivider,
    outlineVariant = TrackLiftDivider.copy(alpha = 0.5f),
    
    scrim = TrackLiftOverlay,
    inverseSurface = TrackLiftOnBackground,
    inverseOnSurface = TrackLiftBackground,
    inversePrimary = TrackLiftPrimary.copy(alpha = 0.3f)
)

// Esquema de cores claro (para futuras implementações)
private val TrackLiftLightColorScheme = lightColorScheme(
    primary = TrackLiftPrimary,
    onPrimary = Color.White,
    primaryContainer = TrackLiftOrangeLight,
    onPrimaryContainer = TrackLiftOnPrimary,
    
    secondary = TrackLiftSecondary,
    onSecondary = Color.White,
    secondaryContainer = TrackLiftSecondaryVariant,
    onSecondaryContainer = Color.White,
    
    tertiary = TrackLiftOrangeDark,
    onTertiary = Color.White,
    tertiaryContainer = TrackLiftOrangeLight,
    onTertiaryContainer = TrackLiftOnPrimary,
    
    background = Color.White,
    onBackground = Color.Black,
    surface = Color(0xFFF5F5F5),
    onSurface = Color.Black,
    surfaceVariant = Color(0xFFE0E0E0),
    onSurfaceVariant = Color(0xFF424242),
    
    error = TrackLiftError,
    onError = Color.White,
    errorContainer = TrackLiftErrorLight,
    onErrorContainer = Color.White,
    
    outline = Color(0xFF757575),
    outlineVariant = Color(0xFFBDBDBD),
    
    scrim = Color(0x80000000),
    inverseSurface = Color.Black,
    inverseOnSurface = Color.White,
    inversePrimary = TrackLiftPrimary.copy(alpha = 0.3f)
)

@Composable
fun TrackliftasaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Desabilitando dynamic color para manter a identidade visual do TrackLift
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> TrackLiftDarkColorScheme
        else -> TrackLiftLightColorScheme
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