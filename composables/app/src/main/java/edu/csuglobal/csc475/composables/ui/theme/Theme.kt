package edu.csuglobal.csc475.composables.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    // Customize these colors as needed
)

private val DarkColors = darkColorScheme(
    // Customize these colors as needed
)


/**
 * Composable function that applies the Composables theme to its content.
 *
 * This function sets up the Material Design theme for the application, applying either
 * a light or dark color scheme based on the system's theme or the provided [darkTheme] parameter.
 *
 * @param darkTheme A boolean value indicating whether to use the dark theme.
 *                  Defaults to the system's dark theme preference.
 * @param content A composable lambda that defines the content to which this theme will be applied.
 *
 * Usage example:
 * ```
 * ComposablesTheme {
 *     // Your app's UI content here
 * }
 * ```
 */
@Composable
fun ComposablesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColors
    } else {
        LightColors
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}