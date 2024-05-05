package com.codingbot.algorithm.viewmodel

import com.codingbot.algorithm.core.common.GraphList
import com.codingbot.algorithm.core.common.Logger
import com.codingbot.algorithm.core.common.SortingList
import com.codingbot.algorithm.ui.ChannelUiEvent
import com.codingbot.algorithm.ui.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class MainUiState(
    val buttonEnable: Boolean = false,
    val elementSelected: Int = 0,
    val selectSortList: MutableList<SortingList> = mutableListOf<SortingList>(),
    val selectGraphList: MutableList<GraphList> = mutableListOf<GraphList>(),
    val moveCount: Int = 0
)

sealed interface MainIntent {
    data class SelectSortList(val selectList: MutableList<SortingList>): MainIntent
    data class SelectGraphList(val selectList: MutableList<GraphList>): MainIntent
}

sealed interface MainUiEvent {
    data class SelectSortList(val selectList: MutableList<SortingList>): MainUiEvent
}
@HiltViewModel
class MainViewModel @Inject constructor()
    : MviViewModel<MainUiState, MainIntent>(MainUiState()),
    UiEvent<MainUiEvent> by ChannelUiEvent() {
    val logger = Logger("SortingViewModel")

    init {
        initSelect()
    }

    private fun initSelect() {
        execute(MainIntent.SelectSortList(enumValues<SortingList>().toMutableList()))
        execute(MainIntent.SelectGraphList(enumValues<GraphList>().toMutableList()))
    }

    override suspend fun MainUiState.reduce(intent: MainIntent): MainUiState =
        when (intent) {
            is MainIntent.SelectSortList -> copy(selectSortList = intent.selectList)
            is MainIntent.SelectGraphList -> copy(selectGraphList = intent.selectList)
        }
}