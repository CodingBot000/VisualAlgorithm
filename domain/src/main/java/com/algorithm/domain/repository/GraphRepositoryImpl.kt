package com.algorithm.domain.repository//package com.algorithm.domain.repository

import com.algorithm.domain.corelogic.graph.GraphBFSAlgorithm
import com.algorithm.domain.corelogic.graph.GraphDFSAlgorithm
import com.algorithm.domain.graph.IGraphAlgorithm
import javax.inject.Inject

class GraphRepositoryImpl @Inject constructor() : GraphRepository {
    override fun getAlgorithm(type: String): IGraphAlgorithm =
        when (type) {
            com.algorithm.common.GraphList.BFS.name -> GraphBFSAlgorithm()
            com.algorithm.common.GraphList.DFS.name -> GraphDFSAlgorithm()
            else -> {
                GraphBFSAlgorithm()
            }
        }
}