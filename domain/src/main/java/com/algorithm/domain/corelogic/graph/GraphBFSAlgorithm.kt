package com.algorithm.domain.corelogic.graph


import com.algorithm.domain.graph.IDisplayGraphUpdateEvent
import com.algorithm.domain.graph.IGraphAlgorithm
import com.algorithm.model.TrackingData
import com.algorithm.model.TrackingDataResult
import com.algorithm.utils.deepCopy
import java.util.LinkedList
import java.util.Queue

class GraphBFSAlgorithm: IGraphAlgorithm {
    private lateinit var arrOrigin: Array<IntArray>
    private lateinit var iDisplayGraphUpdateEvent: IDisplayGraphUpdateEvent
    private var visitedResultHistory: MutableList<TrackingDataResult> = mutableListOf()
    private lateinit var visualVisited:Array<Array<TrackingData>>
    private var backupArr = emptyArray<IntArray>()
    private var order = 0

    private val dirs = arrayOf(intArrayOf(-1, 0), intArrayOf(1, 0), intArrayOf(0, -1), intArrayOf(0, 1))
    private var col = 0
    private var row = 0

    override fun initValue(

        graphListInit: Array<IntArray>,
        iDisplayGraphUpdateEvent: IDisplayGraphUpdateEvent
    ) {
        this.arrOrigin = graphListInit
        this.iDisplayGraphUpdateEvent = iDisplayGraphUpdateEvent

        backupArr = arrOrigin.deepCopy()
    }

    override suspend fun start(start: IntArray, dest: IntArray) {
            trackingStart(start, dest)
    }

    override suspend fun restart(start: IntArray, dest: IntArray) {
        arrOrigin = backupArr.deepCopy()
        trackingStart(start, dest)
    }

    private suspend fun trackingStart(start: IntArray, dest: IntArray) {
        bfs(arrOrigin, start, dest)
        iDisplayGraphUpdateEvent.finish(visitedResultHistory)
    }

    private suspend fun bfs(
        mazeArr: Array<IntArray>,
        start: IntArray,
        destination: IntArray): Boolean
    {
        col = mazeArr.size
        row = mazeArr[0].size

        // Reaching the destination
        if (start[0] == destination[0] && start[1] == destination[1]) {
            return true
        }

        val visited = Array<BooleanArray>(col) { BooleanArray(row) }
        visualVisited = Array(col) { Array(row) { TrackingData() } }

        val queue: Queue<IntArray> = LinkedList()
        visited[start[0]][start[1]] = true
        visualVisited[start[0]][start[1]] =
            TrackingData(
                order = order,
                isVisited = true
            )
        visitedResultHistory.add(
            TrackingDataResult(
                result = visualVisited.deepCopy { it },
                targetX = start[0],
                targetY = start[1],
                order = order
            )
        )
        order++

        queue.offer(intArrayOf(start[0], start[1]))
        while (!queue.isEmpty()) {
            val p = queue.poll()
            for (dir in dirs) {
                //1. 먼저 한쪽방향 (dir)을 정해서 해당 방향으로 아래 while문에서 배열밖으로 나가거나 벽을 만날때까지 계속 움직이메 체크한다
                var x = p[0]
                var y = p[1]
                // 2. dir 동서남북 이동방향을 하나씩 가져와서 아래  while을 수행한다
                while (x in 0..<col && y in 0..<row && mazeArr[x][y] == 0) {
                    // x, y가 위의 크기 조건이고, 현재위치가 0이면, 즉 벽이 아니라면 == 길이라면 루프를 다시 돈다.
                    // 루프를 다시돈다는건, dir로 받은 위치이동값이 더해지므로, 해당방향으로 계속 이동한다는것을 의미한다.
                    // 예를들어 dir이 {-1. 0} 이면 더해줄때마다 왼쪽으로 한칸씩 계속 이동하게 될것이다.
                    // 여기서 x, y는 앞으로 이동하게될 미래의 예상 위치이다.
                    // 그러므로 위치는 배열크기 안으로 while조건문이 제한된다. (배열크기 밖으로 이동할수없으므로)
                    // 벽을 만나거나 밖으로 나가면 while문을 나가게 된다

                    x += dir[0]
                    y += dir[1]
                    if (x in 0..<col && y in 0..<row && mazeArr[x][y] != 1 && !visualVisited[x][y].isVisited) {
                        visualVisited[x][y] =
                            TrackingData(
                                order = order,
                                isVisited = true
                            )
                        visitedResultHistory.add(
                            TrackingDataResult(
                                result = visualVisited.deepCopy { it },
                                targetX = x,
                                targetY = y,
                                order = order
                            )
                        )
                        order++
                    }
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
                visualVisited[x][y] =
                    TrackingData(
                        order = order,
                        isVisited = true
                    )
                visitedResultHistory.add(
                    TrackingDataResult(
                        result = visualVisited.deepCopy { it },
                        targetX = x,
                        targetY = y,
                        order = order
                    )
                )
                order++
                // 목적지에 도달하면 true를 리턴
                if (x == destination[0] && y == destination[1]) return true
                // 6. 이번에 처음 방문한 곳은 queue에 넣는다.
                queue.offer(intArrayOf(x, y))
            }
        }
        return false
    }
}