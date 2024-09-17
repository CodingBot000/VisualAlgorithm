package com.algorithm.domain.corelogic.sorting

import com.algorithm.domain.sorting.IDisplaySortingUpdateEvent
import com.algorithm.domain.sorting.ISortingAlgorithm
import com.algorithm.model.SortingData
import com.algorithm.model.SortingDataResult
import java.util.Collections.swap

internal class BubbleSortAlgorithm(): ISortingAlgorithm
{

    private lateinit var arrOrigin: MutableList<SortingData>
    private var resultArr: MutableList<SortingDataResult> = mutableListOf<SortingDataResult>()
    private lateinit var iDisplaySortingUpdateEvent: IDisplaySortingUpdateEvent
    private var backupArr = emptyList<SortingData>()
    private var arrSize = 0

    override fun initValue(
        sortingListInit: MutableList<SortingData>,
        iDisplaySortingUpdateEvent: IDisplaySortingUpdateEvent
    ) {
        this.arrOrigin = sortingListInit
        this.iDisplaySortingUpdateEvent = iDisplaySortingUpdateEvent
        arrSize = arrOrigin.size
        backupArr = sortingListInit.toMutableList()
    }

    override suspend fun start() {
        sort()
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