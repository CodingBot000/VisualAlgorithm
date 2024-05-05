package com.codingbot.algorithm.data.model

import com.codingbot.algorithm.core.common.Const
import com.codingbot.algorithm.core.common.Logger
import com.codingbot.algorithm.data.SortingData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InsertionSortAlgorithm(): ISortingAlgorithm
{
    val logger = Logger("InsertionSortAlgorithm")

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
        for (i in 1 until arrSize) {
            val tmp = arr[i]
            var j = i - 1
            while (j >= 0 && tmp.element < arr[j].element) {
                iDisplaySortingUpdateEvent.elementList(
                    list = arr,
                    swapTargetIdx1 = j,
                    swapTargetIdx2 = j + 1
                )
                delay(sortingSpeed.toLong())
                arr[j + 1] = arr[j]
                j--

            }
            arr[j + 1] = tmp
            iDisplaySortingUpdateEvent.elementList(
                list = arr,
                swapTargetIdx1 = j,
                swapTargetIdx2 = j + 1
            )
            delay(sortingSpeed.toLong())
        }
        iDisplaySortingUpdateEvent.finish()
    }

}