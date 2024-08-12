package dev.yuanzix.beatify.ui.theme

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

val DarkColorScheme = darkColorScheme(
    primary = PinkBase,
    onPrimary = Color.Black,
    primaryContainer = PinkDark,
    onPrimaryContainer = Color.White,

    secondary = SecondaryPurple,
    onSecondary = Color.Black,
    secondaryContainer = SecondaryPurpleDark,
    onSecondaryContainer = Color.White,

    tertiary = TertiaryBlue,
    onTertiary = Color.Black,
    tertiaryContainer = TertiaryBlueDark,
    onTertiaryContainer = Color.White,

    background = DarkBackground,
    onBackground = Color.White,
    surface = DarkSurface,
    onSurface = Color.White,

    error = ErrorDark,
    onError = Color.Black,
    errorContainer = ErrorColor,
    onErrorContainer = Color.White,

    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color.White
)

val LightColorScheme = lightColorScheme(
    primary = PinkBase,
    onPrimary = Color.White,
    primaryContainer = PinkLight,
    onPrimaryContainer = PinkBase,

    secondary = SecondaryPurple,
    onSecondary = Color.White,
    secondaryContainer = SecondaryPurpleLight,
    onSecondaryContainer = SecondaryPurpleDark,

    tertiary = TertiaryBlue,
    onTertiary = Color.White,
    tertiaryContainer = TertiaryBlueLight,
    onTertiaryContainer = TertiaryBlueDark,

    background = LightBackground,
    onBackground = Color.Black,
    surface = LightSurface,
    onSurface = Color.Black,

    error = ErrorColor,
    onError = Color.White,
    errorContainer = ErrorContainer,
    onErrorContainer = ErrorColor,

    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = Color.Black
)


@Composable
fun BeatifyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
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