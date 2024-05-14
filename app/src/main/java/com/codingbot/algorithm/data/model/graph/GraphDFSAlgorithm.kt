package com.codingbot.algorithm.data.model.graph

import com.codingbot.algorithm.core.utils.deepCopy
import com.codingbot.algorithm.data.model.graph.contract.IDisplayGraphUpdateEvent
import com.codingbot.algorithm.data.model.graph.contract.IGraphAlgorithm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class GraphDFSAlgorithm: IGraphAlgorithm {
    private lateinit var viewModelScope: CoroutineScope
    private lateinit var arrOrigin: Array<IntArray>
    private var visitedResultHistory: MutableList<Array<BooleanArray>> = mutableListOf()
    private lateinit var visualVisited: Array<BooleanArray>
    private lateinit var iDisplayGraphUpdateEvent: IDisplayGraphUpdateEvent
    private var backupArr = emptyArray<IntArray>()

    private val dirs = arrayOf(intArrayOf(-1, 0), intArrayOf(1, 0), intArrayOf(0, -1), intArrayOf(0, 1))
    private var col = 0
    private var row = 0

    override fun initValue(
        viewModelScope: CoroutineScope,
        graphListInit: Array<IntArray>,
        iDisplayGraphUpdateEvent: IDisplayGraphUpdateEvent
    ) {
        this.viewModelScope = viewModelScope
        this.arrOrigin = graphListInit
        this.iDisplayGraphUpdateEvent = iDisplayGraphUpdateEvent

        backupArr = arrOrigin.deepCopy()
    }

    override suspend fun start(start: IntArray, dest: IntArray) {
        viewModelScope.launch {
            trackingStart(start, dest)
        }
    }

    override suspend fun restart(start: IntArray, dest: IntArray) {
        arrOrigin = backupArr.deepCopy()
        viewModelScope.launch {
            trackingStart(start, dest)
        }
    }

    private suspend fun trackingStart(start: IntArray, dest: IntArray) {
        col = arrOrigin.size
        row = arrOrigin[0].size
        val visited = Array(col) { BooleanArray(row) }
        visualVisited = Array(col) { BooleanArray(row) }
        dfs(arrOrigin, start, dest, visited)
        iDisplayGraphUpdateEvent.finish(visitedResultHistory)
    }

    private suspend fun dfs(
        mazeArr: Array<IntArray>,
        start: IntArray,
        destination: IntArray,
        visitedCheck: Array<BooleanArray>
    ): Boolean {
        println(
            "#####  in dfs method start x: " + start[0] + " y: " + start[1] + " visited " + visitedCheck[start[0]][start[1]]
        )

        if (start[0] < 0 || start[0] >= col || start[1] < 0 || start[1] >= row || visitedCheck[start[0]][start[1]]) {
            println("in dfs return FALSE")
            return false
        }
        visitedCheck[start[0]][start[1]] = true
        visualVisited[start[0]][start[1]] = true
        visitedResultHistory.add(visualVisited.deepCopy())

        print(visitedCheck)
        if (start[0] == destination[0] && start[1] == destination[1]) return true
        for (dir in dirs) {
            var x = start[0]
            var y = start[1]
            println("in dfs 11 x: $x y: $y")
            while (x in 0..<col && y in 0..<row && mazeArr[x][y] != 1)
            {
                x += dir[0]
                y += dir[1]
                println("in dfs 22 x: " + x + " y: " + y + "   dir { " + dir[0] + " " + dir[1] + " }")
                if (x in 0..<col && y in 0..<row && mazeArr[x][y] != 1 && !visualVisited[x][y]) {
                    visualVisited[x][y] = true
                    visitedResultHistory.add(visualVisited.deepCopy())
                }

            }
            x -= dir[0]
            y -= dir[1]

            println("in dfs new int x: $x y: $y")
            if (dfs(mazeArr, intArrayOf(x, y), destination, visitedCheck)) {
                println("in dfs call RESULT return * TRUE")
                return true
            }
            println()
        }
        return false
    }

//    private fun print(visited: Array<BooleanArray>?) {
//        if (visited.isNullOrEmpty()) return
//
//        for (i in 0 until m) {
//            for (j in 0 until n) {
//                print("print:${visited[i][j].toString()}" + "\t")
//            }
//            println()
//        }
//    }

}