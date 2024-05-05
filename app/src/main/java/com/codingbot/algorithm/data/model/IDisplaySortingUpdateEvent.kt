package com.codingbot.algorithm.data.model

import com.codingbot.algorithm.data.SortingData


interface IDisplaySortingUpdateEvent {
    fun elementList(list: MutableList<SortingData>,
                    swapTargetIdx1: Int,
                    swapTargetIdx2: Int)
    fun finish()
}