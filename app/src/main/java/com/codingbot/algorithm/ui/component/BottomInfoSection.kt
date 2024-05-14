package com.codingbot.algorithm.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codingbot.algorithm.R
import com.codingbot.algorithm.core.common.GraphList
import com.codingbot.algorithm.ui.theme.CustomTheme
import com.codingbot.algorithm.viewmodel.PlayState

@Composable
fun BottomInfoSection(algorithmType: String,
                      moveCount: Int,
                      backwardButtonEnable: Boolean = true,
                      startButtonEnable: Boolean = true,
                      forwardButtonEnable: Boolean = true,
                      playState: PlayState = PlayState.INIT,
                      finish: Boolean,
                      onValueChange: (Float) -> Unit,
                      onClickBackward:() -> Unit = {},
                      onClickStart:() -> Unit,
                      onClickResume:() -> Unit,
                      onClickPause:() -> Unit,
                      onClickForward:() -> Unit = {},
                      onClickReplay:() -> Unit = {}) {
    var sliderPosition by remember { mutableStateOf(5f) }
    val graphTracking = remember(algorithmType) {
        arrayListOf(GraphList.BFS.name, GraphList.DFS.name).contains(algorithmType)
    }
    Column(modifier = Modifier
        .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Text(
            text = if (graphTracking) {
                    "Sorting Count: "
                } else {
                    "Graph Tracking Count: "
                }
                    + moveCount,
            color = CustomTheme.colors.textColorPrimary)
        Text(
            text = "interval speed: ${sliderPosition.toInt()}",
            color = CustomTheme.colors.textColorPrimary)
        Slider(
            modifier = Modifier
                .padding(horizontal = 10.dp),
            value = sliderPosition,
            onValueChange = { newValue ->
                sliderPosition = newValue
                onValueChange(sliderPosition)
            },
            valueRange = 0f..10f,
            steps = 9
        )

//        val hiddenButton = remember(algorithmType) {
//            !arrayListOf(GraphList.BFS.name, GraphList.DFS.name).contains(sortingType)
//        }

        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center)
        {
            ControlButton(
                resId = R.drawable.icon_redo_48px,
                onClick = {
                    onClickBackward()
                },
                isEnable = backwardButtonEnable
            )

            Spacer(Modifier.width(10.dp))

            ControlButton(
                resId =
                when (playState) {
                    PlayState.INIT, PlayState.PAUSE -> R.drawable.icon_play_circle_48px
                    PlayState.RESUME, PlayState.PLAYING -> R.drawable.icon_pause_circle_48px
                    else -> R.drawable.icon_pause_circle_48px
                },
                onClick = {
                    when (playState) {
                        PlayState.INIT -> onClickStart()
                        PlayState.PAUSE -> onClickResume()
                        PlayState.RESUME, PlayState.PLAYING -> onClickPause()
                        else -> {}
                    }
                },
                isEnable = startButtonEnable
            )

            Spacer(Modifier.width(10.dp))
            ControlButton(
                resId = R.drawable.icon_fast_forward_48px,
                onClick = {
                    onClickForward()
                },
                isEnable = forwardButtonEnable
            )
            Spacer(Modifier.width(10.dp))

//            ControlButton(
//                resId = R.drawable.icon_refresh_48px,
//                onClick = {
//                    onClickReplay
//                },
//            )
        }
    }
}
