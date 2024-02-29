package pro.linguistcopilot.design.compose_ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import pro.linguistcopilot.design.compose_ui.color.DarkLinguistCopilotPalette
import pro.linguistcopilot.design.compose_ui.color.LightLinguistCopilotPalette
import pro.linguistcopilot.design.compose_ui.color.LinguistCopilotPalette
import pro.linguistcopilot.design.compose_ui.typography.LinguistCopilotTypography


private val LightColorScheme = lightColorScheme(
    primary = LightLinguistCopilotPalette.primary,
    onPrimary = LightLinguistCopilotPalette.contentAccent,
    primaryContainer = LightLinguistCopilotPalette.primaryContainer,
    onPrimaryContainer = LightLinguistCopilotPalette.content,
    inversePrimary = LightLinguistCopilotPalette.primary,
    secondary = LightLinguistCopilotPalette.secondary,
    onSecondary = LightLinguistCopilotPalette.contentAccent,
    secondaryContainer = LightLinguistCopilotPalette.secondaryContainer,
    onSecondaryContainer = LightLinguistCopilotPalette.content,
    tertiary = LightLinguistCopilotPalette.tertiary,
    onTertiary = LightLinguistCopilotPalette.contentAccent,
    tertiaryContainer = LightLinguistCopilotPalette.secondaryContainer,
    onTertiaryContainer = LightLinguistCopilotPalette.content,
    background = LightLinguistCopilotPalette.background,
    onBackground = LightLinguistCopilotPalette.content,
    surface = LightLinguistCopilotPalette.surface,
    onSurface = LightLinguistCopilotPalette.content,
    surfaceVariant = LightLinguistCopilotPalette.surfacePlus3,
    onSurfaceVariant = LightLinguistCopilotPalette.content,
    outline = LightLinguistCopilotPalette.outline,
    outlineVariant = LightLinguistCopilotPalette.outline.copy(alpha = 0.5f),
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkLinguistCopilotPalette.primary,
    onPrimary = DarkLinguistCopilotPalette.contentAccent,
    primaryContainer = DarkLinguistCopilotPalette.primaryContainer,
    onPrimaryContainer = DarkLinguistCopilotPalette.content,
    inversePrimary = DarkLinguistCopilotPalette.primary,
    secondary = DarkLinguistCopilotPalette.secondary,
    onSecondary = DarkLinguistCopilotPalette.contentAccent,
    secondaryContainer = DarkLinguistCopilotPalette.secondaryContainer,
    onSecondaryContainer = DarkLinguistCopilotPalette.content,
    tertiary = DarkLinguistCopilotPalette.tertiary,
    onTertiary = DarkLinguistCopilotPalette.contentAccent,
    tertiaryContainer = DarkLinguistCopilotPalette.secondaryContainer,
    onTertiaryContainer = DarkLinguistCopilotPalette.content,
    background = DarkLinguistCopilotPalette.background,
    onBackground = DarkLinguistCopilotPalette.content,
    surface = DarkLinguistCopilotPalette.surface,
    onSurface = DarkLinguistCopilotPalette.content,
    surfaceVariant = DarkLinguistCopilotPalette.surfacePlus3,
    onSurfaceVariant = DarkLinguistCopilotPalette.content,
    outline = DarkLinguistCopilotPalette.outline,
    outlineVariant = DarkLinguistCopilotPalette.outline.copy(alpha = 0.5f),
)

@Composable
fun LinguistCopilotTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val typography = remember { LinguistCopilotTypography() }

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = !darkTheme,
        )
    }

    CompositionLocalProvider(
        LocalLinguistCopilotPalette provides if (darkTheme) DarkLinguistCopilotPalette else LightLinguistCopilotPalette,
        LocalLinguistCopilotTypography provides typography,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = remember {
                Typography(
                    headlineLarge = typography.test1,
                )
            },
            content = content,
        )
    }
}

object LinguistCopilotTheme {

    val palette: LinguistCopilotPalette
        @Composable
        @ReadOnlyComposable
        get() = LocalLinguistCopilotPalette.current

    val typography: LinguistCopilotTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalLinguistCopilotTypography.current
}

internal val LocalLinguistCopilotPalette = staticCompositionLocalOf<LinguistCopilotPalette> {
    error("StaticCompositionLocal LocalLinguistCopilotPalette not provided")
}

internal val LocalLinguistCopilotTypography = staticCompositionLocalOf<LinguistCopilotTypography> {
    error("StaticCompositionLocal LocalLinguistCopilotTypography not provided")
}
