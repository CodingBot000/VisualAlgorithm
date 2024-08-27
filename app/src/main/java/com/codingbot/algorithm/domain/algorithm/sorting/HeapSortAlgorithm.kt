package com.codingbot.algorithm.domain.algorithm.sorting

import com.codingbot.algorithm.core.common.Logger
import com.codingbot.algorithm.domain.model.SortingData
import com.codingbot.algorithm.domain.model.SortingHeapDataResult
import com.codingbot.algorithm.domain.algorithm.sorting.contract.IDisplayHeapSortingUpdateEvent
import com.codingbot.algorithm.domain.algorithm.sorting.contract.ISortingHeapSortingAlgorithm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class HeapSortAlgorithm: ISortingHeapSortingAlgorithm
{
    val logger = Logger("HeapSortAlgorithm")

    private lateinit var viewModelScope: CoroutineScope
    private lateinit var arrOrigin: MutableList<SortingData>
    private var resultArr: MutableList<SortingHeapDataResult> = mutableListOf<SortingHeapDataResult>()
    private lateinit var iDisplayHeapSortingUpdateEvent: IDisplayHeapSortingUpdateEvent
    private var backupArr = emptyList<SortingData>()
    private var arrSize = 0

    override fun initValue(
        viewModelScope: CoroutineScope,
        sortingListInit: MutableList<SortingData>,
        iDisplayHeapSortingUpdateEvent: IDisplayHeapSortingUpdateEvent
    ) {
        this.viewModelScope = viewModelScope
        this.arrOrigin = sortingListInit
        this.iDisplayHeapSortingUpdateEvent = iDisplayHeapSortingUpdateEvent
        arrSize = arrOrigin.size
        backupArr = arrOrigin.toMutableList()
    }

    override suspend fun start() {
        viewModelScope.launch {
            sort()
        }
    }

    override suspend fun restart() {
        arrOrigin = backupArr.toMutableList()
        start()
    }

    private suspend fun sort() {
        var results = MutableList(arrOrigin.count()) { SortingData() }
        heapSort(
            array = arrOrigin.toMutableList(),
            results = results)

        resultArr.add(
            SortingHeapDataResult(
                sortingDataList = arrOrigin.toMutableList(),
                resultList = results.toMutableList(),
                swapTargetIdx1 = -1,
                swapTargetIdx2 = -1
            )
        )
        iDisplayHeapSortingUpdateEvent.finish(resultArr)
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
            results[index] = array[0]
            index++
            array[0] = array[j]

            resultArr.add(
                SortingHeapDataResult(
                    sortingDataList = array.toMutableList(),
                    resultList = results.toMutableList(),
                    swapTargetIdx1 = -1,
                    swapTargetIdx2 = -1
                )
            )
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

            resultArr.add(
                SortingHeapDataResult(
                    sortingDataList = array.toMutableList(),
                    resultList = results.toMutableList(),
                    swapTargetIdx1 = p,
                    swapTargetIdx2 = pIdx
                )
            )

            array[pIdx] = temp
            resultArr.add(
                SortingHeapDataResult(
                    sortingDataList = array.toMutableList(),
                    resultList = results.toMutableList(),
                    swapTargetIdx1 = pIdx,
                    swapTargetIdx2 = p
                )
            )
            heapify(array, heapSize, p, results)
        }
    }
}