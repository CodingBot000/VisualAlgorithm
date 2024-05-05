package com.codingbot.algorithm.ui.component

import androidx.compose.material.Button
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.codingbot.algorithm.ui.theme.CustomTheme


@Composable
fun BottomInfoSection(moveCount: Int,
                      startButtonEnable: Boolean,
                      finish: Boolean,
                      onValueChange: (Float) -> Unit,
                      onClickStart:() -> Unit,
                      onClickReplay:() -> Unit) {
    var sliderPosition by remember { mutableStateOf(5f) }
    Text(text = "Sorting Count: $moveCount",
        color = CustomTheme.colors.textColorPrimary)
    Text(text = "interval speed: ${sliderPosition.toInt()}",
        color = CustomTheme.colors.textColorPrimary)
    Slider(
        value = sliderPosition,
        onValueChange = { newValue ->
            sliderPosition = newValue
            onValueChange(sliderPosition)
        },
        valueRange = 0f..10f,
        steps = 9
    )

    Button(
        onClick = {
            onClickStart()
        },
        enabled = startButtonEnable
    ) {
        Text(text = "Start")
    }

    Button(
        onClick = {
            onClickReplay()
        },
        enabled = finish
    ) {
        Text(text = "Replay")
    }
}
