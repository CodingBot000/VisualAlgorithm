package com.codingbot.algorithm.ui.sorting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.codingbot.algorithm.core.common.Logger
import com.codingbot.algorithm.ui.component.ScreenTitle
import com.codingbot.algorithm.ui.theme.Color
import com.codingbot.algorithm.viewmodel.SortingHeapSortingViewModel

@Composable
fun SortingHeapSortingScreen(
    navController: NavController,
    sortingViewModel: SortingHeapSortingViewModel = hiltViewModel(),
    sortingType: String
) {
    val logger = remember { Logger("SortingHeapSortingScreen", true, "[Screen]") }
    logger { "sortingViewModel:$sortingViewModel" }
    val uiState = sortingViewModel.uiState.collectAsStateWithLifecycle()

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
            .padding(horizontal = 10.dp)
            .fillMaxSize(),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {

            Text(
                text = "Result",
                color = Color.Black,
                fontSize = 10.sp,
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
            )
            {
                itemsIndexed(uiState.value.heapSortingResultList,
                    key = { index, item -> "$index _$item" })
                { index, item ->
                    SortingBarCell(
                        sortingBarCellType = SortingBarCellType.SortingResult,
                        item = item,
                        screenWidth = screenWidth,
                        listSize = uiState.value.heapSortingResultList.size)
                }
            }
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(color = Color.Gray_50) // 여기서 Color.Gray_50가 유효한지 확인 필요, 일반적으로 Color.Gray 혹은 다른 정의된 색상 사용
//            ) {
//                uiState.value.heapSortingResultList.forEach { item ->
//                    SortingBarCell(
//                        sortingBarCellType = SortingBarCellType.SortingResult,
//                        item = item,
//                        screenWidth = screenWidth,
//                        listSize = uiState.value.heapSortingResultList.size
//                    )
//                }
//            }

            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Array Temp",
                color = Color.Black,
                fontSize = 10.sp,
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
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
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(color = Color.Gray_50) // 여기서 Color.Gray_50가 유효한지 확인 필요, 일반적으로 Color.Gray 혹은 다른 정의된 색상 사용
//            ) {
//                uiState.value.elementList.forEach { item ->
//                    SortingBarCell(
//                        sortingBarCellType = SortingBarCellType.SortingResult,
//                        item = item,
//                        screenWidth = screenWidth,
//                        listSize = uiState.value.elementList.size
//                    )
//                }
//            }

            Spacer(modifier = Modifier.weight(1f))
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
