package com.codingbot.algorithm.ui.sorting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.codingbot.algorithm.core.common.Logger
import com.codingbot.algorithm.ui.component.ScreenTitle
import com.codingbot.algorithm.viewmodel.SortingViewModel

@Composable
fun SortingScreen(
    navController: NavController,
    sortingViewModel: SortingViewModel = hiltViewModel(),
    sortingType: String
) {
    val logger = remember { Logger("SortingScreen", true, "[Screen]") }
    logger { "sortingViewModel:$sortingViewModel" }
    val uiState = sortingViewModel.uiState.collectAsStateWithLifecycle()
    var sliderPosition by remember { mutableStateOf(5f) }
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    LaunchedEffect(key1 = Unit) {
        sortingViewModel.initSorting(sortingType)
    }

    Column(modifier = Modifier
        .fillMaxSize())
    {
        ScreenTitle(
            title = sortingType,
            onClickBack = {
                navController.popBackStack()
            }
        )

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {

            LazyRow(
                modifier = Modifier.fillMaxWidth()
            )
            {
                itemsIndexed(uiState.value.elementList,
                    key = { index, item -> "$index _$item" })
                { index, item ->
                    SortingBarCell(
                        item = item,
                        screenWidth = screenWidth,
                        listSize = uiState.value.elementList.size)
                }
            }

            BottomInfoSection(
                moveCount = uiState.value.moveCount,
                startButtonEnable = uiState.value.startButtonEnable,
                finish = uiState.value.finish,
                onValueChange = { sliderPosition ->
                    sortingViewModel.setSortingSpeed(((10 - sliderPosition.toInt()) * 100).toFloat())
                },
                onClickStart = {
                    sortingViewModel.startButtonEnabled(false)
                    sortingViewModel.start()
                },
                onClickReplay = {
                    sortingViewModel.restart()
                }
            )
        }
    }
}


