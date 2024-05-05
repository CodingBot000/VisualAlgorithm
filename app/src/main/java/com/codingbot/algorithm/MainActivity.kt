package com.codingbot.algorithm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.codingbot.algorithm.core.common.GraphList
import com.codingbot.algorithm.core.common.Screen
import com.codingbot.algorithm.core.common.SortingList
import com.codingbot.algorithm.ui.graph.GraphScreen
import com.codingbot.algorithm.ui.sorting.SortingHeapSortingScreen
import com.codingbot.algorithm.ui.sorting.SortingScreen
import com.codingbot.algorithm.ui.sorting.SortingSelectScreen
import com.codingbot.algorithm.ui.theme.VisualAlgorithmTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VisualAlgorithmTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.SelectListScreen.route
                    ) {
                        composable(
                            route = Screen.SelectListScreen.route)
                        {
                            SortingSelectScreen(navController = navController)
                        }
                        composable(
                            route = Screen.SortingScreen.routeWithArgs,
                            arguments = Screen.SortingScreen.arguments
                            ) { entry ->
                                val type = entry.arguments?.getString(Screen.SortingScreen.type)  ?: SortingList.BUBBLE_SORT.name
                                SortingScreen(navController = navController,
                                    sortingType = type)
                            }
                        composable(
                            route = Screen.GraphScreen.routeWithArgs,
                            arguments = Screen.GraphScreen.arguments
                        ) { entry ->
                            val type = entry.arguments?.getString(Screen.GraphScreen.type)  ?: GraphList.BFS.name
                            GraphScreen(navController = navController,
                                graphType = type)
                        }

                        composable(
                            route = Screen.SortingHeapSortingScreen.routeWithArgs,
                            arguments = Screen.SortingHeapSortingScreen.arguments
                        ) { entry ->
                            val type = entry.arguments?.getString(Screen.SortingHeapSortingScreen.type)  ?: SortingList.HEAP_SORT.name
                            SortingHeapSortingScreen(navController = navController,
                                sortingType = type)
                        }
                    }
                }
            }
        }
    }
}
