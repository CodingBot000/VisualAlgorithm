package com.codingbot.algorithm.ui.component


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.codingbot.algorithm.core.common.Const
import com.codingbot.algorithm.ui.theme.Color

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
