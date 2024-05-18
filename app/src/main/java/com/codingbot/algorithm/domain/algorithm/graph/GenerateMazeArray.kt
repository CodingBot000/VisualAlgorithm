package com.codingbot.algorithm.domain.algorithm.graph

import kotlin.random.Random

class GenerateMazeArray {
    fun generateMaze(rows: Int, cols: Int): Array<IntArray> {
        val maze = Array(rows) { IntArray(cols) { 0 } }
        val visited = Array(rows) { BooleanArray(cols) { false } }

        val startRow = 0
        val startCol = cols - 1
        val endRow = rows - 1
        val endCol = cols - 1

        val directions = listOf(
            Pair(0, 1),  // Right
            Pair(1, 0),  // Down
            Pair(0, -1), // Left
            Pair(-1, 0)  // Up
        )

        fun isValidMove(row: Int, col: Int): Boolean {
            return row in 0 until rows && col in 0 until cols && !visited[row][col]
        }

        fun dfs(row: Int, col: Int) {
            visited[row][col] = true
            directions.shuffled().forEach { (dr, dc) ->
                val newRow = row + dr
                val newCol = col + dc
                if (isValidMove(newRow, newCol)) {
                    // Ensure at least one cell between walls
                    maze[newRow][newCol] = 0
                    dfs(newRow, newCol)
                }
            }
        }

        // Initialize the maze with DFS path
        dfs(startRow, startCol)

        // Ensure start and end points are free
        maze[startRow][startCol] = 0
        maze[endRow][endCol] = 0

        // Ensure at least 40% walls
        val minWalls = (rows * cols * 0.4).toInt()
        var wallCount = 0
        while (wallCount < minWalls) {
            val r = Random.nextInt(rows)
            val c = Random.nextInt(cols)
            if (maze[r][c] == 0 && !(r == startRow && c == startCol) && !(r == endRow && c == endCol)) {
                maze[r][c] = 1
                wallCount++
            }
        }

        return maze
    }

    fun printMaze(maze: Array<IntArray>) {
        println("printMaze !!!!")
        maze.forEach { row ->
            println(row.joinToString(" ") { it.toString() })
        }
    }

}