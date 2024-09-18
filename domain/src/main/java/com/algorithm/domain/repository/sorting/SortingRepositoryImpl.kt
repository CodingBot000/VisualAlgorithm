package com.algorithm.domain.repository.sorting//package com.algorithm.domain.repository

import com.algorithm.common.SortingList
import com.algorithm.domain.corelogic.sorting.BubbleSortAlgorithm
import com.algorithm.domain.corelogic.sorting.InsertionSortAlgorithm
import com.algorithm.domain.corelogic.sorting.QuickSortAlgorithm
import com.algorithm.domain.corelogic.sorting.SelectionSortAlgorithm
import com.algorithm.domain.sorting.ISortingAlgorithm
import javax.inject.Inject

class SortingRepositoryImpl @Inject constructor() : SortingRepository {
    override fun getAlgorithm(sortingType: String): ISortingAlgorithm =
        when (sortingType) {
            SortingList.BUBBLE_SORT.name -> {
                BubbleSortAlgorithm()
            }
            SortingList.SELECTION_SORT.name -> {
                SelectionSortAlgorithm()
            }
            SortingList.INSERTION_SORT.name -> {
                InsertionSortAlgorithm()
            }
            SortingList.QUICK_SORT.name -> {
                QuickSortAlgorithm()
            }
            else -> {
                BubbleSortAlgorithm()
            }
        }

}