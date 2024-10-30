package com.algorithm.domain.sorting

import com.algorithm.model.SortingData
import com.algorithm.model.SortingDataResult

interface IDisplaySortingUpdateEvent {
    fun elementList(list: MutableList<SortingData>,
                    swapTargetIdx1: Int,
                    swapTargetIdx2: Int)
    fun finish(resultList: MutableList<SortingDataResult>)
}