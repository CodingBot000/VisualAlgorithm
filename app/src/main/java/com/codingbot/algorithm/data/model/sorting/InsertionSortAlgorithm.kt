package com.codingbot.algorithm.data.model.sorting

import com.codingbot.algorithm.core.common.Logger
import com.codingbot.algorithm.data.SortingData
import com.codingbot.algorithm.data.SortingDataResult
import com.codingbot.algorithm.data.model.sorting.contract.IDisplaySortingUpdateEvent
import com.codingbot.algorithm.data.model.sorting.contract.ISortingAlgorithm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class InsertionSortAlgorithm(): ISortingAlgorithm
{
    val logger = Logger("InsertionSortAlgorithm")

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
        for (i in 1 until arrSize) {
            val tmp = arrOrigin[i]
            var j = i - 1
            while (j >= 0 && tmp.element < arrOrigin[j].element) {
                resultArr.add(
                    SortingDataResult(
                        sortingDataList = arrOrigin.toMutableList(),
                        swapTargetIdx1 = j,
                        swapTargetIdx2 = j + 1
                    )
                )
//                iDisplaySortingUpdateEvent.elementList(
//                    list = arr,
//                    swapTargetIdx1 = j,
//                    swapTargetIdx2 = j + 1
//                )
//                delay(sortingSpeed.toLong())
                arrOrigin[j + 1] = arrOrigin[j]
                j--

            }
            arrOrigin[j + 1] = tmp
//            iDisplaySortingUpdateEvent.elementList(
//                list = arr,
//                swapTargetIdx1 = j,
//                swapTargetIdx2 = j + 1
//            )
//            delay(sortingSpeed.toLong())
            resultArr.add(
                SortingDataResult(
                    sortingDataList = arrOrigin.toMutableList(),
                    swapTargetIdx1 = j,
                    swapTargetIdx2 = j + 1
                )
            )
        }
        iDisplaySortingUpdateEvent.finish(resultArr)
    }

}