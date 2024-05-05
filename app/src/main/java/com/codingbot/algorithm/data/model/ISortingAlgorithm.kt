package com.codingbot.algorithm.data.model

import com.codingbot.algorithm.data.SortingData
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