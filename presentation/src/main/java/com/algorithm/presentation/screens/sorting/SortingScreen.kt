package com.algorithm.presentation.screens.sorting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.algorithm.presentation.ui.component.BottomInfoSection
import com.algorithm.presentation.ui.component.LogBottomSheet
import com.algorithm.presentation.ui.component.ScreenTitle
import com.algorithm.presentation.ui.component.TopIcon
import com.algorithm.presentation.ui.theme.CustomTheme
import com.algorithm.presentation.ui.theme.Dimens
import com.algorithm.presentation.viewmodel.SortingUiState
import com.algorithm.presentation.viewmodel.SortingViewModel

@Composable
fun SortingScreen(
    navController: NavController,
    sortingViewModel: SortingViewModel = hiltViewModel(),
    screenWidth: Dp = LocalConfiguration.current.screenWidthDp.dp,
    sortingType: String
) {
    val logger = remember { com.algorithm.utils.Logger("SortingScreen", true, "[Screen]") }

    val uiState = sortingViewModel.uiState.collectAsStateWithLifecycle()
    var isLogBottomSheetOpen by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        sortingViewModel.initValue(sortingType)
    }

    Column(modifier = Modifier
        .background(color = CustomTheme.colors.bg)
        .padding(bottom = 5.dp)
        .fillMaxSize(),
        verticalArrangement = Arrangement.Center)
    {
        ScreenTitle(
            title = sortingType,
            onClickBack = {
                navController.popBackStack()
            },
            trailingIcon = {
                TopIcon(
                    onClick = {
                        isLogBottomSheetOpen = !isLogBottomSheetOpen
                    }
                )
            }
        )
        middleContent(
            uiState = uiState,
            screenWidth = screenWidth
        )
        bottomContent(
            uiState = uiState,
            sortingType = sortingType,
            onValueChange = { sliderPosition ->
                sortingViewModel.setSpeedValue(((10 - sliderPosition.toInt()) * 100).toFloat())
            },
            onClickStart = {
                sortingViewModel.start()
            },
            onClickResume = {
                sortingViewModel.resume()
            },
            onClickPause = {
                sortingViewModel.pause()
            },
            onClickForward = {
                sortingViewModel.forward()
            },
            onClickBackward = {
                sortingViewModel.backward()
            },
            onClickReplay = {
                sortingViewModel.restart()
            }
        )

        if (isLogBottomSheetOpen) {
            LogBottomSheet(
                logHistoryString = sortingViewModel.getHistoryList().toString(),
                closeSheet = { isLogBottomSheetOpen = false }
            )
        }
    }
}

@Composable
private fun ColumnScope.middleContent(
    uiState: State<SortingUiState>,
    screenWidth: Dp)
{
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        contentAlignment = Alignment.Center
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.Sorting.SortingScreenHorizontalPadding)
        )
        {
            itemsIndexed(uiState.value.elementList,
                key = { index, item -> "$index _$item" })
            { _, item ->
                val elementWidth =
                    (screenWidth.value / uiState.value.elementList.size).dp - (Dimens.Sorting.SortingScreenHorizontalPadding * 2 / uiState.value.elementList.size)
                SortingBarCell(
                    item = item,
                    elementWidth = elementWidth
                )
            }
        }
    }
}


@Composable
private fun ColumnScope.bottomContent(
    uiState: State<SortingUiState>,
    sortingType: String,
    onValueChange: (Float) -> Unit,
    onClickBackward:() -> Unit = {},
    onClickStart:() -> Unit,
    onClickResume:() -> Unit,
    onClickPause:() -> Unit,
    onClickForward:() -> Unit = {},
    onClickReplay:() -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        BottomInfoSection(
            algorithmType = sortingType,
            moveCount = uiState.value.moveCount,
            startButtonEnable = uiState.value.startButtonEnable,
            forwardButtonEnable = uiState.value.forwardButtonEnable,
            backwardButtonEnable = uiState.value.backwardButtonEnable,
            playState = uiState.value.playState,
            finish = uiState.value.finish,
            onValueChange = { sliderPosition ->
                onValueChange(sliderPosition)
            },
            onClickStart = {
                onClickStart()
            },
            onClickResume = {
                onClickResume()
            },
            onClickPause = {
                onClickPause()
            },
            onClickForward = {
                onClickForward()
            },
            onClickBackward = {
                onClickBackward()
            },
            onClickReplay = {
                onClickReplay()
            }
        )
    }
}

