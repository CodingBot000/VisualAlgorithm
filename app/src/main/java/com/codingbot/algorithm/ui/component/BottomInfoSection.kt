package com.codingbot.algorithm.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.codingbot.algorithm.ui.theme.CustomTheme
import com.codingbot.algorithm.viewmodel.PlayState

@Composable
fun BottomInfoSection(moveCount: Int,
                      backwardButtonEnable: Boolean = true,
                      startButtonEnable: Boolean = true,
                      forwardButtonEnable: Boolean = true,
                      playState: PlayState = PlayState.INIT,
                      finish: Boolean,
                      onValueChange: (Float) -> Unit,
                      onClickBackward:() -> Unit = {},
                      onClickStart:() -> Unit,
                      onClickResume:() -> Unit = {},
                      onClickPause:() -> Unit = {},
                      onClickForward:() -> Unit = {},
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

    Row(modifier = Modifier
        .fillMaxWidth())
    {
        Button(
            onClick = {
                onClickBackward()
            },
            enabled = backwardButtonEnable
        ) {
            Text(text = "Backward")
        }

        Button(
            onClick = {
                if (playState == PlayState.INIT) {
                    onClickStart()
                } else if (playState == PlayState.PAUSE) {
                    onClickResume()
                } else if (playState == PlayState.RESUME) {
                    onClickPause()
                } else {
                    onClickPause()
                }
            },
//            enabled = startButtonEnable
        ) {
            if (playState == PlayState.INIT) {
                Text(text = "Start")
            } else if (playState == PlayState.RESUME) {
                Text(text = "Pause")
            } else if (playState == PlayState.PAUSE) {
                Text(text = "Resume")
            } else if (playState == PlayState.PLAYING) {
                Text(text = "Pause")
            } else {
                Text(text = "Pause")
            }
        }

        Button(
            onClick = {
                onClickForward()
            },
            enabled = forwardButtonEnable
        ) {
            Text(text = "Forward")
        }

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