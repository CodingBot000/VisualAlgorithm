package com.codingbot.algorithm.data.model.sorting.contract

import com.codingbot.algorithm.data.SortingData
import com.codingbot.algorithm.data.model.sorting.contract.IDisplaySortingUpdateEvent
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