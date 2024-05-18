package com.codingbot.algorithm.domain.algorithm.sorting.contract

import com.codingbot.algorithm.domain.model.SortingData
import com.codingbot.algorithm.domain.model.SortingDataResult

interface IDisplaySortingUpdateEvent {
    fun elementList(list: MutableList<SortingData>,
                    swapTargetIdx1: Int,
                    swapTargetIdx2: Int)
    fun finish(resultList: MutableList<SortingDataResult>)
}