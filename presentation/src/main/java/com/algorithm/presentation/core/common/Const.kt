package com.algorithm.presentation.core.common

import androidx.navigation.NavType
import androidx.navigation.navArgument

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

    object AsynchronousScreen : Screen {
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
    const val AsynchronousScreen = "AsynchronousScreen"
}

