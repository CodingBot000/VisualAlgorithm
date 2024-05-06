package com.codingbot.algorithm.data.model.sorting

import com.codingbot.algorithm.core.common.Const
import com.codingbot.algorithm.core.common.Logger
import com.codingbot.algorithm.data.SortingData
import com.codingbot.algorithm.data.model.sorting.contract.IDisplayHeapSortingUpdateEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HeapSortAlgorithm
{
    val logger = Logger("HeapSortAlgorithm")

    private lateinit var viewModelScope: CoroutineScope
    private lateinit var arr: MutableList<SortingData>
    private lateinit var iDisplayHeapSortingUpdateEvent: IDisplayHeapSortingUpdateEvent

    private var sortingSpeed: Float = Const.sortingSpeed
    private var backupArr = emptyList<SortingData>()
    private var arrSize = 0

    fun initValue(
        viewModelScope: CoroutineScope,
        arr: MutableList<SortingData>,
        iDisplayHeapSortingUpdateEvent: IDisplayHeapSortingUpdateEvent
    ) {
        this.viewModelScope = viewModelScope
        this.arr = arr
        this.iDisplayHeapSortingUpdateEvent = iDisplayHeapSortingUpdateEvent

        backupArr = arr.toMutableList()
    }
    fun setSpeed(speed: Float) {
        this.sortingSpeed = speed
    }

    suspend fun start() {
        arrSize = arr.size
        viewModelScope.launch {
            sort()
        }
    }

    suspend fun restart() {
        arr = backupArr.toMutableList()
        start()
    }

    private suspend fun sort() {
        var results = MutableList(arr.count()) { SortingData() }
        heapSort(
            array = arr,
            results = results)
        iDisplayHeapSortingUpdateEvent.elementList(
            list = arr,
            resultList = results.toMutableList(),
            swapTargetIdx1 = -1,
            swapTargetIdx2 = -1
        )
        iDisplayHeapSortingUpdateEvent.finish()
    }

    private suspend fun heapSort(
        array: MutableList<SortingData>,
        results: MutableList<SortingData>,
    ): List<SortingData> {
        var arrayLen = array.count()
        var index = 0
        // max heap 초기화
        // 자식 노드를 가진 노드들만 순회
        for (i in arrayLen / 2 - 1 downTo 0) {
            heapify(array, arrayLen, i, results)
        }

        // 하나씩 추출
        for (j in arrayLen - 1 downTo 0) {
//            results.add(array[0])
            results[index] = array[0]
            index++
            array[0] = array[j]
            iDisplayHeapSortingUpdateEvent.elementList(
                list = array,
                resultList = results.toMutableList(),
                swapTargetIdx1 = -1,
                swapTargetIdx2 = -1
            )
            delay(sortingSpeed.toLong())
            heapify(array, --arrayLen, 0, results)
        }
        return results
    }


    private suspend fun heapify(
        array: MutableList<SortingData>,
        heapSize: Int,
        pIdx: Int,
        results: MutableList<SortingData>) {
        // 부모 노드, 좌측 자식 노드, 우측 자식 노드 구함
        var p = pIdx
        var l = pIdx * 2 + 1
        var r = pIdx * 2 + 2
        // 왼쪽 자식 노드와 비교
        if (l < heapSize && array[l].element < array[p].element) {
            p = l
        }
        // 오른쪽 자식 노드와 비교
        if (r < heapSize && array[r].element < array[p].element) {
            p = r
        }

        // 부모 노드의 값보다 자식 노드의 값이 크다면 재귀함수
        if (pIdx != p) {
            val temp = array[p]
            array[p] = array[pIdx]
            iDisplayHeapSortingUpdateEvent.elementList(
                list = array,
                swapTargetIdx1 = p,
                swapTargetIdx2 = pIdx
            )
//            delay(sortingSpeed.toLong())
            array[pIdx] = temp
            iDisplayHeapSortingUpdateEvent.elementList(
                list = array,
                swapTargetIdx1 = pIdx,
                swapTargetIdx2 = p
            )
//            delay(sortingSpeed.toLong())
            heapify(array, heapSize, p, results)
        }
    }
}