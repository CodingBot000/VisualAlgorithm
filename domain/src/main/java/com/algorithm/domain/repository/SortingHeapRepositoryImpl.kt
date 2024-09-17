package com.algorithm.domain.repository//package com.algorithm.domain.repository

import com.algorithm.common.SortingList
import com.algorithm.domain.corelogic.sorting.BubbleSortAlgorithm
import com.algorithm.domain.corelogic.sorting.HeapSortAlgorithm
import com.algorithm.domain.corelogic.sorting.InsertionSortAlgorithm
import com.algorithm.domain.corelogic.sorting.QuickSortAlgorithm
import com.algorithm.domain.corelogic.sorting.SelectionSortAlgorithm
import com.algorithm.domain.sorting.ISortingAlgorithm
import com.algorithm.domain.sorting.ISortingHeapSortingAlgorithm
import javax.inject.Inject

class SortingHeapRepositoryImpl @Inject constructor() : SortingHeapRepository {
    override fun getAlgorithm(): ISortingHeapSortingAlgorithm =
        HeapSortAlgorithm()
}