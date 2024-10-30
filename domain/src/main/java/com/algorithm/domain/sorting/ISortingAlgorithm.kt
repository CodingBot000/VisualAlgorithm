package com.algorithm.domain.sorting

import com.algorithm.model.SortingData

interface ISortingAlgorithm
{
    fun initValue(
        sortingListInit: MutableList<SortingData>,
        iDisplaySortingUpdateEvent: IDisplaySortingUpdateEvent
    )
    suspend fun start()
    suspend fun restart()
}