package com.codingbot.algorithm.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

class CustomColors(
    textColorPrimary: Color,
    bg: Color,
    bgOpposite: Color,
    buttonBackground: Color,
    buttonIcon: Color,
    white: Color,
    black: Color,
) {
    var textColorPrimary by mutableStateOf(textColorPrimary)
        private set
    var bg by mutableStateOf(bg)
        private set
    var bgOpposite by mutableStateOf(bgOpposite)
        private set
    var buttonBackground by mutableStateOf(buttonBackground)
        private set
    var buttonIcon by mutableStateOf(buttonIcon)
        private set

    var white by mutableStateOf(white)
        private set

    var black by mutableStateOf(black)
        private set

}

fun lightColors() = with(com.codingbot.algorithm.ui.theme.Color) {
    CustomColors(
        textColorPrimary = Black,
        bg = White,
        bgOpposite = Black,
        buttonBackground = Blue_Gray_50.copy(alpha = .1f),
        buttonIcon = Black.copy(alpha = .1f),
        white = White,
        black = Black,
    )
}

fun darkColors() = with(com.codingbot.algorithm.ui.theme.Color) {
    CustomColors(
        textColorPrimary = White,
        bg = Gray_95,
        bgOpposite = White,
        buttonBackground = Blue_Gray_20.copy(alpha = .1f),
        buttonIcon = White.copy(alpha = .1f),
        white = White,
        black = Black,
    )
}

val LocalColors = staticCompositionLocalOf { lightColors() }
