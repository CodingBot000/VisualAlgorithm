package com.codingbot.algorithm.ui.component


import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import com.codingbot.algorithm.core.common.Const
import com.codingbot.algorithm.ui.theme.Color

fun Modifier.scaleWhenPressed(
    pressed: Boolean,
    percentage: Float = .95f
): Modifier = composed {
    val scale = animateFloatAsState(
        targetValue = if (pressed) percentage else 1f,
        animationSpec = tween(100, easing = LinearEasing)
    )

    graphicsLayer {
        scaleX = scale.value
        scaleY = scale.value
    }
}

fun Modifier.scaleWhenPressed(
    onClick: () -> Unit,
    enabled: Boolean = true,
    percentage: Float = .95f
): Modifier = composed {
    var pressed by remember { mutableStateOf(false) }
    val updatedOnClick by rememberUpdatedState(onClick)

    val scale = animateFloatAsState(
        targetValue = if (pressed) percentage else 1f,
        animationSpec = tween(100, easing = LinearEasing)
    )

    graphicsLayer {
        scaleX = scale.value
        scaleY = scale.value
    }.pointerInput(enabled) {
        if (enabled) detectTapGestures(
            onTap = { updatedOnClick() },
            onPress = {
                pressed = true
                tryAwaitRelease()
                pressed = false
            }
        )
    }
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.clickableSingle(
    enabled: Boolean = true,
    onClick: () -> Unit
) = composed {
    val multipleEventsCutter = remember(::MultipleEventsCutterImpl)

    clickable(
        enabled = enabled,
        indication = null,
        interactionSource = remember(::MutableInteractionSource),
        onClick = { multipleEventsCutter.processEvent(onClick) }
    )
}

fun Modifier.clickableSingle(
    enabled: Boolean = true,
    percentage: Float,
    onClick: () -> Unit
) = composed {
    val multipleEventsCutter = remember(::MultipleEventsCutterImpl)

    scaleWhenPressed(
        enabled = enabled,
        percentage = percentage,
        onClick = {
            multipleEventsCutter.processEvent(onClick)
        }
    )
}

internal interface MultipleEventsCutter {
    fun processEvent(event: () -> Unit)

}

private class MultipleEventsCutterImpl : MultipleEventsCutter {

    private val now: Long
        get() = System.currentTimeMillis()

    private var lastProcessedMillis: Long = now
    private val debounceMillis = 500L
    private val globalDebounceMillis = 100L

    override fun processEvent(event: () -> Unit) {
        val now = now
        if (now - Const.globalMultipleInputTime <= globalDebounceMillis) {
            return
        }
        Const.globalMultipleInputTime = now
        if (now - lastProcessedMillis >= debounceMillis) {
            event()
        }
        lastProcessedMillis = now
    }
}

/**
 *  리스트 아이템(clickable 인경우만) 기본/pressed 상태 배경색 지정
 */
@Composable
fun Modifier.listItemClickable(
    isWhite: Boolean = true,
    onClick: () -> Unit = {}
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState()
    val basicColor = if (isWhite) Color.Blue_50 else Color.Blue_10
    val pressedColor = if (isWhite) Color.Blue_90 else Color.Blue_70
    return this
        .background(color = if(isPressed.value) pressedColor else basicColor)
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick,
        )
}
