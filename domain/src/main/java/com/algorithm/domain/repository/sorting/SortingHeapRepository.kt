package com.algorithm.domain.repository.sorting//package com.algorithm.domain.repository

import com.algorithm.domain.sorting.ISortingHeapSortingAlgorithm

interface SortingHeapRepository {
    fun getAlgorithm(): ISortingHeapSortingAlgorithm
}