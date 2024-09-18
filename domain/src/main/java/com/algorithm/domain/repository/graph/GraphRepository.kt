package com.algorithm.domain.repository.graph//package com.algorithm.domain.repository

import com.algorithm.domain.graph.IGraphAlgorithm

interface GraphRepository {
    fun getAlgorithm(sortingType: String): IGraphAlgorithm
}