package com.algorithm.presentation.screens

import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.algorithm.common.SortingList
import com.algorithm.presentation.R
import com.algorithm.presentation.component.ScaleAndAlphaArgs
import com.algorithm.presentation.component.calculateDelayAndEasing
import com.algorithm.presentation.component.scaleAndAlpha
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

    Column(modifier = Modifier
        .background(color = CustomTheme.colors.bg)
        .fillMaxSize()
        .padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        SelectMenuGridList(
            subTitle = stringResource(id = R.string.sorting),
            itemsList = uiState.value.selectSortList,
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
            itemsList = uiState.value.selectGraphList,
            onClick = { navigateCellName ->
                navController.navigate(Screen.GraphScreen.route(navigateCellName))
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
        style = CustomTheme.typography.title3Regular
    )
}

@Composable
private fun <T : Enum<T>> SelectMenuGridList(
    subTitle: String,
    itemsList: List<T>,
    onClick: (String) -> Unit
) {
    val state = rememberLazyListState()
    SubTitle(subTitle = subTitle)
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
                SelectionCell(
                    itemName = item.name, // Enum의 name 프로퍼티 사용
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

