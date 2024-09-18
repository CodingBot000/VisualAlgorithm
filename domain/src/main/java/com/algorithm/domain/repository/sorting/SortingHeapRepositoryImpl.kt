package com.algorithm.domain.repository.sorting//package com.algorithm.domain.repository

import com.algorithm.domain.corelogic.sorting.HeapSortAlgorithm
import com.algorithm.domain.sorting.ISortingHeapSortingAlgorithm
import javax.inject.Inject

class SortingHeapRepositoryImpl @Inject constructor() : SortingHeapRepository {
    override fun getAlgorithm(): ISortingHeapSortingAlgorithm =
        HeapSortAlgorithm()
}