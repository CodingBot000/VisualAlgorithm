package com.codingbot.algorithm.data.model.sorting.contract

import com.codingbot.algorithm.data.SortingData
import com.codingbot.algorithm.data.SortingDataResult
import com.codingbot.algorithm.data.SortingHeapDataResult


interface IDisplayHeapSortingUpdateEvent {
    fun finish(resultList: MutableList<SortingHeapDataResult>)
}