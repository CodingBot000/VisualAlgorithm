package com.algorithm.domain.repository//package com.algorithm.domain.repository

import com.algorithm.domain.graph.IGraphAlgorithm

interface GraphRepository {
    fun getAlgorithm(sortingType: String): IGraphAlgorithm
}