package com.codingbot.algorithm.data.model.sorting.contract

import com.codingbot.algorithm.data.SortingData


interface IDisplayHeapSortingUpdateEvent {
    fun elementList(list: MutableList<SortingData>,
                    resultList: MutableList<SortingData>? = null,
                    swapTargetIdx1: Int,
                    swapTargetIdx2: Int)
    fun finish()
}