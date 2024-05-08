package com.codingbot.algorithm.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.codingbot.algorithm.R
import com.codingbot.algorithm.core.common.GraphList
import com.codingbot.algorithm.core.common.SortingList
import com.codingbot.algorithm.ui.theme.CustomTheme
import com.codingbot.algorithm.ui.theme.Dimens
import com.codingbot.algorithm.viewmodel.PlayState

@Composable
fun BottomInfoSection(sortingType: String,
                      moveCount: Int,
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

    val hiddenButton = remember(sortingType) {
//        !arrayListOf(GraphList.BFS.name, GraphList.DFS.name, SortingList.HEAP_SORT.name).contains(sortingType)
        !arrayListOf(GraphList.BFS.name, GraphList.DFS.name).contains(sortingType)
    }

    Row(modifier = Modifier
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center)
    {

        if (hiddenButton) {
            Button(
                onClick = {
                    onClickBackward()
                },
                enabled = backwardButtonEnable,
            ) {
                bottomIcon(resId = R.drawable.icon_redo_48px)
            }
            Spacer(Modifier.width(10.dp))
        }

        Button(
            onClick = {
                when(playState) {
                    PlayState.INIT -> onClickStart()
                    PlayState.PAUSE -> onClickResume()
                    PlayState.RESUME, PlayState.PLAYING -> onClickPause()
                    else -> {}
                }
            },
            enabled = startButtonEnable
        ) {
            var resId = when(playState) {
                PlayState.INIT -> R.drawable.icon_play_circle_48px
                PlayState.PAUSE -> R.drawable.icon_play_circle_48px
                PlayState.RESUME, PlayState.PLAYING -> R.drawable.icon_pause_circle_48px
                else -> R.drawable.icon_pause_circle_48px
            }
            bottomIcon(resId = resId)
        }
        Spacer(Modifier.width(10.dp))
        if (hiddenButton) {
            Button(
                onClick = {
                    onClickForward()
                },
                enabled = forwardButtonEnable
            ) {
                bottomIcon(resId = R.drawable.icon_fast_forward_48px)
            }
            Spacer(Modifier.width(10.dp))
        }

//        Button(
//            onClick = {
//                onClickReplay()
//            },
//            enabled = finish
//        ) {
//            bottomIcon(resId = R.drawable.icon_refresh_48px)
//        }


    }
}

@Composable
private fun bottomIcon(
    resId: Int
) {
    Icon(
        modifier = Modifier.size(Dimens.Size.S20)
            .background(CustomTheme.colors.buttonBackground),
        painter = painterResource(id = resId),
        contentDescription = null,
        tint = CustomTheme.colors.buttonIcon.copy(alpha = .9f)
    )
}