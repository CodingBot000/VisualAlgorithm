package com.codingbot.algorithm.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


object CustomTheme {
    val colors: CustomColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    val typography: CustomTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
}

@Composable
fun VisualAlgorithmTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    typography: CustomTypography = CustomTheme.typography,
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
//            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    val colors = if (darkTheme) darkColors() else lightColors()

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalTypography provides typography,
    ) {
        content()
    }

}