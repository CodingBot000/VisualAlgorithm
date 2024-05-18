package com.codingbot.algorithm.domain.algorithm.sorting.contract

import com.codingbot.algorithm.domain.model.SortingHeapDataResult


interface IDisplayHeapSortingUpdateEvent {
    fun finish(resultList: MutableList<SortingHeapDataResult>)
}