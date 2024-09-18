package com.algorithm.domain.repository.sorting//package com.algorithm.domain.repository

import com.algorithm.domain.sorting.ISortingAlgorithm

interface SortingRepository {
    fun getAlgorithm(sortingType: String): ISortingAlgorithm
}