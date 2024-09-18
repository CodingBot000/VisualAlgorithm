package com.algorithm.domain.sorting

import com.algorithm.model.SortingData

interface ISortingAlgorithm
{
    fun initValue(
        sortingListInit: MutableList<com.algorithm.model.SortingData>,
        iDisplaySortingUpdateEvent: IDisplaySortingUpdateEvent
    )
    suspend fun start()
    suspend fun restart()
}