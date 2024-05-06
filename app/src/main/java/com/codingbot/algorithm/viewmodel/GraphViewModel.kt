package com.codingbot.algorithm.viewmodel

import androidx.lifecycle.viewModelScope
import com.codingbot.algorithm.core.common.GraphList
import com.codingbot.algorithm.core.common.Logger
import com.codingbot.algorithm.data.model.graph.GraphAlgorithm
import com.codingbot.algorithm.data.model.graph.contract.IDisplayGraphUpdateEvent
import com.codingbot.algorithm.data.model.graph.GraphBFSAlgorithm
import com.codingbot.algorithm.data.model.graph.GraphDFSAlgorithm
import com.codingbot.algorithm.ui.ChannelUiEvent
import com.codingbot.algorithm.ui.UiEvent
import kotlinx.coroutines.launch

data class GraphUiState(
    val startButtonEnable: Boolean = true,
//    val visitedList: Array<BooleanArray> = Array<BooleanArray>(Const.GRAPH_ARRAY_SIZE) {
//        BooleanArray(Const.GRAPH_ARRAY_SIZE)
//    },
//    val visitedList: Array<IntArray> = Array<IntArray>(Const.GRAPH_ARRAY_SIZE) {
//        IntArray(Const.GRAPH_ARRAY_SIZE)
//    },
    val visitedList: List<Boolean> = emptyList(),
    val moveCount: Int = 0,
    val finish: Boolean = false
)

sealed interface GraphIntent {
    data class StartButtonEnable(val enable: Boolean): GraphIntent
    data class ElementList(val list: List<Boolean>): GraphIntent
    data class MoveCount(val moveCount: Int): GraphIntent
    data class Finish(val finish: Boolean): GraphIntent
}

sealed interface GraphUiEvent {

}

class GraphViewModel
    : BaseViewModel<GraphUiState, GraphIntent>(GraphUiState()),
    UiEvent<GraphUiEvent> by ChannelUiEvent()
{
    val logger = Logger("GraphViewModel")

    private val INIT_SPEED = 500f
    private var speed = INIT_SPEED
    private var moveCount = 0
    var arrColSize = 0
    var graphType: String = GraphList.BFS.name

    val start = intArrayOf(0, 4)
    val dest = intArrayOf(4, 4)

    val startIdx: Int
        get() = getFlatArrayIndex(start)
    val destIdx: Int
        get() = getFlatArrayIndex(dest)

    private var graphAlgorithm: GraphAlgorithm? = null

    init {
        initGraph()
    }
    lateinit var baseGridArray: Array<IntArray>
    private fun getFlatArrayIndex(pos: IntArray): Int {
        return pos[0] * arrColSize + pos[1]
    }

    fun startButtonEnabled(enable: Boolean) {
        execute(GraphIntent.StartButtonEnable(enable))
    }
    fun setSpeed(speed: Float) {
        this.speed = speed
        graphAlgorithm?.speedValue = speed
    }
    private fun initGraph() {
        val mazeInit = arrayOf(
            intArrayOf(0, 0, 1, 0, 0),
            intArrayOf(0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 1, 0),
            intArrayOf(1, 1, 0, 1, 1),
            intArrayOf(0, 0, 0, 0, 0)
        )
        arrColSize = mazeInit.size
        baseGridArray = mazeInit
    }

    fun initTrackingMaze(graphType: String) {
        this.graphType = graphType
        when (graphType) {
            GraphList.BFS.name -> {
                graphAlgorithm = GraphBFSAlgorithm()
                (graphAlgorithm as? GraphBFSAlgorithm)?.initValue(
                    viewModelScope = viewModelScope,
                    arr = baseGridArray,
                    iDisplayGraphUpdateEvent = object: IDisplayGraphUpdateEvent {
                        override fun visitedList(list: Array<BooleanArray>) {
                            val flatList = list.flatMap { it.asList() }
                            execute(GraphIntent.ElementList(list = flatList))
                            execute(GraphIntent.MoveCount(moveCount = moveCount))
                        }

                        override fun finish() {
                            execute(GraphIntent.Finish(true))
                        }
                    }
                )
            }
            GraphList.DFS.name -> {
                graphAlgorithm = GraphDFSAlgorithm()
                (graphAlgorithm as? GraphDFSAlgorithm)?.initValue(
                    viewModelScope = viewModelScope,
                    arr = baseGridArray,
                    iDisplayGraphUpdateEvent = object: IDisplayGraphUpdateEvent {
                        override fun visitedList(list: Array<BooleanArray>) {
                            val flatList = list.flatMap { it.asList() }
                            execute(GraphIntent.ElementList(list = flatList))
                            execute(GraphIntent.MoveCount(moveCount = moveCount))
                        }

                        override fun finish() {
                            execute(GraphIntent.Finish(true))
                        }
                    }
                )
            }
        }
    }

    fun start() {
        viewModelScope.launch {
            when (graphType) {
                GraphList.BFS.name -> (graphAlgorithm as? GraphBFSAlgorithm)?.start(start, dest)
                GraphList.DFS.name -> (graphAlgorithm as? GraphDFSAlgorithm)?.start(start, dest)
            }
        }
    }

    fun restart() {
        viewModelScope.launch {
            when (graphType) {
                GraphList.BFS.name -> (graphAlgorithm as? GraphBFSAlgorithm)?.restart(start, dest)
                GraphList.DFS.name -> (graphAlgorithm as? GraphDFSAlgorithm)?.restart(start, dest)
            }
        }
    }
    override suspend fun GraphUiState.reduce(intent: GraphIntent): GraphUiState =
        when (intent) {
            is GraphIntent.StartButtonEnable -> copy(startButtonEnable = intent.enable)
            is GraphIntent.ElementList -> {
                val newList = intent.list.toList()
                copy(visitedList = newList)
            }
            is GraphIntent.Finish -> copy(finish = intent.finish)
            is GraphIntent.MoveCount -> copy(moveCount = intent.moveCount)
        }
}