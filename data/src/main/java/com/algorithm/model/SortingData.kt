package com.algorithm.model

data class SortingData(
    val element:Int = 0,
    val scaledNum: Int = 0,
    val swap1:Boolean = false,
    val swap2:Boolean = false
)
data class SortingDataResult(
    val sortingDataList: MutableList<SortingData>,
    val swapTargetIdx1: Int = 0,
    val swapTargetIdx2: Int = 0
)
data class SortingHeapDataResult(
    val sortingDataList: MutableList<SortingData>,
    val resultList: MutableList<SortingData>,
    val swapTargetIdx1: Int = 0,
    val swapTargetIdx2: Int = 0
)
