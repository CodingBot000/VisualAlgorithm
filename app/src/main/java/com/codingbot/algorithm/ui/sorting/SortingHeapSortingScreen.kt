package com.codingbot.algorithm.ui.sorting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.codingbot.algorithm.R
import com.codingbot.algorithm.core.common.Logger
import com.codingbot.algorithm.ui.component.BottomInfoSection
import com.codingbot.algorithm.ui.component.ScreenTitle
import com.codingbot.algorithm.ui.theme.CustomTheme
import com.codingbot.algorithm.ui.theme.Dimens
import com.codingbot.algorithm.viewmodel.SortingHeapSortingViewModel

@Composable
fun SortingHeapSortingScreen(
    navController: NavController,
    sortingViewModel: SortingHeapSortingViewModel = hiltViewModel(),
    screenWidth: Dp = LocalConfiguration.current.screenWidthDp.dp,
    sortingType: String
) {
    val logger = remember { Logger("SortingHeapSortingScreen", true, "[Screen]") }

    val uiState = sortingViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        sortingViewModel.initSorting(sortingType)
    }

    Column(modifier = Modifier
        .background(color = CustomTheme.colors.bg)
        .fillMaxSize())
    {
        ScreenTitle(
            title = sortingType,
            onClickBack = {
                navController.popBackStack()
            }
        )

        Column(modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center)
        {
            Text(
                text = stringResource(id = R.string.result),
                color = CustomTheme.colors.textColorPrimary,
                style = CustomTheme.typography.caption2Regular,
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
            )
            {
                itemsIndexed(uiState.value.heapSortingResultList,
                    key = { index, item -> "$index _$item" })
                { _, item ->
                    val elementWidth = (screenWidth.value / uiState.value.elementList.size).dp - (Dimens.Sorting.SortingScreenHorizontalPadding * 2  / uiState.value.elementList.size)
                    SortingBarCell(
                        sortingBarCellType = SortingBarCellType.SortingResult,
                        item = item,
                        elementWidth = elementWidth
                    )
                }
            }


            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = stringResource(id = R.string.array_temp),
                color = CustomTheme.colors.textColorPrimary,
                style = CustomTheme.typography.caption2Regular
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
            )
            {
                itemsIndexed(uiState.value.elementList,
                    key = { index, item -> "$index _$item" })
                { _, item ->
                    val elementWidth = (screenWidth.value / uiState.value.elementList.size).dp - (Dimens.Sorting.SortingScreenHorizontalPadding * 2  / uiState.value.elementList.size)
                    SortingBarCell(
                        item = item,
                        elementWidth = elementWidth
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            BottomInfoSection(
                sortingType = sortingType,
                moveCount = uiState.value.moveCount,
                startButtonEnable = uiState.value.startButtonEnable,
                forwardButtonEnable = uiState.value.forwardButtonEnable,
                backwardButtonEnable = uiState.value.backwardButtonEnable,
                playState = uiState.value.playState,
                finish = uiState.value.finish,
                onValueChange = { sliderPosition ->
                    sortingViewModel.setSortingSpeed(((10 - sliderPosition.toInt()) * 100).toFloat())
                },
                onClickStart = {
                    sortingViewModel.start()
                },
                onClickResume = {
                    sortingViewModel.resumeSorting()
                },
                onClickPause = {
                    sortingViewModel.pauseSorting()
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
//                onClickStart = {
//                    sortingViewModel.startButtonEnabled(false)
//                    sortingViewModel.start()
//                },
//                onClickReplay = {
//                    sortingViewModel.restart()
//                }
            )
        }
    }
}
