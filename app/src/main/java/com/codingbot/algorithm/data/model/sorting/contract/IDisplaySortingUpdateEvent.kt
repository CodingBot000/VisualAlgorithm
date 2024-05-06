package com.codingbot.algorithm.data.model.sorting.contract

import com.codingbot.algorithm.data.SortingData
import com.codingbot.algorithm.data.SortingDataResult


interface IDisplaySortingUpdateEvent {
    fun elementList(list: MutableList<SortingData>,
                    swapTargetIdx1: Int,
                    swapTargetIdx2: Int)
    fun finish(resultList: MutableList<SortingDataResult>)
}