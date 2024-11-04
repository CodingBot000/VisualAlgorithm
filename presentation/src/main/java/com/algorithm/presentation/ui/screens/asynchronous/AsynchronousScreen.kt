package com.algorithm.presentation.ui.screens.asynchronous

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.algorithm.model.AsynchronousData
import com.algorithm.presentation.ui.component.ScreenTitle
import com.algorithm.presentation.ui.theme.Color
import com.algorithm.presentation.ui.theme.CustomTheme
import com.algorithm.presentation.ui.theme.Dimens
import com.algorithm.presentation.viewmodel.AsynchronousViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AsynchronousScreen(
    navController: NavController,
    asynchronousViewModel: AsynchronousViewModel = hiltViewModel()
) {
    val state = asynchronousViewModel.uiState.collectAsState()

    Column(modifier = Modifier
        .background(color = CustomTheme.colors.bg)
        .padding(bottom = Dimens.BottomPadding)
        .fillMaxSize(),
        verticalArrangement = Arrangement.Center)
    {
        ScreenTitle(
            title = "Race Condition",
            onClickBack = {
                navController.popBackStack()
            },
            trailingIcon = {

            }
        )

        if (state.value.dataResult == null) {
            TryLoadingScreen(state.value.reTryCnt)
            return
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            stickyHeader {
                ListTitleSection()
            }
            state.value.dataResult?.let { it ->
                items(it.increment.size) { index ->
                    val data = it.increment[index]
                    val sharedCounter = it.sharedCountList[index]
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)

                        .background(
                            color = if (data.criticalSection) {
                                Color.Red_20
                            } else {
                                Color.Blue_10
                            }
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.width(Dimens.RaceCondition.W_Index),
                            text = "[$index] ",
                            color = CustomTheme.colors.black,
                            style = CustomTheme.typography.caption2Bold,
                            textAlign = TextAlign.Center
                        )

                        if (data.coroutineNum == 1) {
                            MemoryCopyCacheToShared(data.valueA, data.valueB)
                        } else {
                            EmptySpace()
                        }

                        Spacer(modifier = Modifier.padding(Dimens.RaceCondition.WbetweenSpace))
                        SharedCounterView(sharedCounter)
                        Spacer(modifier = Modifier.padding(Dimens.RaceCondition.WbetweenSpace))

                        if (data.coroutineNum == 2) {
                            MemoryCopyCacheToShared(data.valueA, data.valueB)
                        } else {
                            EmptySpace()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TryLoadingScreen(
    reTryCnt: Int
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(220.dp)
                .background(color = Color.Gray_5, shape = CircleShape)
        )

        CircularProgressIndicator(
            modifier = Modifier.size(200.dp),
            strokeWidth = 17.dp
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Try Count",
                color = CustomTheme.colors.black,
                style = CustomTheme.typography.title3Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = reTryCnt.toString(),
                color = CustomTheme.colors.black,
                style = CustomTheme.typography.largeTitleBold,
                textAlign = TextAlign.Center
            )
        }

    }

}
@Composable
private fun ListTitleSection() {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(40.dp)

        .background(color = Color.Gray_5),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.width(Dimens.RaceCondition.W_Index),
            text = "Routine\nIndex",
            color = CustomTheme.colors.black,
            style = CustomTheme.typography.captionBold,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier
                .width(Dimens.RaceCondition.W_RaceConditionCell),
            text = "Coroutine\nNo 1",
            color = CustomTheme.colors.black,
            style = CustomTheme.typography.captionBold,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier.width(Dimens.RaceCondition.W_SharedCounter),
            text = "Shared\nCounter",
            color = CustomTheme.colors.black,
            style = CustomTheme.typography.captionBold,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.width(Dimens.RaceCondition.W_RaceConditionCell),
            text = "Coroutine\nNo 2",
            color = CustomTheme.colors.black,
            style = CustomTheme.typography.captionBold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SharedCounterView(sharedCounter: Int) {
    Text(
        modifier = Modifier
            .width(Dimens.RaceCondition.W_SharedCounter)
            .background(color = Color.Yellow_10),
        text = sharedCounter.toString(),
        color = CustomTheme.colors.elementNormal,
        style = CustomTheme.typography.captionBold,
        textAlign = TextAlign.Center
    )

}

@Composable
private fun EmptySpace() {
    Spacer(modifier = Modifier.width(Dimens.RaceCondition.W_RaceConditionCell))
}

@Composable
private fun MemoryCopyCacheToShared(valueA: Int, valueB: Int) {
    Row(modifier = Modifier
        .width(Dimens.RaceCondition.W_RaceConditionCell)
        .height(40.dp)
        .padding(horizontal = 10.dp)
        .background(color = Color.Blue_Gray_10),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .width(Dimens.RaceCondition.W_RaceCondition),
            text = if (valueA == -1) {
                ""
            } else {
                valueA.toString()
            },
            color = CustomTheme.colors.black,
            style = CustomTheme.typography.captionBold,
            textAlign = TextAlign.Center
        )

        Icon(
            modifier = Modifier
                .size(32.dp),

            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
            contentDescription = null
        )

        Text(
            modifier = Modifier
                .width(Dimens.RaceCondition.W_RaceCondition),
            text = if (valueB == -1) {
                ""
            } else {
                valueB.toString()
            },
            color = CustomTheme.colors.black,
            style = CustomTheme.typography.captionBold,
            textAlign = TextAlign.Center
        )
    }

}