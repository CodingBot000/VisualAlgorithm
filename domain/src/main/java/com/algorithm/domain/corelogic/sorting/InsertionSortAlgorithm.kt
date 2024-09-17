package com.algorithm.domain.corelogic.sorting
import com.algorithm.domain.sorting.IDisplaySortingUpdateEvent
import com.algorithm.domain.sorting.ISortingAlgorithm
import com.algorithm.model.SortingData
import com.algorithm.model.SortingDataResult

internal class InsertionSortAlgorithm(): ISortingAlgorithm
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
        for (i in 1 until arrSize) {
            val tmp = arrOrigin[i]
            var j = i - 1
            while (j >= 0 && tmp.element < arrOrigin[j].element) {
                resultArr.add(
                    SortingDataResult(
                        sortingDataList = arrOrigin.toMutableList(),
                        swapTargetIdx1 = j,
                        swapTargetIdx2 = j + 1
                    )
                )
//                iDisplaySortingUpdateEvent.elementList(
//                    list = arr,
//                    swapTargetIdx1 = j,
//                    swapTargetIdx2 = j + 1
//                )
//                delay(sortingSpeed.toLong())
                arrOrigin[j + 1] = arrOrigin[j]
                j--

            }
            arrOrigin[j + 1] = tmp
//            iDisplaySortingUpdateEvent.elementList(
//                list = arr,
//                swapTargetIdx1 = j,
//                swapTargetIdx2 = j + 1
//            )
//            delay(sortingSpeed.toLong())
            resultArr.add(
                SortingDataResult(
                    sortingDataList = arrOrigin.toMutableList(),
                    swapTargetIdx1 = j,
                    swapTargetIdx2 = j + 1
                )
            )
        }
        iDisplaySortingUpdateEvent.finish(resultArr)
    }

}