package com.codingbot.algorithm.domain.algorithm.sorting

import com.codingbot.algorithm.core.common.Logger
import com.codingbot.algorithm.domain.model.SortingData
import com.codingbot.algorithm.domain.model.SortingDataResult
import com.codingbot.algorithm.domain.algorithm.sorting.contract.IDisplaySortingUpdateEvent
import com.codingbot.algorithm.domain.algorithm.sorting.contract.ISortingAlgorithm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Collections.swap

class BubbleSortAlgorithm(): ISortingAlgorithm
{
    val logger = Logger("BubbleSortAlgorithm")

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
            for (j in 0 until arrSize - i - 1) {
                if (arrOrigin[j].element > arrOrigin[j + 1].element) {
                    resultArr.add(
                        SortingDataResult(
                            sortingDataList = arrOrigin.toMutableList(),
                            swapTargetIdx1 = j,
                            swapTargetIdx2 = j + 1
                        )
                    )
                    swap(arrOrigin, j, j + 1)
                    resultArr.add(
                        SortingDataResult(
                            sortingDataList = arrOrigin.toMutableList(),
                            swapTargetIdx1 = j,
                            swapTargetIdx2 = j + 1
                        )
                    )
                }
            }
        }
        iDisplaySortingUpdateEvent.finish(resultArr)
    }
}