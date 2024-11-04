package com.algorithm.model


data class AsynchronousData(
    val coroutineNum: Int = -1,
    val valueA: Int = -1,
    val valueB: Int = -1
) {
    val criticalSection: Boolean
        get() = kotlin.math.abs(valueA - valueB) > 1
}


data class AsynchronousDataResult(
    val sharedCountList: MutableList<Int>,
    val increment: MutableList<AsynchronousData>,
    val raceConditionMap: HashMap<Int, AsynchronousData>
)