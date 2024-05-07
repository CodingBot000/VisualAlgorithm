package com.codingbot.algorithm.data.model.graph

import com.codingbot.algorithm.core.common.Const
import com.codingbot.algorithm.data.model.graph.contract.IDisplayGraphUpdateEvent
import com.codingbot.algorithm.data.model.graph.contract.IGraphAlgorithm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class GraphDFSAlgorithm: IGraphAlgorithm {
//    lateinit var mazeArray: Array<Array<GraphData>>
//    fun setInit(mazeArray: Array<Array<GraphData>>) {
//        this.mazeArray = mazeArray
//    }
//    lateinit var mazeArray: Array<IntArray>
    lateinit var viewModelScope: CoroutineScope
    lateinit var arrOrigin: Array<IntArray>
    lateinit var iDisplayGraphUpdateEvent: IDisplayGraphUpdateEvent
    private var speedValue: Float = Const.sortingSpeed
    private var backupArr = emptyArray<IntArray>()

    var dirs = arrayOf(intArrayOf(-1, 0), intArrayOf(1, 0), intArrayOf(0, -1), intArrayOf(0, 1))
    var col = 0
    var row = 0
    override fun setSpeed(speed: Float) {
        this.speedValue = speed
    }

    override fun initValue(
        viewModelScope: CoroutineScope,
        graphListInit: Array<IntArray>,
        iDisplayGraphUpdateEvent: IDisplayGraphUpdateEvent
    ) {
        this.viewModelScope = viewModelScope
        this.arrOrigin = graphListInit
        this.iDisplayGraphUpdateEvent = iDisplayGraphUpdateEvent

        backupArr = arrOrigin.clone()
    }

    override suspend fun start(start: IntArray, dest: IntArray) {
        viewModelScope.launch {
            trackingStart(start, dest)
        }
    }

    override suspend fun restart(start: IntArray, dest: IntArray) {
        arrOrigin = backupArr.clone()
        viewModelScope.launch {
            trackingStart(start, dest)
        }
    }

    private suspend fun trackingStart(start: IntArray, dest: IntArray) {
        col = arrOrigin.size
        row = arrOrigin[0].size
        val visited = Array(col) { BooleanArray(row) }

        dfs(arrOrigin, start, dest, visited)
        iDisplayGraphUpdateEvent.finish()
    }

    private suspend fun dfs(
        mazeArr: Array<IntArray>,
        start: IntArray,
        destination: IntArray,
        visited: Array<BooleanArray>
    ): Boolean {
        println(
            "#####  in dfs method start x: " + start[0] + " y: " + start[1] + " visited " + visited[start[0]][start[1]]
        )
        if (start[0] < 0 || start[0] >= col || start[1] < 0 || start[1] >= row || visited[start[0]][start[1]]) {
            println("in dfs return FALSE")
            return false
        }
        visited[start[0]][start[1]] = true
        iDisplayGraphUpdateEvent.visitedList(
            visitedList = visited
        )
        delay(speedValue.toLong())
        print(visited)
        if (start[0] == destination[0] && start[1] == destination[1]) return true
        for (dir in dirs) {
            var x = start[0]
            var y = start[1]
            println("in dfs 11 x: $x y: $y")
            while (x in 0..<col && y >= 0 && y < row && mazeArr[x][y] != 1) {
                x += dir[0]
                y += dir[1]
                println("in dfs 22 x: " + x + " y: " + y + "   dir { " + dir[0] + " " + dir[1] + " }")
            }
            x -= dir[0]
            y -= dir[1]
            println("in dfs new int x: $x y: $y")
            if (dfs(mazeArr, intArrayOf(x, y), destination, visited)) {
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