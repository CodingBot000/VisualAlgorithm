package com.codingbot.algorithm.data.model.sorting.contract

import com.codingbot.algorithm.data.SortingData
import com.codingbot.algorithm.data.SortingDataResult
import com.codingbot.algorithm.data.SortingHeapDataResult


interface IDisplayHeapSortingUpdateEvent {
    fun elementList(list: MutableList<SortingData>,
                    resultList: MutableList<SortingData>,
                    swapTargetIdx1: Int,
                    swapTargetIdx2: Int)
    fun finish(resultList: MutableList<SortingHeapDataResult>)
}