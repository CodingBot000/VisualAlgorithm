package com.codingbot.algorithm.data.model.sorting

import com.codingbot.algorithm.core.common.Const
import com.codingbot.algorithm.core.common.Logger
import com.codingbot.algorithm.data.SortingData
import com.codingbot.algorithm.data.SortingDataResult
import com.codingbot.algorithm.data.model.sorting.contract.IDisplaySortingUpdateEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Collections.swap

class BubbleSortAlgorithm(): ISortingAlgorithm
{
    val logger = Logger("BubbleSortAlgorithm")

    private lateinit var viewModelScope: CoroutineScope
    private lateinit var arr: MutableList<SortingData>
    private var resultArr: MutableList<SortingDataResult> = mutableListOf<SortingDataResult>()
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
            for (j in 0 until arrSize - i - 1) {
                if (arr[j].element > arr[j + 1].element) {
                    resultArr.add(SortingDataResult(sortingDataList = arr.toMutableList(), swapTargetIdx1 = j, swapTargetIdx2 = j + 1))
//                    iDisplaySortingUpdateEvent.elementList(
//                        list = arr,
//                        swapTargetIdx1 = j,
//                        swapTargetIdx2 = j + 1
//                    )
//                    delay(sortingSpeed.toLong())
                    swap(arr, j, j + 1)
                    resultArr.add(SortingDataResult(sortingDataList = arr.toMutableList(), swapTargetIdx1 = j, swapTargetIdx2 = j + 1))
//                    iDisplaySortingUpdateEvent.elementList(
//                        list = arr,
//                        swapTargetIdx1 = j,
//                        swapTargetIdx2 = j + 1
//                    )
//                    delay(sortingSpeed.toLong())
                }
            }
        }
        iDisplaySortingUpdateEvent.finish(resultArr)
    }

}