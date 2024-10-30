package com.algorithm.domain.sorting

import com.algorithm.model.SortingHeapDataResult


interface IDisplayHeapSortingUpdateEvent {
    fun finish(resultList: MutableList<SortingHeapDataResult>)
}