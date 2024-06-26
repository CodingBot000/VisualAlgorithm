package com.codingbot.algorithm.core.common

import androidx.navigation.NavType
import androidx.navigation.navArgument

object Const {
    var sortingSpeed = 500f
    var sortingSpeedSliderInit = sortingSpeed / 100
    var globalMultipleInputTime = 0L

    const val GRAPH_HEIGHT_FROM = 10
    const val GRAPH_HEIGHT_TO = 150
    const val ARRAYS_SIZE = 13

    const val GRAPH_ARRAY_SIZE = 5
}

object InitValue {
    val MAZE_INIT = arrayOf(
        intArrayOf(0, 0, 1, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(0, 0, 0, 0, 0, 1, 1, 0, 1, 0),
        intArrayOf(0, 0, 0, 1, 0, 0, 0, 0, 0, 0),
        intArrayOf(1, 1, 0, 1, 1, 1, 1, 0, 1, 1),
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(0, 1, 1, 1, 1, 1, 0, 1, 1, 1),
        intArrayOf(0, 1, 0, 0, 0, 0, 1, 0, 0, 0),
        intArrayOf(0, 1, 0, 1, 0, 1, 0, 0, 1, 0),
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 1, 0),
    )
    val MAZE_START = intArrayOf(0, MAZE_INIT[0].size -1)
    val MAZE_DEST = intArrayOf(MAZE_INIT[0].size -1, MAZE_INIT.size -1)
    val MAZE_COLUMS = MAZE_INIT[0].size
}
enum class SortingList {
    BUBBLE_SORT, SELECTION_SORT, INSERTION_SORT, QUICK_SORT, HEAP_SORT
}

enum class GraphList {
    DFS, BFS
}

sealed interface Screen {
    val route: String

    object SelectListScreen : Screen {
        override val route: String = ScreenRoutes.SelectListScreen
    }

    object SortingScreen : Screen {
        override val route: String = ScreenRoutes.SortingScreen

        const val type = "sortingType"

        val routeWithArgs = "$route/{$type}"

        val arguments = listOf(
            navArgument(type) { type = NavType.StringType },
        )

        fun route(
            sortingType: String = ""
        ): String {
            return "$route/$sortingType"
        }
    }

    object SortingHeapSortingScreen : Screen {
        override val route: String = ScreenRoutes.SortingHeapSortingScreen

        const val type = "sortingType"

        val routeWithArgs = "$route/{$type}"

        val arguments = listOf(
            navArgument(type) { type = NavType.StringType },
        )

        fun route(
            trackingType: String = ""
        ): String {
            return "$route/$trackingType"
        }
    }

    object GraphScreen : Screen {
        override val route: String = ScreenRoutes.GraphScreen

        const val type = "trackingType"

        val routeWithArgs = "$route/{$type}"

        val arguments = listOf(
            navArgument(type) { type = NavType.StringType },
        )

        fun route(
            trackingType: String = ""
        ): String {
            return "$route/$trackingType"
        }
    }

}



private object ScreenRoutes {
    const val SelectListScreen =  "SelectListScreen"
    const val SortingScreen =  "SortingScreen"
    const val SortingHeapSortingScreen =  "SortingHeapSortingScreen"
    const val GraphScreen =  "GraphScreen"
}

