package com.algorithm.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.algorithm.presentation.ui.theme.Color
import com.algorithm.presentation.ui.theme.rainbowColors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.function.Supplier
import javax.inject.Inject

data class MainUiState(
    val selectSortList: MutableList<com.algorithm.common.SortingList> = mutableListOf<com.algorithm.common.SortingList>(),
    val selectGraphList: MutableList<com.algorithm.common.GraphList> = mutableListOf<com.algorithm.common.GraphList>(),
    val rainbowColors: MutableList<androidx.compose.ui.graphics.Color> = mutableListOf<androidx.compose.ui.graphics.Color>()
)

sealed interface MainIntent {
    data class SelectSortList(val selectList: MutableList<com.algorithm.common.SortingList>): MainIntent
    data class SelectGraphList(val selectList: MutableList<com.algorithm.common.GraphList>): MainIntent
    data class RainBowColorsList(val rainbowColorList: MutableList<androidx.compose.ui.graphics.Color>): MainIntent
}

@HiltViewModel
class MainViewModel @Inject constructor()
    : BaseViewModel<MainUiState, MainIntent>(MainUiState())
{
    val logger = com.algorithm.utils.Logger("SortingViewModel")

//    var curColorIndex = 0

    init {
        initSelect()
    }

    private fun initSelect() {
        execute(MainIntent.SelectSortList(enumValues<com.algorithm.common.SortingList>().toMutableList()))
        execute(MainIntent.SelectGraphList(enumValues<com.algorithm.common.GraphList>().toMutableList()))
        viewModelScope.launch {
            while (true) {
                delay(500L) // 0.5초 딜레이
                rainbowColors.shuffle()
                logger.log(msg = Supplier { "qq qq $rainbowColors" })
//                val rainbowColorList: MutableList<androidx.compose.ui.graphics.Color> = rainbowColors
                execute(MainIntent.RainBowColorsList(rainbowColors))
            }
        }
    }

    override suspend fun MainUiState.reduce(intent: MainIntent): MainUiState =
        when (intent) {
            is MainIntent.SelectSortList -> copy(selectSortList = intent.selectList)
            is MainIntent.SelectGraphList -> copy(selectGraphList = intent.selectList)
            is MainIntent.RainBowColorsList -> copy(rainbowColors = intent.rainbowColorList.toMutableList())
        }

}