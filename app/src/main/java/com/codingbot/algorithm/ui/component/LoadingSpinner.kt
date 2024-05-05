package com.codingbot.algorithm.ui.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.codingbot.algorithm.R
import com.codingbot.algorithm.ui.theme.Color

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
fun LoadingSpinnerCircleBox(
    modifier: Modifier = Modifier,
    boxSize: Dp = 40.dp,
    size: Dp = 24.dp,
    painterResId: Int = if (isSystemInDarkTheme()) R.drawable.icon_spinner_dark else R.drawable.icon_spinner_light
) {
    Box(
        modifier = modifier
            .size(boxSize)
            .background(Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        LoadingSpinner(modifier, size, painterResId)
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
fun LoadingSpinner(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    painterResId: Int = if (isSystemInDarkTheme()) R.drawable.icon_spinner_dark else R.drawable.icon_spinner_light
) {
    val transition = rememberInfiniteTransition(label = "rotateTransition")
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        label = "rotation",
        animationSpec = infiniteRepeatable(tween(1337, easing = LinearEasing))
    )

    Image(
        contentDescription = null,
        painter = painterResource(id = painterResId),
        modifier = modifier.size(size).graphicsLayer { rotationZ = rotation }
    )
}