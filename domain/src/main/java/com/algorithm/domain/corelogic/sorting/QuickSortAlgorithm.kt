package com.algorithm.domain.corelogic.sorting

import com.algorithm.domain.sorting.IDisplaySortingUpdateEvent
import com.algorithm.domain.sorting.ISortingAlgorithm
import com.algorithm.model.SortingData
import com.algorithm.model.SortingDataResult
import java.util.Collections.swap

internal class QuickSortAlgorithm(): ISortingAlgorithm
{

    private lateinit var arrOrigin: MutableList<SortingData>
    private var resultArr: MutableList<SortingDataResult> = mutableListOf<SortingDataResult>()
    private lateinit var iDisplaySortingUpdateEvent: IDisplaySortingUpdateEvent
    private var backupArr = emptyList<SortingData>()
    private var arrSize = 0

    override fun initValue(
        sortingListInit: MutableList<SortingData>,
        iDisplaySortingUpdateEvent: IDisplaySortingUpdateEvent
    ) {
        this.arrOrigin = sortingListInit
        this.iDisplaySortingUpdateEvent = iDisplaySortingUpdateEvent
        arrSize = arrOrigin.size
        backupArr = sortingListInit.toMutableList()
    }

    override suspend fun start() {
        sort()
    }

    override suspend fun restart() {
        arrOrigin = backupArr.toMutableList()
        start()
    }

    private suspend fun sort() {
        quickSort(arrOrigin)
        iDisplaySortingUpdateEvent.finish(resultArr)
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
//                iDisplaySortingUpdateEvent.elementList(
//                    list = arr,
//                    swapTargetIdx1 = left,
//                    swapTargetIdx2 = right
//                )
//                delay(sortingSpeed.toLong())
                resultArr.add(
                    SortingDataResult(
                        sortingDataList = arr.toMutableList(),
                        swapTargetIdx1 = left,
                        swapTargetIdx2 = right
                    )
                )
                swap(arr, left, right)
//                iDisplaySortingUpdateEvent.elementList(
//                    list = arr,
//                    swapTargetIdx1 = left,
//                    swapTargetIdx2 = right
//                )
//                delay(sortingSpeed.toLong())
                resultArr.add(
                    SortingDataResult(
                        sortingDataList = arr.toMutableList(),
                        swapTargetIdx1 = left,
                        swapTargetIdx2 = right
                    )
                )
                left++
                right--
            }
        }
        return left
    }
}