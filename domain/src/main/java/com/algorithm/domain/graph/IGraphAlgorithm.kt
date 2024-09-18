package com.algorithm.domain.graph

interface IGraphAlgorithm
{
    fun initValue(
        graphListInit: Array<IntArray>,
        iDisplayGraphUpdateEvent: IDisplayGraphUpdateEvent
    )

    suspend fun start(start: IntArray, dest: IntArray)
    suspend fun restart(start: IntArray, dest: IntArray)
}