package com.algorithm.presentation.screens

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.algorithm.common.SortingList
import com.algorithm.presentation.R
import com.algorithm.presentation.core.common.Screen
import com.algorithm.presentation.ui.theme.CustomTheme
import com.algorithm.presentation.viewmodel.MainViewModel

@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel  = hiltViewModel()
) {
    val logger = remember { com.algorithm.utils.Logger("MainScreen", true, "[Screen]") }

    val uiState = mainViewModel.uiState.collectAsStateWithLifecycle()
    val state = rememberLazyListState()
    Column(modifier = Modifier
        .background(color = CustomTheme.colors.bg)
        .fillMaxSize()
        .padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        Text(
            modifier = Modifier
                .padding(5.dp),
            text = stringResource(id = R.string.sorting),
            color = CustomTheme.colors.textColorPrimary,
            style = CustomTheme.typography.title3Regular
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            content = {
                items(uiState.value.selectSortList.count()) { index ->
                    val (delay, easing) = state.calculateDelayAndEasing(index, 2)
                    val animation = tween<Float>(durationMillis = 500, delayMillis = delay, easing = easing)
                    val args = ScaleAndAlphaArgs(fromScale = 2f, toScale = 1f, fromAlpha = 0f, toAlpha = 1f)
                    val (scale, alpha) = scaleAndAlpha(args = args, animation = animation)


                    val item = uiState.value.selectSortList[index]
                    SelectionCell(
                        itemName = item.name,
                        modifier = Modifier.graphicsLayer(alpha = alpha, scaleX = scale, scaleY = scale),
                        onClick = { navigateCellName ->
                            if ( navigateCellName == SortingList.HEAP_SORT.name) {
                                navController.navigate(Screen.SortingHeapSortingScreen.route(navigateCellName))
                            } else {
                                navController.navigate(Screen.SortingScreen.route(navigateCellName))
                            }
                        }
                    )
                }
            }
        )

        Text(
            modifier = Modifier
                .padding(5.dp),
            text = stringResource(id = R.string.graph),
            color = CustomTheme.colors.textColorPrimary,
            style = CustomTheme.typography.title3Regular
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            content = {
                items(uiState.value.selectGraphList.count()) { index ->

                    val (delay, easing) = state.calculateDelayAndEasing(index, 2)
                    val animation = tween<Float>(durationMillis = 500, delayMillis = delay, easing = easing)
                    val args = ScaleAndAlphaArgs(fromScale = 2f, toScale = 1f, fromAlpha = 0f, toAlpha = 1f)
                    val (scale, alpha) = scaleAndAlpha(args = args, animation = animation)


                    val item = uiState.value.selectGraphList[index]
                    SelectionCell(
                        itemName = item.name,
                        modifier = Modifier.graphicsLayer(alpha = alpha, scaleX = scale, scaleY = scale),
                        onClick = { navigateCellName ->
                            navController.navigate(Screen.GraphScreen.route(navigateCellName))
                        }
                    )
                }
            }
        )
    }
}

@Composable
private fun LazyListState.calculateDelayAndEasing(index: Int, columnCount: Int): Pair<Int, Easing> {
    val row = index / columnCount
    val column = index % columnCount
    val firstVisibleRow = firstVisibleItemIndex
    val visibleRows = layoutInfo.visibleItemsInfo.count()
    val scrollingToBottom = firstVisibleRow < row
    val isFirstLoad = visibleRows == 0
    val rowDelay = 200 * when {
        isFirstLoad -> row // initial load
        scrollingToBottom -> visibleRows + firstVisibleRow - row // scrolling to bottom
        else -> 1 // scrolling to top
    }
    val scrollDirectionMultiplier = if (scrollingToBottom || isFirstLoad) 1 else -1
    val columnDelay = column * 150 * scrollDirectionMultiplier
    val easing = if (scrollingToBottom || isFirstLoad) LinearOutSlowInEasing else FastOutSlowInEasing
    return rowDelay + columnDelay to easing
}

private enum class State { PLACING, PLACED }

data class ScaleAndAlphaArgs(
    val fromScale: Float,
    val toScale: Float,
    val fromAlpha: Float,
    val toAlpha: Float
)

@Composable
fun scaleAndAlpha(
    args: ScaleAndAlphaArgs,
    animation: FiniteAnimationSpec<Float>
): Pair<Float, Float> {
    val transitionState = remember { MutableTransitionState(State.PLACING).apply { targetState = State.PLACED } }
    val transition = updateTransition(transitionState)
    val alpha by transition.animateFloat(transitionSpec = { animation }) { state ->
        when (state) {
            State.PLACING -> args.fromAlpha
            State.PLACED -> args.toAlpha
        }
    }
    val scale by transition.animateFloat(transitionSpec = { animation }) { state ->
        when (state) {
            State.PLACING -> args.fromScale
            State.PLACED -> args.toScale
        }
    }
    return alpha to scale
}
@Composable
private fun SelectionCell(
    itemName: String,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .background(color = CustomTheme.colors.bg)
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
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier
                    .padding(5.dp),
                text = itemName.replace("_", "\n"),
                color = CustomTheme.colors.black,
                style = CustomTheme.typography.title3Regular
            )
        }
    }
    Spacer(modifier = Modifier.width(10.dp))
}

