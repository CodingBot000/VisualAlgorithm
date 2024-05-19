package com.codingbot.algorithm.domain.algorithm.sorting.contract

import com.codingbot.algorithm.domain.model.SortingData
import kotlinx.coroutines.CoroutineScope

interface ISortingAlgorithm
{
    fun initValue(
        viewModelScope: CoroutineScope,
        sortingListInit: MutableList<SortingData>,
        iDisplaySortingUpdateEvent: IDisplaySortingUpdateEvent
    )
    suspend fun start()
    suspend fun restart()
}