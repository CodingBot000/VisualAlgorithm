package com.algorithm.domain.sorting

import com.algorithm.model.SortingData

interface ISortingHeapSortingAlgorithm
{
    fun initValue(
        sortingListInit: MutableList<SortingData>,
        iDisplayHeapSortingUpdateEvent: IDisplayHeapSortingUpdateEvent
    )
    suspend fun start()
    suspend fun restart()
}