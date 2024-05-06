package com.codingbot.algorithm.data.model.sorting

import com.codingbot.algorithm.data.SortingData
import com.codingbot.algorithm.data.model.sorting.contract.IDisplaySortingUpdateEvent
import kotlinx.coroutines.CoroutineScope

interface ISortingAlgorithm
{
    fun setSpeed(speed: Float)
    fun initValue(
        viewModelScope: CoroutineScope,
        arr: MutableList<SortingData>,
        iDisplaySortingUpdateEvent: IDisplaySortingUpdateEvent
    )

    suspend fun start()
    suspend fun restart()
}