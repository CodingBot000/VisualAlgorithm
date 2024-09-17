package com.algorithm.domain.repository//package com.algorithm.domain.repository

import com.algorithm.domain.corelogic.sorting.HeapSortAlgorithm
import com.algorithm.domain.sorting.ISortingHeapSortingAlgorithm

interface SortingHeapRepository {
    fun getAlgorithm(): ISortingHeapSortingAlgorithm
}