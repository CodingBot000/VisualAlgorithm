package com.codingbot.algorithm.data.model.sorting

import com.codingbot.algorithm.core.common.Const
import com.codingbot.algorithm.core.common.Logger
import com.codingbot.algorithm.data.SortingData
import com.codingbot.algorithm.data.model.sorting.contract.IDisplaySortingUpdateEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Collections.swap

class QuickSortAlgorithm(): ISortingAlgorithm
{
    val logger = Logger("QuickSortAlgorithm")

    private lateinit var viewModelScope: CoroutineScope
    private lateinit var arr: MutableList<SortingData>
    private lateinit var iDisplaySortingUpdateEvent: IDisplaySortingUpdateEvent

    private var sortingSpeed: Float = Const.sortingSpeed
    private var backupArr = emptyList<SortingData>()
    private var arrSize = 0

    override fun initValue(
        viewModelScope: CoroutineScope,
        arr: MutableList<SortingData>,
        iDisplaySortingUpdateEvent: IDisplaySortingUpdateEvent
    ) {
        this.viewModelScope = viewModelScope
        this.arr = arr
        this.iDisplaySortingUpdateEvent = iDisplaySortingUpdateEvent

        backupArr = arr.toMutableList()
    }
    override fun setSpeed(speed: Float) {
        this.sortingSpeed = speed
    }

    override suspend fun start() {
        arrSize = arr.size
        viewModelScope.launch {
            sort()
        }
    }

    override suspend fun restart() {
        arr = backupArr.toMutableList()
        start()
    }

    private suspend fun sort() {
        quickSort(arr)
        iDisplaySortingUpdateEvent.finish()
    }

    private suspend fun quickSort(arr: MutableList<SortingData>) {
        quickSort(arr, 0, arr.size - 1)
    }

    private suspend fun quickSort(arr: MutableList<SortingData>, left: Int, right: Int) {
        val part = partition(arr, left, right)
        if (left < part - 1) {
            quickSort(arr, left, part - 1)
        }
        if (part < right) {
            quickSort(arr, part, right)
        }
    }

    private suspend fun partition(arr: MutableList<SortingData>, left: Int, right: Int): Int {
        var left = left
        var right = right
        val pivot = arr[(left + right) / 2]
        while (left <= right) { // 해당 부분 배열에 대해 반복
            while (arr[left].element < pivot.element) { //피벗보다 큰 left를 찾음
                left++
            }
            while (arr[right].element > pivot.element) { //피벗보다 작은 right를 찾음
                right--
            }
            if (left <= right) { //left가 right보다 왼쪽에 있으면 둘이 자리 바꿈
                iDisplaySortingUpdateEvent.elementList(
                    list = arr,
                    swapTargetIdx1 = left,
                    swapTargetIdx2 = right
                )
                delay(sortingSpeed.toLong())
                swap(arr, left, right)
                iDisplaySortingUpdateEvent.elementList(
                    list = arr,
                    swapTargetIdx1 = left,
                    swapTargetIdx2 = right
                )
                delay(sortingSpeed.toLong())
                left++
                right--
            }
        }
        return left
    }
}