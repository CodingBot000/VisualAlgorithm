package com.codingbot.algorithm.viewmodel

import com.codingbot.algorithm.core.common.Logger
import com.codingbot.algorithm.core.common.SortingList
import com.codingbot.algorithm.ui.ChannelUiEvent
import com.codingbot.algorithm.ui.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class SortingSelectUiState(
    val buttonEnable: Boolean = false,
    val elementSelected: Int = 0,
    val selectList: MutableList<String> = mutableListOf<String>()

)

sealed interface SortingSelectIntent {
    data class SelectList(val selectList: MutableList<String>): SortingSelectIntent
}

sealed interface SortingSelectUiEvent {

}

@HiltViewModel
class SortingSelectViewModel @Inject constructor()
    : MviViewModel<SortingSelectUiState, SortingSelectIntent>(SortingSelectUiState()),
    UiEvent<SortingSelectUiEvent> by ChannelUiEvent()
{
    val logger = Logger("SortingSelectViewModel")
    private val selectList = mutableListOf(SortingList.BUBBLE_SORT, SortingList.SELECTION_SORT)

    override suspend fun SortingSelectUiState.reduce(intent: SortingSelectIntent): SortingSelectUiState =
        when (intent) {
            is SortingSelectIntent.SelectList -> copy(selectList = intent.selectList)
        }
}