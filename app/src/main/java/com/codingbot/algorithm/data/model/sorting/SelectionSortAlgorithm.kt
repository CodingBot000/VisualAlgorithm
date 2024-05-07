package com.codingbot.algorithm.data.model.sorting

import com.codingbot.algorithm.core.common.Logger
import com.codingbot.algorithm.data.SortingData
import com.codingbot.algorithm.data.SortingDataResult
import com.codingbot.algorithm.data.model.sorting.contract.IDisplaySortingUpdateEvent
import com.codingbot.algorithm.data.model.sorting.contract.ISortingAlgorithm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Collections.swap

class SelectionSortAlgorithm(): ISortingAlgorithm
{
    val logger = Logger("SelectionSortAlgorithm")

    private lateinit var viewModelScope: CoroutineScope
    private lateinit var arrOrigin: MutableList<SortingData>
    private var resultArr: MutableList<SortingDataResult> = mutableListOf<SortingDataResult>()
    private lateinit var iDisplaySortingUpdateEvent: IDisplaySortingUpdateEvent
    private var backupArr = emptyList<SortingData>()
    private var arrSize = 0

    override fun initValue(
        viewModelScope: CoroutineScope,
        sortingListInit: MutableList<SortingData>,
        iDisplaySortingUpdateEvent: IDisplaySortingUpdateEvent
    ) {
        this.viewModelScope = viewModelScope
        this.arrOrigin = sortingListInit
        this.iDisplaySortingUpdateEvent = iDisplaySortingUpdateEvent
        arrSize = arrOrigin.size
        backupArr = sortingListInit.toMutableList()
    }

    override suspend fun start() {
        viewModelScope.launch {
            sort()
        }
    }

    override suspend fun restart() {
        arrOrigin = backupArr.toMutableList()
        start()
    }

    private suspend fun sort() {
        for (i in 0 until arrSize - 1) {
            var minIdx = i
            for (j in i + 1 until arrSize) {
                if (arrOrigin[j].element < arrOrigin[minIdx].element) {
                    minIdx = j
                }
            }
//            iDisplaySortingUpdateEvent.elementList(
//                list = arr,
//                swapTargetIdx1 = i,
//                swapTargetIdx2 = minIdx
//            )
//            delay(sortingSpeed.toLong())
            resultArr.add(
                SortingDataResult(
                    sortingDataList = arrOrigin.toMutableList(),
                    swapTargetIdx1 = i,
                    swapTargetIdx2 = minIdx
                )
            )
            swap(arrOrigin, i, minIdx)
//            iDisplaySortingUpdateEvent.elementList(
//                list = arr,
//                swapTargetIdx1 = i,
//                swapTargetIdx2 = minIdx
//            )
//            delay(sortingSpeed.toLong())
            resultArr.add(
                SortingDataResult(
                    sortingDataList = arrOrigin.toMutableList(),
                    swapTargetIdx1 = i,
                    swapTargetIdx2 = minIdx
                )
            )
        }
        iDisplaySortingUpdateEvent.finish(resultArr)
    }
}