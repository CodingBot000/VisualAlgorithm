package com.codingbot.algorithm.ui.sorting

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.codingbot.algorithm.core.common.Logger
import com.codingbot.algorithm.core.common.Screen
import com.codingbot.algorithm.core.common.SortingList
import com.codingbot.algorithm.ui.theme.CustomTheme
import com.codingbot.algorithm.viewmodel.MainViewModel

@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel  = hiltViewModel()
) {
    val logger = remember { Logger("SortingSelectScreen", true, "[Screen]") }
    logger { "mainViewModel:$mainViewModel" }
    val uiState = mainViewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier
        .background(color = CustomTheme.colors.bg)
        .fillMaxSize()
        .padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        LazyColumn(modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(10.dp)
        )
        {
            item {
                Text(
                    modifier = Modifier
                        .padding(5.dp),
                    text = "Sorting",
                    color = CustomTheme.colors.black,
                    style = CustomTheme.typography.title3Regular
                )
            }

            itemsIndexed(uiState.value.selectSortList,
                key = { index, item -> "$index _$item" })
            { _, item ->
                SelectionCell(
                    itemName = item.name,
                    onClick = { navigateCellName ->
                        if ( navigateCellName == SortingList.HEAP_SORT.name) {
                            navController.navigate(Screen.SortingHeapSortingScreen.route(navigateCellName))
                        } else {
                            navController.navigate(Screen.SortingScreen.route(navigateCellName))
                        }
                    }
                )
            }
            item {
                Text(
                    modifier = Modifier
                        .padding(5.dp),
                    text = "Graph",
                    color = CustomTheme.colors.black,
                    style = CustomTheme.typography.title3Regular
                )
            }

            itemsIndexed(uiState.value.selectGraphList,
                key = { index, item -> "$index _$item" })
            { _, item ->
                SelectionCell(
                    itemName = item.name,
                    onClick = { navigateCellName ->
                        navController.navigate(Screen.GraphScreen.route(navigateCellName))
                    }
                )
            }
        }
    }
}

@Composable
private fun SelectionCell(
    itemName: String,
    onClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .background(color = CustomTheme.colors.bg)
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .clickable {
                onClick(itemName)
            },
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
                text = itemName.replace("_", " "),
                color = CustomTheme.colors.black,
                style = CustomTheme.typography.title3Regular
            )
        }
    }
    Spacer(modifier = Modifier.width(10.dp))
}

