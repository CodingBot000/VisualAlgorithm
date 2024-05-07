package com.codingbot.algorithm.data.model.graph.contract

import com.codingbot.algorithm.data.GraphData
import com.codingbot.algorithm.data.SortingData
import com.codingbot.algorithm.data.model.sorting.contract.IDisplaySortingUpdateEvent
import kotlinx.coroutines.CoroutineScope

interface IGraphAlgorithm
{
    fun initValue(
        viewModelScope: CoroutineScope,
        graphListInit: Array<IntArray>,
        iDisplayGraphUpdateEvent: IDisplayGraphUpdateEvent
    )

    fun setSpeed(speed: Float)
    suspend fun start(start: IntArray, dest: IntArray)
    suspend fun restart(start: IntArray, dest: IntArray)
}