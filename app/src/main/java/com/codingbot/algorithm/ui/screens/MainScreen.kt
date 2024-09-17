package com.codingbot.algorithm.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.codingbot.algorithm.R
import com.codingbot.algorithm.core.common.Screen
import com.codingbot.algorithm.ui.theme.CustomTheme
import com.codingbot.algorithm.viewmodel.MainViewModel

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

        Text(
            modifier = Modifier
                .padding(5.dp),
            text = stringResource(id = R.string.sorting),
            color = CustomTheme.colors.white,
            style = CustomTheme.typography.title3Regular
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            content = {
                items(uiState.value.selectSortList.count()) { index ->
                    val item = uiState.value.selectSortList[index]
                    SelectionCell(
                        itemName = item.name,
                        onClick = { navigateCellName ->
                            if ( navigateCellName == com.algorithm.common.SortingList.HEAP_SORT.name) {
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
            color = CustomTheme.colors.white,
            style = CustomTheme.typography.title3Regular
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            content = {
                items(uiState.value.selectGraphList.count()) { index ->
                    val item = uiState.value.selectGraphList[index]
                    SelectionCell(
                        itemName = item.name,
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
                text = itemName.replace("_", "\n"),
                color = CustomTheme.colors.black,
                style = CustomTheme.typography.title3Regular
            )
        }
    }
    Spacer(modifier = Modifier.width(10.dp))
}

