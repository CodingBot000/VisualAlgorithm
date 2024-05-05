package com.codingbot.algorithm.data.model.graph

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.LinkedList

import java.util.Queue




class GraphBFSAlgorithm: GraphAlgorithm() {
//    lateinit var mazeArray: Array<Array<GraphData>>
//    fun setInit(mazeArray: Array<Array<GraphData>>) {
//        this.mazeArray = mazeArray
//    }
//    lateinit var mazeArray: Array<IntArray>

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

        bfs(arr, start, dest)
        iDisplayGraphUpdateEvent.finish()
    }


    var dirs = arrayOf(intArrayOf(-1, 0), intArrayOf(1, 0), intArrayOf(0, -1), intArrayOf(0, 1))
    var m = 0
    var n = 0

    private suspend fun bfs(maze: Array<IntArray>, start: IntArray, destination: IntArray): Boolean {
        m = maze.size
        n = maze[0].size
        if (start[0] == destination[0] && start[1] == destination[1]) return true
        val visited = Array<BooleanArray>(m) {
            BooleanArray(n)
        }
        val queue: Queue<IntArray> = LinkedList()
        visited[start[0]][start[1]] = true
        iDisplayGraphUpdateEvent.visitedList(
            list = visited
        )
        delay(speedValue.toLong())
        queue.offer(intArrayOf(start[0], start[1]))
        while (!queue.isEmpty()) {
            val p = queue.poll()
            println("===*** while START queue.poll => p[] {" + p[0] + ", " + p[1] + "}")
            for (dir in dirs) {
                //1. 먼저 한쪽방향 (dir)을 정해서 해당 방향으로 아래 while문에서 배열밖으로 나가거나 벽을 만날때까지 계속 움직이메 체크한다
                var x = p[0]
                var y = p[1]
                // 2. dir 동서남북 이동방향을 하나씩 가져와서 아래  while을 수행한다
                while (x >= 0 && x < m && y >= 0 && y < n && maze[x][y] == 0) {
                    // x, y가 위의 크기 조건이고, 현재위치가 0이면, 즉 벽이 아니라면 == 길이라면 루프를 다시 돈다.
                    // 루프를 다시돈다는건, dir로 받은 위치이동값이 더해지므로, 해당방향으로 계속 이동한다는것을 의미한다.
                    // 예를들어 dir이 {-1. 0} 이면 더해줄때마다 왼쪽으로 한칸씩 계속 이동하게 될것이다.
                    // 여기서 x, y는 앞으로 이동하게될 미래의 예상 위치이다.
                    // 그러므로 위치는 배열크기 안으로 while조건문이 제한된다. (배열크기 밖으로 이동할수없으므로)
                    // 벽을 만나거나 밖으로 나가면 while문을 나가게 된다
                    println("")
                    println("11 x: $x y: $y")
                    x += dir[0]
                    y += dir[1]
                    println("22x: $x y: $y")
                }
                // 3. 위에서 막힌데까지 이동한뒤에 막힌걸 인식했으므로 다시 이전자리로 되돌아가야한다.
                // 예를 들어 첫번째 이동은 시작위치 (0,4)
                // 위에 while문에서 dir {-1. 0}과 더해져서 (-1, 4) 가 된다.
                // x>= 이므로 조건에 맞지않아 (-1) while문을 나오고
                // 아래의 -= 연산을 수행하게되면 다시 (0, 4) 처음으로 되돌아간다. 즉,
                // while문을 돌다가 벽(1)을 만나거나 밖으로 나가게되면 마지막자리로 다시 뒤돌아간다
                x -= dir[0]
                y -= dir[1]
                println("33 backx: " + x + " y: " + y + " /visited[x][y]:" + visited[x][y])
                // 4. 되돌아간 위치가 방문한적이 있다면 continue한다. (최초시작점은 루프전에 visited로 true했으니 시작점은 바로 continue된다)
                if (visited[x][y]) continue
                // 5. 방문한적이 없다면 방문한 표시를 한다.
                visited[x][y] = true
                iDisplayGraphUpdateEvent.visitedList(
                    list = visited
                )
                delay(speedValue.toLong())
                println("check visited[$x][$y] true addQueue {$x, $y}")
                print(visited)
                println("========")

                // 목적지에 도달하면 true를 리턴
                if (x == destination[0] && y == destination[1]) return true
                // 6. 이번에 처음 방문한 곳은 queue에 넣는다.
                queue.offer(intArrayOf(x, y))
            }
            println("==== while End")
        }
        return false
    }

    private fun print(visited: Array<BooleanArray>?) {
        println("qqqqqqqqq")
        if (visited == null || visited.size == 0) return
        println("qqqqqqqqq")
        for (i in 0 until m) {
            for (j in 0 until n) {
                print(visited[i][j].toString() + "\t")
            }
            println()
        }
    }

}