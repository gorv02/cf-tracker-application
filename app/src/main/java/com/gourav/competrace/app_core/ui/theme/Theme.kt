package com.gourav.competrace.app_core.ui.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.gourav.competrace.app_core.data.UserPreferences

private val LightColorScheme = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    errorContainer = md_theme_light_errorContainer,
    onError = md_theme_light_onError,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    outline = md_theme_light_outline,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inverseSurface = md_theme_light_inverseSurface,
    inversePrimary = md_theme_light_inversePrimary,
    surfaceTint = md_theme_light_surfaceTint,
    outlineVariant = md_theme_light_outlineVariant,
    scrim = md_theme_light_scrim,
)


private val DarkColorScheme = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    errorContainer = md_theme_dark_errorContainer,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
    outlineVariant = md_theme_dark_outlineVariant,
    scrim = md_theme_dark_scrim,
)

@Composable
fun CompetraceTheme(
    currentTheme: String = CompetraceThemeNames.DEFAULT,
    darkModePref: String = DarkModePref.SYSTEM_DEFAULT,
    isSysInDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    val myColorScheme = when(currentTheme){
        CompetraceThemeNames.DYNAMIC -> {
            when (darkModePref) {
                DarkModePref.DARK -> dynamicDarkColorScheme(context)
                DarkModePref.LIGHT -> dynamicLightColorScheme(context)
                else -> {
                    if (isSysInDarkTheme) dynamicDarkColorScheme(context)
                    else dynamicLightColorScheme(context)
                }
            }
        }
        else -> {
            when(darkModePref){
                DarkModePref.DARK -> DarkColorScheme
                DarkModePref.LIGHT -> LightColorScheme
                else -> {
                    if(isSysInDarkTheme) DarkColorScheme
                    else LightColorScheme
                }
            }
        }
    }

    val systemUiController = rememberSystemUiController()
    val isDarkTheme = isDarkTheme()
    SideEffect {
        systemUiController.setStatusBarColor(
            color = myColorScheme.surface,
            darkIcons = !isDarkTheme
        )
        systemUiController.setNavigationBarColor(
            color = myColorScheme.surfaceColorAtElevation(3.dp),
            darkIcons = !isDarkTheme
        )
    }

    MaterialTheme(
        colorScheme = myColorScheme,
        typography = MontserratTypography,
        shapes = CompetraceShapes
    ) {
        // TODO (M3): MaterialTheme doesn't provide LocalIndication, remove when it does
        val rippleIndication = rememberRipple()
        CompositionLocalProvider(
            LocalIndication provides rippleIndication,
            content = content
        )
    }
}

@Composable
fun isDarkTheme(
    isSysInDarkTheme: Boolean = isSystemInDarkTheme()
): Boolean {

    val userPreferences = UserPreferences(LocalContext.current)
    val darkModePref by userPreferences.darkModePrefFlow.collectAsState(initial = DarkModePref.SYSTEM_DEFAULT)

    return when (darkModePref) {
        DarkModePref.DARK -> true
        DarkModePref.LIGHT -> false
        else -> isSysInDarkTheme
    }
}