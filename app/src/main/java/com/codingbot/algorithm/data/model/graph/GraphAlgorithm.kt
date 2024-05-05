package com.codingbot.algorithm.data.model.graph

import com.codingbot.algorithm.core.common.Const
import kotlinx.coroutines.CoroutineScope

abstract class GraphAlgorithm
{
    lateinit var viewModelScope: CoroutineScope
    lateinit var arr: Array<IntArray>
    lateinit var iDisplayGraphUpdateEvent: IDisplayGraphUpdateEvent
    lateinit var mazeArray: Array<IntArray>

    var speedValue: Float = Const.sortingSpeed
    var backupArr = emptyArray<IntArray>()
    var arrSize = 0
    abstract fun setSpeed(speed: Float)
    abstract fun initValue(
        viewModelScope: CoroutineScope,
        arr: Array<IntArray>,
        iDisplaySortingUpdateEvent: IDisplayGraphUpdateEvent
    )

    abstract suspend fun start(start: IntArray, dest: IntArray)
    abstract suspend fun restart(start: IntArray, dest: IntArray)
}