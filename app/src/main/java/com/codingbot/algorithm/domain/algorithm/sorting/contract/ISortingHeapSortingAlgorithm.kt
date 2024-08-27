package com.codingbot.algorithm.domain.algorithm.sorting.contract

import com.codingbot.algorithm.domain.model.SortingData
import kotlinx.coroutines.CoroutineScope

interface ISortingHeapSortingAlgorithm
{
    fun initValue(
        viewModelScope: CoroutineScope,
        sortingListInit: MutableList<SortingData>,
        iDisplayHeapSortingUpdateEvent: IDisplayHeapSortingUpdateEvent
    )
    suspend fun start()
    suspend fun restart()
}