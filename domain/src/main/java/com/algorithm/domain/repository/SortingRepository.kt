package com.algorithm.domain.repository//package com.algorithm.domain.repository

import com.algorithm.domain.sorting.ISortingAlgorithm

interface SortingRepository {
    fun getAlgorithm(sortingType: String): ISortingAlgorithm
}