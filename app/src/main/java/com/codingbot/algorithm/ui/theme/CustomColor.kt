package com.codingbot.algorithm.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

class CustomColors(
    bg: Color,
    bgOpposite: Color,
    surface: Color,
    surfaceBlue: Color,
    label1: Color,
    label1Opposite: Color,
    label1OnPrimary: Color,
    label2: Color,
    label2OnPrimary: Color,
    label3: Color,
    label3OnPrimary: Color,
    labelPositive: Color,
    primary: Color,
    shadow: Color,
    error: Color,
    unactivated: Color,
    white: Color,
    black: Color,
    dimNormal: Color,
    dimThick: Color,
    dimLight: Color,
    dimLight10: Color,
    dimDark: Color,
    bgNavy: Color,
    labelLive: Color,
    surfaceBlueThick: Color,
    surfaceBlueLight: Color,
    affirmative: Color,
    primaryInactive: Color,
    border1: Color,
    surfaceGray: Color
) {
    val textColor = Color.Black

    var bg by mutableStateOf(bg)
        private set

    var bgOpposite by mutableStateOf(bgOpposite)
        private set

    var surface by mutableStateOf(surface)
        private set

    var surfaceBlue by mutableStateOf(surfaceBlue)
        private set

    var label1 by mutableStateOf(label1)
        private set

    var label1Opposite by mutableStateOf(label1Opposite)
        private set

    var label1OnPrimary by mutableStateOf(label1OnPrimary)
        private set

    var label2 by mutableStateOf(label2)
        private set

    var label2OnPrimary by mutableStateOf(label2OnPrimary)
        private set

    var label3 by mutableStateOf(label3)
        private set

    var label3OnPrimary by mutableStateOf(label3OnPrimary)
        private set

    var labelPositive by mutableStateOf(labelPositive)
        private set

    var primary by mutableStateOf(primary)
        private set

    var shadow by mutableStateOf(shadow)
        private set

    var error by mutableStateOf(error)
        private set

    var unactivated by mutableStateOf(unactivated)
        private set

    var white by mutableStateOf(white)
        private set

    var black by mutableStateOf(black)
        private set

    var dimNormal by mutableStateOf(dimNormal)
        private set

    var dimThick by mutableStateOf(dimThick)
        private set

    var dimLight by mutableStateOf(dimLight)
        private set

    var dimLight10 by mutableStateOf(dimLight10)
        private set

    var bgNavy by mutableStateOf(bgNavy)
        private set

    var labelLive by mutableStateOf(labelLive)
        private set

    var surfaceBlueThick by mutableStateOf(surfaceBlueThick)
        private set

    var surfaceBlueLight by mutableStateOf(surfaceBlueLight)
        private set

    val dimDark by mutableStateOf(dimDark)

    val affirmative by mutableStateOf(affirmative)

    val primaryInactive by mutableStateOf(primaryInactive)

    val border1 by mutableStateOf(border1)

    val surfaceGray by mutableStateOf(surfaceGray)
}

fun lightColors() = with(com.codingbot.algorithm.ui.theme.Color) {
    CustomColors(
        bg = White,
        bgOpposite = Gray_95,
        surface = Gray_5,
        surfaceBlue = Blue_Gray_10,
        label1 = Black,
        label1Opposite = White,
        label1OnPrimary = White,
        label2 = Gray_60,
        label2OnPrimary = Blue_20,
        label3 = Gray_30,
        label3OnPrimary = Blue_Gray_60,
        labelPositive = Pink,
        primary = Black,
        shadow = Black.copy(alpha = .1f),
        error = Error,
        unactivated = Gray_20,
        white = White,
        black = Black,
        dimNormal = Black.copy(alpha = .4f),
        dimThick = Black.copy(alpha = .6f),
        dimLight = White.copy(alpha = .4f),
        dimLight10 = White.copy(alpha = .1f),
        bgNavy = Navy,
        labelLive = Skyblue,
        surfaceBlueThick = Blue_Gray_20,
        surfaceBlueLight = Blue_Gray_5,
        dimDark = Gray_90,
        affirmative = Green,
        primaryInactive = Blue_60,
        border1 = Gray_10,
        surfaceGray = Gray_5
    )
}

fun darkColors() = with(com.codingbot.algorithm.ui.theme.Color) {
    CustomColors(
        bg = Gray_95,
        bgOpposite = White,
        surface = Gray_90,
        surfaceBlue = Blue_Gray_80,
        label1 = White,
        label1Opposite = Black,
        label1OnPrimary = White,
        label2 = Gray_40,
        label2OnPrimary = Blue_20,
        label3OnPrimary = Blue_Gray_60,
        labelPositive = Pink,
        label3 = Gray_70,
        primary = White,
        shadow = White.copy(alpha = .1f),
        error = Error,
        unactivated = Gray_80,
        white = White,
        black = Black,
        dimNormal = Black.copy(alpha = .4f),
        dimThick = Black.copy(alpha = .6f),
        dimLight = White.copy(alpha = .4f),
        dimLight10 = White.copy(alpha = .1f),
        bgNavy = Navy,
        labelLive = Skyblue,
        surfaceBlueThick = Blue_Gray_70,
        surfaceBlueLight = Blue_Gray_80,
        dimDark = Gray_90,
        affirmative = Green,
        primaryInactive = Blue_60,
        border1 = Gray_80,
        surfaceGray = Gray_5
    )
}

val LocalColors = staticCompositionLocalOf { lightColors() }
