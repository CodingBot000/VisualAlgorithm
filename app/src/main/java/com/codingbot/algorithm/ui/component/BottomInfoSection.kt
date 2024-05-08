package com.codingbot.algorithm.ui.component

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
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

    Column(modifier = Modifier
        .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Text(
            text = "Sorting Count: $moveCount",
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

        val hiddenButton = remember(sortingType) {
            !arrayListOf(GraphList.BFS.name, GraphList.DFS.name).contains(sortingType)
        }

        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center)
        {

            if (hiddenButton) {
                CustomButton(
                    imageVector = ImageVector.vectorResource(R.drawable.icon_redo_48px),
                    onClick = {
                        onClickBackward()
                    },
                    isEnable = backwardButtonEnable
                )

                Spacer(Modifier.width(10.dp))
            }

            CustomButton(
                imageVector =
                when (playState) {
                    PlayState.INIT, PlayState.PAUSE -> ImageVector.vectorResource(R.drawable.icon_play_circle_48px)
                    PlayState.RESUME, PlayState.PLAYING -> ImageVector.vectorResource(R.drawable.icon_pause_circle_48px)
                    else -> ImageVector.vectorResource(R.drawable.icon_pause_circle_48px)
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
            if (hiddenButton) {
                CustomButton(
                    imageVector = ImageVector.vectorResource(R.drawable.icon_fast_forward_48px),
                    onClick = {
                        onClickForward()
                    },
                    isEnable = forwardButtonEnable
                )
                Spacer(Modifier.width(10.dp))
            }
        }
    }
}

@Preview
@Composable
fun CenterAlignedTexts() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "aaaa")
        Text(text = "bbbb")
        Text(
            text = "Sorting Count:")
    }
}

@Composable
fun CustomButton(
    imageVector: ImageVector,
//    resId: Int,
    onClick:() -> Unit = {},
    isEnable: Boolean = false
) {
    Button(
        onClick = {
           onClick()
        }, colors = ButtonDefaults.buttonColors(
            backgroundColor = CustomTheme.colors.buttonBackground,
            contentColor = Color.Black,
            disabledBackgroundColor = CustomTheme.colors.buttonBackgroundDisabled,
            disabledContentColor = Color.Black
        ), enabled = isEnable,
        shape = RoundedCornerShape(10.dp),
//        border = BorderStroke(width = 2.dp, color = Color.Blue),
        content = {
            Image(imageVector = imageVector, contentDescription = "")
        })
}
@Composable
private fun bottomIcon(
    resId: Int,
    onClick:() -> Unit = {},
    isEnable: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(Dimens.Size.S20),
        enabled = isEnable,
//        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
    ) {
        // 리소스 이미지 사용
        Image(
//            painter = if (isEnable) {
                painterResource(id = resId)
//            } else {
//                BitmapPainter(bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888))
//            },
                    ,
            contentDescription = null // contentDescription은 생략합니다.
        )
    }
//    Icon(
//        modifier = Modifier.size(Dimens.Size.S20)
//            .background(CustomTheme.colors.buttonBackground)
//            .clickable {
//                onClick()
//            },
//        painter = painterResource(id = resId),
//        contentDescription = null,
//        tint = CustomTheme.colors.buttonIcon.copy(alpha = .9f),
//    )
}