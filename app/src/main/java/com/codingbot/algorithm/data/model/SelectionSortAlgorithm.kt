package com.codingbot.algorithm.data.model

import com.codingbot.algorithm.core.common.Const
import com.codingbot.algorithm.core.common.Logger
import com.codingbot.algorithm.data.SortingData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Collections.swap

class SelectionSortAlgorithm(): ISortingAlgorithm
{
    val logger = Logger("SelectionSortAlgorithm")

    private lateinit var viewModelScope: CoroutineScope
    private lateinit var arr: MutableList<SortingData>
    private lateinit var iDisplaySortingUpdateEvent: IDisplaySortingUpdateEvent

    private var sortingSpeed: Float = Const.sortingSpeed
    private var backupArr = emptyList<SortingData>()
    private var arrSize = 0

    override fun initValue(
        viewModelScope: CoroutineScope,
        arr: MutableList<SortingData>,
        iDisplaySortingUpdateEvent: IDisplaySortingUpdateEvent
    ) {
        this.viewModelScope = viewModelScope
        this.arr = arr
        this.iDisplaySortingUpdateEvent = iDisplaySortingUpdateEvent

        backupArr = arr.toMutableList()
    }
    override fun setSpeed(speed: Float) {
        this.sortingSpeed = speed
    }

    override suspend fun start() {
        arrSize = arr.size
        viewModelScope.launch {
            sort()
        }
    }

    override suspend fun restart() {
        arr = backupArr.toMutableList()
        start()
    }

    private suspend fun sort() {
        for (i in 0 until arrSize - 1) {
            var minIdx = i
            for (j in i + 1 until arrSize) {
                if (arr[j].element < arr[minIdx].element) {
                    minIdx = j
                }
            }
            iDisplaySortingUpdateEvent.elementList(
                list = arr,
                swapTargetIdx1 = i,
                swapTargetIdx2 = minIdx
            )
            delay(sortingSpeed.toLong())
            swap(arr, i, minIdx)
            iDisplaySortingUpdateEvent.elementList(
                list = arr,
                swapTargetIdx1 = i,
                swapTargetIdx2 = minIdx
            )
            delay(sortingSpeed.toLong())
        }
        iDisplaySortingUpdateEvent.finish()
    }
}