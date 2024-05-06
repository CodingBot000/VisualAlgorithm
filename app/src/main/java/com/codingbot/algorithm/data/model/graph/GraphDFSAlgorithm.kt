package com.codingbot.algorithm.data.model.graph

import com.codingbot.algorithm.data.model.graph.contract.IDisplayGraphUpdateEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class GraphDFSAlgorithm: GraphAlgorithm() {
//    lateinit var mazeArray: Array<Array<GraphData>>
//    fun setInit(mazeArray: Array<Array<GraphData>>) {
//        this.mazeArray = mazeArray
//    }
//    lateinit var mazeArray: Array<IntArray>


    var dirs = arrayOf(intArrayOf(-1, 0), intArrayOf(1, 0), intArrayOf(0, -1), intArrayOf(0, 1))
    var m = 0
    var n = 0
    override fun setSpeed(speed: Float) {
        this.speedValue = speed
    }

    override fun initValue(
        viewModelScope: CoroutineScope,
        arr: Array<IntArray>,
        iDisplayGraphUpdateEvent: IDisplayGraphUpdateEvent
    ) {
        this.viewModelScope = viewModelScope
        this.arr = arr
        this.iDisplayGraphUpdateEvent = iDisplayGraphUpdateEvent

        backupArr = arr.clone()
    }

    override suspend fun start(start: IntArray, dest: IntArray) {
        viewModelScope.launch {
            trackingStart(start, dest)
        }
    }

    override suspend fun restart(start: IntArray, dest: IntArray) {
        arr = backupArr.clone()
        viewModelScope.launch {
            trackingStart(start, dest)
        }
    }

    private suspend fun trackingStart(start: IntArray, dest: IntArray) {
        m = arr.size
        n = arr[0].size
        val visited = Array(m) { BooleanArray(n) }

        dfs(arr, start, dest, visited)
        iDisplayGraphUpdateEvent.finish()
    }

    private suspend fun dfs(
        maze: Array<IntArray>,
        start: IntArray,
        destination: IntArray,
        visited: Array<BooleanArray>
    ): Boolean {
        println(
            "#####  in dfs method start x: " + start[0] + " y: " + start[1] + " visited " + visited[start[0]][start[1]]
        )
        if (start[0] < 0 || start[0] >= m || start[1] < 0 || start[1] >= n || visited[start[0]][start[1]]) {
            println("in dfs return FALSE")
            return false
        }
        visited[start[0]][start[1]] = true
        iDisplayGraphUpdateEvent.visitedList(
            list = visited
        )
        delay(speedValue.toLong())
        print(visited)
        if (start[0] == destination[0] && start[1] == destination[1]) return true
        for (dir in dirs) {
            var x = start[0]
            var y = start[1]
            println("in dfs 11 x: $x y: $y")
            while (x in 0..<m && y >= 0 && y < n && maze[x][y] != 1) {
                x += dir[0]
                y += dir[1]
                println("in dfs 22 x: " + x + " y: " + y + "   dir { " + dir[0] + " " + dir[1] + " }")
            }
            x -= dir[0]
            y -= dir[1]
            println("in dfs new int x: $x y: $y")
            if (dfs(maze, intArrayOf(x, y), destination, visited)) {
                println("in dfs call RESULT return * TRUE")
                return true
            }
            println()
        }
        return false
    }

    private fun print(visited: Array<BooleanArray>?) {

        if (visited.isNullOrEmpty()) return

        for (i in 0 until m) {
            for (j in 0 until n) {
                print("print:${visited[i][j].toString()}" + "\t")
            }
            println()
        }
    }

}