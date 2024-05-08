package com.codingbot.algorithm.viewmodel

import androidx.lifecycle.viewModelScope
import com.codingbot.algorithm.core.common.GraphList
import com.codingbot.algorithm.core.common.Logger
import com.codingbot.algorithm.data.model.graph.GraphBFSAlgorithm
import com.codingbot.algorithm.data.model.graph.GraphDFSAlgorithm
import com.codingbot.algorithm.data.model.graph.contract.IDisplayGraphUpdateEvent
import com.codingbot.algorithm.data.model.graph.contract.IGraphAlgorithm
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

data class GraphUiState(
    val startButtonEnable: Boolean = true,
//    val visitedList: Array<BooleanArray> = Array<BooleanArray>(Const.GRAPH_ARRAY_SIZE) {
//        BooleanArray(Const.GRAPH_ARRAY_SIZE)
//    },
//    val visitedList: Array<IntArray> = Array<IntArray>(Const.GRAPH_ARRAY_SIZE) {
//        IntArray(Const.GRAPH_ARRAY_SIZE)
//    },
    val playState: PlayState = PlayState.INIT,
    val visitedList: List<Boolean> = emptyList(),
    val moveCount: Int = 0,
    val finish: Boolean = false
)

sealed interface GraphIntent {
    data class StartButtonEnable(val enable: Boolean): GraphIntent
    data class PlayButtonState(val playState: PlayState): GraphIntent
    data class ElementList(val list: List<Boolean>): GraphIntent
    data class MoveCount(val moveCount: Int): GraphIntent
    data class Finish(val finish: Boolean): GraphIntent
}

class GraphViewModel
    : BaseViewModel<GraphUiState, GraphIntent>(GraphUiState())
{
    val logger = Logger("GraphViewModel")

    private val INIT_SPEED = 500f
    private var speed = INIT_SPEED
    private var moveCount = 0
    var arrColSize = 0
    var graphType: String = GraphList.BFS.name

    private lateinit var trackingResultHistoryList: MutableList<Array<BooleanArray>>
    private var graphAlgorithm: IGraphAlgorithm? = null
    private var continuation: Continuation<Unit>? = null
    private var curPlayState = PlayState.INIT
    private var sortingProgressIndex = 0

    val start = intArrayOf(0, 4)
    val dest = intArrayOf(4, 4)

    val startIdx: Int
        get() = getFlatArrayIndex(start)
    val destIdx: Int
        get() = getFlatArrayIndex(dest)


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
//        graphAlgorithm?.setSpeed(speed)
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
            GraphList.BFS.name -> graphAlgorithm = GraphBFSAlgorithm()
            GraphList.DFS.name -> graphAlgorithm = GraphDFSAlgorithm()
        }

        graphAlgorithm?.initValue(
            viewModelScope = viewModelScope,
            graphListInit = baseGridArray,
            iDisplayGraphUpdateEvent = object: IDisplayGraphUpdateEvent {
//                override fun visitedList(visitedList: Array<BooleanArray>) {
//                    val flatList = visitedList.flatMap { it.asList() }
//                    execute(GraphIntent.ElementList(list = flatList))
//                    execute(GraphIntent.MoveCount(moveCount = moveCount))
//                }

                override fun finish(resultVisitedArray: MutableList<Array<BooleanArray>>) {
                    execute(GraphIntent.Finish(true))

                    trackingResultHistoryList = resultVisitedArray
                    runAlgorithmProcess()
                }
            }
        )
    }

    private fun runAlgorithmProcess() {
        viewModelScope.launch {
            while (sortingProgressIndex < trackingResultHistoryList.size) {
                checkPaused()
                sortingProgressIndex++
                updateBars()
//                decideForwardBackwardEnable()
            }
        }
    }
    private suspend fun updateBars() {
        try {
            trackingResultHistoryList[sortingProgressIndex].run {
//                displayBars(sortingDataList, swapTargetIdx1, swapTargetIdx2)
                val flatList = this.flatMap { it.asList() }
                execute(GraphIntent.ElementList(list = flatList))
                execute(GraphIntent.MoveCount(moveCount = moveCount))
                delay(speed.toLong())
            }
        } catch (e: IndexOutOfBoundsException) {
            logger { "updateBars sortingIndexException: $e" }
        }
    }
    private suspend fun checkPaused() {
        if (curPlayState == PlayState.PAUSE) {
            suspendCancellableCoroutine<Unit> { continuation = it }
        }
    }

    fun setPlayButtonState(playState: PlayState) {
        curPlayState = playState
        execute(GraphIntent.PlayButtonState(playState))
        logger { "setPlayButtonState:$playState" }
//        decideForwardBackwardEnable()
    }
    fun pauseTracking() {
        setPlayButtonState(PlayState.PAUSE)

    }

    fun resumeTracking() {
        setPlayButtonState(PlayState.RESUME)
        continuation?.resume(Unit)
        continuation = null
    }

    fun start() {
        viewModelScope.launch {
            graphAlgorithm?.start(start, dest)
            setPlayButtonState(PlayState.PLAYING)
        }
    }

    fun restart() {
        moveCount = 0
        sortingProgressIndex = 0
        viewModelScope.launch {
            initGraph()
            graphAlgorithm?.restart(start, dest)
            setPlayButtonState(PlayState.PLAYING)
        }
    }


    override suspend fun GraphUiState.reduce(intent: GraphIntent): GraphUiState =
        when (intent) {
            is GraphIntent.StartButtonEnable -> copy(startButtonEnable = intent.enable)
            is GraphIntent.PlayButtonState -> {
                copy(playState = intent.playState)
            }
            is GraphIntent.ElementList -> {
                val newList = intent.list.toList()
                copy(visitedList = newList)
            }
            is GraphIntent.Finish -> copy(finish = intent.finish)
            is GraphIntent.MoveCount -> copy(moveCount = intent.moveCount)
        }
}