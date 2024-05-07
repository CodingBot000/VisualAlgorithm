package com.codingbot.algorithm.viewmodel

import com.codingbot.algorithm.core.common.GraphList
import com.codingbot.algorithm.core.common.Logger
import com.codingbot.algorithm.core.common.SortingList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class MainUiState(
    val selectSortList: MutableList<SortingList> = mutableListOf<SortingList>(),
    val selectGraphList: MutableList<GraphList> = mutableListOf<GraphList>(),
)

sealed interface MainIntent {
    data class SelectSortList(val selectList: MutableList<SortingList>): MainIntent
    data class SelectGraphList(val selectList: MutableList<GraphList>): MainIntent
}

@HiltViewModel
class MainViewModel @Inject constructor()
    : BaseViewModel<MainUiState, MainIntent>(MainUiState())
{
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