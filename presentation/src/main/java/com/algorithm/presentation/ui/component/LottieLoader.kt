package com.algorithm.presentation.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.LottieRetrySignal
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.algorithm.presentation.R

@Composable
fun LottieLoader(
    lottieRes: Int,
    modifier: Modifier
) {
//    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(lottieRes))
//    LottieAnimation(composition)

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(lottieRes)
    )
    val lottieAnimatable = rememberLottieAnimatable()

    LaunchedEffect(composition) {
        lottieAnimatable.animate(
            iterations = LottieConstants.IterateForever,
            composition = composition,
            clipSpec = LottieClipSpec.Frame(0, 1200),
            initialProgress = 0f
        )
    }

    Box(
        modifier = modifier.wrapContentSize()
    ) {
        LottieAnimation(
            composition = composition,
            progress = lottieAnimatable.progress,
            modifier = Modifier.wrapContentSize(),
            contentScale = ContentScale.FillHeight
        )
    }
}

