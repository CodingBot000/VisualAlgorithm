package com.algorithm.presentation.ui.screens

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.algorithm.common.SortingList
import com.algorithm.presentation.R
import com.algorithm.presentation.core.common.Screen
import com.algorithm.presentation.ui.component.LottieLoader
import com.algorithm.presentation.ui.component.RainBowTextColorAnimation
import com.algorithm.presentation.ui.component.ScaleAndAlphaArgs
import com.algorithm.presentation.ui.component.calculateDelayAndEasing
import com.algorithm.presentation.ui.component.scaleAndAlpha
import com.algorithm.presentation.ui.theme.CustomTheme
import com.algorithm.presentation.ui.theme.rainbowColors
import com.algorithm.presentation.viewmodel.MainViewModel

@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel  = hiltViewModel()
) {
    val logger = remember { com.algorithm.utils.Logger("MainScreen", true, "[Screen]") }

    val uiState = mainViewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier
        .background(color = CustomTheme.colors.black)
        .fillMaxSize()
        .padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        SelectMenuGridList(
            subTitle = stringResource(id = R.string.sorting),
            lottieRes = R.raw.tower_hanging,
            itemsList = uiState.value.selectSortList,
            twinkleColorList = uiState.value.rainbowColors,
            onClick = { navigateCellName ->
                if ( navigateCellName == SortingList.HEAP_SORT.name) {
                    navController.navigate(Screen.SortingHeapSortingScreen.route(navigateCellName))
                } else {
                    navController.navigate(Screen.SortingScreen.route(navigateCellName))
                }
            }
        )

        SelectMenuGridList(
            subTitle = stringResource(id = R.string.graph),
            lottieRes = R.raw.lottie_dice,
            itemsList = uiState.value.selectGraphList,
            twinkleColorList = uiState.value.rainbowColors,
            onClick = { navigateCellName ->
                navController.navigate(Screen.GraphScreen.route(navigateCellName))
            }
        )

        SelectMenuGridList(
            subTitle = stringResource(id = R.string.raceCondition),
            lottieRes = R.raw.lottie_dice,
            itemsList = uiState.value.selectRaceConditionList,
            twinkleColorList = uiState.value.rainbowColors,
            onClick = { navigateCellName ->
                navController.navigate(Screen.AsynchronousScreen.route)
            }
        )
    }
}

@Composable
private fun SubTitle(
    subTitle: String
) {

    Text(
        modifier = Modifier
            .padding(5.dp),
        text = subTitle,
        color = CustomTheme.colors.textColorPrimary,
        style = CustomTheme.typography.title1Bold
    )
}

@Composable
private fun <T : Enum<T>> SelectMenuGridList(
    subTitle: String,
    lottieRes: Int = 0,
    itemsList: List<T>,
    twinkleColorList: MutableList<Color>,
    onClick: (String) -> Unit
) {
    val state = rememberLazyListState()

    Row(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
    ) {
        RainBowTextColorAnimation(
            text = subTitle,
            fontSize = 32.sp,
            rainbowColors = rainbowColors
        )

        LottieLoader(
            lottieRes = lottieRes,
            modifier = Modifier.size(100.dp))

    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        content = {
            items(itemsList.size) { index ->
                val (delay, easing) = state.calculateDelayAndEasing(index, 2)
                val animation = tween<Float>(durationMillis = 500, delayMillis = delay, easing = easing)
                val args = ScaleAndAlphaArgs(fromScale = 2f, toScale = 1f, fromAlpha = 0f, toAlpha = 1f)
                val (scale, alpha) = scaleAndAlpha(args = args, animation = animation)

                val item = itemsList[index]
                val backgroundColor
                    = if (twinkleColorList.isNotEmpty()) {
                        val colorIndx = if (index >0 && index >= twinkleColorList.size) {
                            index % twinkleColorList.size
                        } else {
                            index
                        }
                        twinkleColorList[colorIndx]
                    } else {
                        CustomTheme.colors.bg
                    }

                SelectionCell(
                    itemName = item.name, // Enum의 name 프로퍼티 사용
                    backgroundColor = backgroundColor,
                    modifier = Modifier.graphicsLayer(alpha = alpha, scaleX = scale, scaleY = scale),
                    onClick = { navigateCellName ->
                        onClick(navigateCellName)
                    }
                )
            }
        }
    )
}

@Composable
private fun SelectionCell(
    itemName: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .clickable {
                onClick(itemName)
            }
            .then(modifier),
        shape = RoundedCornerShape(16.dp),
        elevation = 5.dp,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            RainBowTextColorAnimation(
                text = itemName.replace("_", "\n"),
                fontSize = 24.sp,
                rainbowColors = rainbowColors
            )
        }
    }
    Spacer(modifier = Modifier.width(10.dp))
}

