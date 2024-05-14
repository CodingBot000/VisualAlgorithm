package com.codingbot.algorithm.ui.graph

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.icons.rounded.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.codingbot.algorithm.core.common.Logger
import com.codingbot.algorithm.ui.component.BottomInfoSection
import com.codingbot.algorithm.ui.component.ScreenTitle
import com.codingbot.algorithm.ui.theme.Color
import com.codingbot.algorithm.ui.theme.CustomTheme
import com.codingbot.algorithm.viewmodel.GraphUiState
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
        graphViewModel.initValue(graphType)
    }

    Column(modifier = Modifier
        .background(color = CustomTheme.colors.bg)
        .fillMaxSize()
        .padding(horizontal = 10.dp))
    {
        ScreenTitle(
            title = graphType,
            onClickBack = {
                navController.popBackStack()
            }
        )
        middleContent(
            uiState = uiState,
            baseGridArray = graphViewModel.originArr,
            startIdx = startIdx,
            destIdx = destIdx
        )
        bottomContent(
            uiState = uiState,
            graphType = graphType,
            onValueChange = { sliderPosition ->
                graphViewModel.setSpeedValue(((10 - sliderPosition.toInt()) * 100).toFloat())
            },
            onClickStart = {
//                    graphViewModel.startButtonEnabled(false)
                graphViewModel.start()
            },
            onClickResume = {
                graphViewModel.resume()
            },
            onClickPause = {
                graphViewModel.pause()
            },
            onClickForward = {
                graphViewModel.forward()
            },
            onClickBackward = {
                graphViewModel.backward()
            },
            onClickReplay = {
                graphViewModel.restart()
            }
        )
    }
}


@Composable
private fun ColumnScope.middleContent(
    uiState: State<GraphUiState>,
    baseGridArray: Array<IntArray>,
    startIdx: Int,
    destIdx: Int
)
{
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        contentAlignment = Alignment.Center
    ) {
        baseGrid(baseGridArray, startIdx, destIdx)
        overlayGrid(visitedArray = uiState.value.visitedList, startIdx, destIdx)
    }
}
@Composable
private fun ColumnScope.bottomContent(
    uiState: State<GraphUiState>,
    graphType: String,
    onValueChange: (Float) -> Unit,
    onClickBackward:() -> Unit = {},
    onClickStart:() -> Unit,
    onClickResume:() -> Unit,
    onClickPause:() -> Unit,
    onClickForward:() -> Unit = {},
    onClickReplay:() -> Unit = {}
) {
    BottomInfoSection(
        algorithmType = graphType,
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

@Composable
private fun overlayGrid(visitedArray: List<Boolean>, startIdx: Int, destIdx: Int) {
    val logger = remember { Logger("GraphScreen", true, "[Screen]") }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val columns = 5 // The number of columns in the grid
    val boxSize = screenWidth / columns
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        content = {
            logger { "visitedList overlayGrid array: $visitedArray" }
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
                                Color.Yellow_40
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
        Icon(imageVector = Icons.Rounded.Home , contentDescription = null)
    } else if (curIdx == destIdx) {
        Icon(imageVector = Icons.Rounded.ExitToApp , contentDescription = null)
    }
}

