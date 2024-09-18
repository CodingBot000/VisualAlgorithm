package com.algorithm.presentation.ui.component


import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.algorithm.common.Const

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
        if (now - com.algorithm.common.Const.globalMultipleInputTime <= globalDebounceMillis) {
            return
        }
        com.algorithm.common.Const.globalMultipleInputTime = now
        if (now - lastProcessedMillis >= debounceMillis) {
            event()
        }
        lastProcessedMillis = now
    }
}