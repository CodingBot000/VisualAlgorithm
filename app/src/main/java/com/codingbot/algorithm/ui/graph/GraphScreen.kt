package com.codingbot.algorithm.ui.graph

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.codingbot.algorithm.core.common.Const
import com.codingbot.algorithm.core.common.Logger
import com.codingbot.algorithm.viewmodel.SortingViewModel

@Composable
fun GraphScreen(
    navController: NavController,
    sortingViewModel: SortingViewModel
) {
    val logger = remember { Logger("SortingScreen", true, "[Screen]") }
    logger { "sortingViewModel:$sortingViewModel" }
    val uiState = sortingViewModel.uiState.collectAsStateWithLifecycle()
    var sliderPosition by remember { mutableStateOf(Const.sortingSpeedSliderInit) }


    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Button(onClick = {

        }) {

        }

        LazyRow(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            contentPadding = PaddingValues(10.dp)
        )
        {

            logger { "uiState.value.elementSelected:${uiState.value.elementSelected}"}

            itemsIndexed(uiState.value.elementList,
                key = { index, item -> "$index _$item" })
            { index, item ->

                logger { "itemsIndexed index:$index item:${item.element}"}
                Text(
                    modifier = Modifier.background(if (item.swap1 || item.swap2)
                    {
                        Color.Yellow   // 조건이 참이면 파란색
                    } else {
                        Color.White    // 조건이 거짓이면 빨간색
                    }),
                    text = item.element.toString(),
                    color = Color.Black,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.width(10.dp))

            }
        }

        Text(text = "Sorting Count: ${uiState.value.moveCount}")
        Text(text = "interval speed: ${sliderPosition.toInt()}")
        Slider(
            value = sliderPosition,
            onValueChange = { newValue ->
                sliderPosition = newValue
                sortingViewModel.setSortingSpeed(((10 - sliderPosition.toInt()) * 100).toFloat())
            },
            valueRange = 0f..10f,
            steps = 9  // 0부터 10까지 1씩 증가하므로 총 9개의 간격이 필요합니다.
        )

    }

}


