package com.codingbot.algorithm.domain.algorithm.graph.contract

import kotlinx.coroutines.CoroutineScope

interface IGraphAlgorithm
{
    fun initValue(
        viewModelScope: CoroutineScope,
        graphListInit: Array<IntArray>,
        iDisplayGraphUpdateEvent: IDisplayGraphUpdateEvent
    )

    suspend fun start(start: IntArray, dest: IntArray)
    suspend fun restart(start: IntArray, dest: IntArray)
}