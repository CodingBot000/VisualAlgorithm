package com.codingbot.algorithm.ui.graph

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.codingbot.algorithm.core.common.Logger
import com.codingbot.algorithm.ui.component.BottomInfoSection
import com.codingbot.algorithm.ui.component.ScreenTitle
import com.codingbot.algorithm.ui.theme.Color
import com.codingbot.algorithm.ui.theme.CustomTheme
import com.codingbot.algorithm.viewmodel.GraphViewModel


@Composable
fun GraphScreen(
    navController: NavController,
    graphViewModel: GraphViewModel = hiltViewModel(),
    graphType: String
) {
    val logger = remember { Logger("GraphScreen", true, "[Screen]") }

    val uiState = graphViewModel.uiState.collectAsStateWithLifecycle()
    val startIdx by remember { mutableStateOf(graphViewModel.startIdx) }
    val destIdx by remember { mutableStateOf(graphViewModel.destIdx) }

    LaunchedEffect(key1 = Unit) {
        graphViewModel.initTrackingMaze(graphType)
    }

    Column(modifier = Modifier
        .background(color = CustomTheme.colors.bg)
        .fillMaxSize())
    {
        ScreenTitle(
            title = graphType,
            onClickBack = {
                navController.popBackStack()
            }
        )

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {

            Box {
                baseGrid(graphViewModel.baseGridArray, startIdx, destIdx)
                overlayGrid(visitedArray = uiState.value.visitedList, startIdx, destIdx)
            }

            BottomInfoSection(
                sortingType = graphType,
                moveCount = uiState.value.moveCount,
                startButtonEnable = uiState.value.startButtonEnable,
                finish = uiState.value.finish,
                onValueChange = { sliderPosition ->
                    graphViewModel.setSpeed(((10 - sliderPosition.toInt()) * 100).toFloat())
                },
                onClickStart = {
                    graphViewModel.startButtonEnabled(false)
                    graphViewModel.start()
                },
                onClickReplay = {
                    graphViewModel.restart()
                }
            )
        }
    }
}


@Composable
private fun overlayGrid(visitedArray: List<Boolean>, startIdx: Int, destIdx: Int) {
    val logger = remember { Logger("GraphScreen", true, "[Screen]") }
//    val array = remember { visitedArray.flatMap { it.asList() } }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val columns = 5 // The number of columns in the grid
    val boxSize = screenWidth / columns
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        content = {
            logger { "qqqqq  visitedList overlayGrid array: $visitedArray" }
            items(visitedArray.count()) { index ->

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .alpha(if (visitedArray[index]) 0.5f else 0.0f)
                        .border(1.dp, color = Color.Red_50)
                        .then(if (visitedArray[index]) Modifier.background(Color.Pink40) else Modifier),
                    contentAlignment = Alignment.Center
                ) {
                    startAndGoalFlag(
                        curIdx = index,
                        startIdx = startIdx,
                        destIdx = destIdx)
                }

            }
        }
    )
}


@Composable
private fun baseGrid(mazeArray: Array<IntArray>, startIdx: Int, destIdx: Int) {
    val array = remember { mazeArray.flatMap { it.asList() } }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val columns = 5 // The number of columns in the grid
    val boxSize = screenWidth / columns
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        content = {
            items(array.count()) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .background(
                            if (array[index] == 0) {
                                Color.Yellow
                            } else {
                                Color.Blue_30
                            }
                        )
                        .border(1.dp, color = Color.Red_50),
                    contentAlignment = Alignment.Center
                ) {
                    startAndGoalFlag(
                        curIdx = index,
                        startIdx = startIdx,
                        destIdx = destIdx)
                }
            }
        }
    )
}

@Composable
private fun startAndGoalFlag(curIdx: Int, startIdx: Int, destIdx: Int) {
    if (curIdx == startIdx) {
        Text(text = "START",
            color = CustomTheme.colors.textColorPrimary,
            fontWeight = FontWeight.Bold
        )
    } else if (curIdx == destIdx) {
        Text(text = "GOAL",
            color = CustomTheme.colors.textColorPrimary,
            fontWeight = FontWeight.Bold)
    }
}

