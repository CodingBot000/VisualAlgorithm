package com.algorithm.presentation.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class MainUiState(
    val selectSortList: MutableList<com.algorithm.common.SortingList> = mutableListOf<com.algorithm.common.SortingList>(),
    val selectGraphList: MutableList<com.algorithm.common.GraphList> = mutableListOf<com.algorithm.common.GraphList>(),
)

sealed interface MainIntent {
    data class SelectSortList(val selectList: MutableList<com.algorithm.common.SortingList>): MainIntent
    data class SelectGraphList(val selectList: MutableList<com.algorithm.common.GraphList>): MainIntent
}

@HiltViewModel
class MainViewModel @Inject constructor()
    : BaseViewModel<MainUiState, MainIntent>(MainUiState())
{
    val logger = com.algorithm.utils.Logger("SortingViewModel")

    init {
        initSelect()
    }

    private fun initSelect() {
        execute(MainIntent.SelectSortList(enumValues<com.algorithm.common.SortingList>().toMutableList()))
        execute(MainIntent.SelectGraphList(enumValues<com.algorithm.common.GraphList>().toMutableList()))
    }

    override suspend fun MainUiState.reduce(intent: MainIntent): MainUiState =
        when (intent) {
            is MainIntent.SelectSortList -> copy(selectSortList = intent.selectList)
            is MainIntent.SelectGraphList -> copy(selectGraphList = intent.selectList)
        }
}