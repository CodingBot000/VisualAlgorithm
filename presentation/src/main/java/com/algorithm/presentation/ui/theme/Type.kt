package com.algorithm.presentation.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.algorithm.presentation.R


private val NotoSansKr = FontFamily(
    Font(R.font.notosanskr_bold, FontWeight.Bold),
    Font(R.font.notosanskr_regular, FontWeight.Normal)
)

private val Int.px: TextUnit
    @Composable get() = with(LocalDensity.current) { dp.toSp() }

@Immutable
class CustomTypography {

    val caption2Regular: TextStyle
        @Composable get() = TextStyle(
            fontSize = 10.px,
            fontFamily = NotoSansKr,
            fontWeight = FontWeight.Normal,
            lineHeight = 14.px
        )

    val captionRegular: TextStyle
        @Composable get() = TextStyle(
            fontSize = 12.px,
            fontFamily = NotoSansKr,
            fontWeight = FontWeight.Normal,
            lineHeight = 16.px
        )

    val footnoteRegular: TextStyle
        @Composable get() = TextStyle(
            fontSize = 14.px,
            fontFamily = NotoSansKr,
            fontWeight = FontWeight.Normal,
            lineHeight = 19.px
        )

    val bodyRegular: TextStyle
        @Composable get() = TextStyle(
            fontSize = 16.px,
            fontFamily = NotoSansKr,
            fontWeight = FontWeight.Normal,
            lineHeight = 21.px
        )

    val headlineRegular: TextStyle
        @Composable get() = TextStyle(
            fontSize = 18.px,
            fontFamily = NotoSansKr,
            fontWeight = FontWeight.Normal,
            lineHeight = 23.px
        )

    val title3Regular: TextStyle
        @Composable get() = TextStyle(
            fontSize = 20.px,
            fontFamily = NotoSansKr,
            fontWeight = FontWeight.Normal,
            lineHeight = 25.px
        )

    val title2Regular: TextStyle
        @Composable get() = TextStyle(
            fontSize = 22.px,
            fontFamily = NotoSansKr,
            fontWeight = FontWeight.Normal,
            lineHeight = 28.px
        )

    val title1Regular: TextStyle
        @Composable get() = TextStyle(
            fontSize = 28.px,
            fontFamily = NotoSansKr,
            fontWeight = FontWeight.Normal,
            lineHeight = 34.px
        )

    val largeTitleRegular: TextStyle
        @Composable get() = TextStyle(
            fontSize = 34.px,
            fontFamily = NotoSansKr,
            fontWeight = FontWeight.Normal,
            lineHeight = 41.px
        )

    val caption2Bold: TextStyle
        @Composable get() = TextStyle(
            fontSize = 10.px,
            fontFamily = NotoSansKr,
            fontWeight = FontWeight.Bold,
            lineHeight = 14.px
        )
    val caption2ExtraBold: TextStyle
        @Composable get() = TextStyle(
            fontSize = 10.px,
            fontFamily = NotoSansKr,
            fontWeight = FontWeight.ExtraBold,
            lineHeight = 14.px
        )

    val captionBold: TextStyle
        @Composable get() = TextStyle(
            fontSize = 12.px,
            fontFamily = NotoSansKr,
            fontWeight = FontWeight.Bold,
            lineHeight = 16.px
        )

    val footnoteBold: TextStyle
        @Composable get() = TextStyle(
            fontSize = 14.px,
            fontFamily = NotoSansKr,
            fontWeight = FontWeight.Bold,
            lineHeight = 19.px
        )

    val bodyBold: TextStyle
        @Composable get() = TextStyle(
            fontSize = 16.px,
            fontFamily = NotoSansKr,
            fontWeight = FontWeight.Bold,
            lineHeight = 21.px
        )

    val headlineBold: TextStyle
        @Composable get() = TextStyle(
            fontSize = 18.px,
            fontFamily = NotoSansKr,
            fontWeight = FontWeight.Bold,
            lineHeight = 23.px
        )

    val title3Bold: TextStyle
        @Composable get() = TextStyle(
            fontSize = 20.px,
            fontFamily = NotoSansKr,
            fontWeight = FontWeight.Bold,
            lineHeight = 25.px
        )

    val title2Bold: TextStyle
        @Composable get() = TextStyle(
            fontSize = 22.px,
            fontFamily = NotoSansKr,
            fontWeight = FontWeight.Bold,
            lineHeight = 28.px
        )

    val title1Bold: TextStyle
        @Composable get() = TextStyle(
            fontSize = 28.px,
            fontFamily = NotoSansKr,
            fontWeight = FontWeight.Bold,
            lineHeight = 34.px
        )

    val largeTitleBold: TextStyle
        @Composable get() = TextStyle(
            fontSize = 34.px,
            fontFamily = NotoSansKr,
            fontWeight = FontWeight.Bold,
            lineHeight = 41.px
        )
}

val LocalTypography = staticCompositionLocalOf { CustomTypography() }
