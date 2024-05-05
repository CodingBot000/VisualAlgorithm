package com.codingbot.algorithm.ui.sorting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.codingbot.algorithm.core.common.GraphList
import com.codingbot.algorithm.core.common.Logger
import com.codingbot.algorithm.core.common.Screen
import com.codingbot.algorithm.core.common.SortingList
import com.codingbot.algorithm.viewmodel.MainViewModel

@Composable
fun SortingSelectScreen(
    navController: NavController,
    mainViewModel: MainViewModel  = hiltViewModel()
) {
    val logger = remember { Logger("SortingSelectScreen", true, "[Screen]") }
    logger { "mainViewModel:$mainViewModel" }
    val uiState = mainViewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        LazyColumn(modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(10.dp)
        )
        {
            itemsIndexed(uiState.value.selectSortList,
                key = { index, item -> "$index _$item" })
            { index, item ->
                SelectionCell(
                    item = item,
                    onClick = { navigateCell ->
                        logger { "name : ${navigateCell.name}" }
                        if (navigateCell.name == SortingList.HEAP_SORT.name) {
                            logger { "name : ${navigateCell.name} heapSort: ${SortingList.HEAP_SORT.name}" }
                            navController.navigate(Screen.SortingHeapSortingScreen.route(navigateCell.name))
                        } else {
                            navController.navigate(Screen.SortingScreen.route(navigateCell.name))
                        }
                    }
                )
            }
        }

        LazyColumn(modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(10.dp)
        )
        {
            itemsIndexed(uiState.value.selectGraphList,
                key = { index, item -> "$index _$item" })
            { index, item ->
                SelectionCell(
                    item = item,
                    onClick = { navigateCell ->
                        navController.navigate(Screen.GraphScreen.route(navigateCell.name))
                    }
                )
            }
        }
    }
}


@Composable
fun SelectionCell(
    item: SortingList,
    onClick: (SortingList) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .clickable {
                onClick(item)
            },
        shape = RoundedCornerShape(16.dp),
        elevation = 5.dp
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            text = item.name.replace("_", " "),
            color = Color.Black,
            fontSize = 20.sp
        )
    }
    Spacer(modifier = Modifier.width(10.dp))
}


@Composable
fun SelectionCell(
    item: GraphList,
    onClick: (GraphList) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .clickable {
                onClick(item)
            },
        shape = RoundedCornerShape(16.dp),
        elevation = 5.dp
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            text = item.name.replace("_", " "),
            color = Color.Black,
            fontSize = 20.sp
        )
    }
    Spacer(modifier = Modifier.width(10.dp))
}

