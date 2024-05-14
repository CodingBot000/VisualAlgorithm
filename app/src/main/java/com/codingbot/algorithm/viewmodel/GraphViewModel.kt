package com.codingbot.algorithm.viewmodel

import androidx.lifecycle.viewModelScope
import com.codingbot.algorithm.core.common.GraphList
import com.codingbot.algorithm.core.common.Logger
import com.codingbot.algorithm.core.common.SortingList
import com.codingbot.algorithm.data.SortingData
import com.codingbot.algorithm.data.model.graph.GraphBFSAlgorithm
import com.codingbot.algorithm.data.model.graph.GraphDFSAlgorithm
import com.codingbot.algorithm.data.model.graph.contract.IDisplayGraphUpdateEvent
import com.codingbot.algorithm.data.model.graph.contract.IGraphAlgorithm
import com.codingbot.algorithm.data.model.sorting.BubbleSortAlgorithm
import com.codingbot.algorithm.data.model.sorting.InsertionSortAlgorithm
import com.codingbot.algorithm.data.model.sorting.QuickSortAlgorithm
import com.codingbot.algorithm.data.model.sorting.SelectionSortAlgorithm
import com.codingbot.algorithm.data.model.sorting.contract.ISortingAlgorithm
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

data class GraphUiState(
    val startButtonEnable: Boolean = true,
    val forwardButtonEnable: Boolean = false,
    val backwardButtonEnable: Boolean = false,
    val playState: PlayState = PlayState.INIT,
    val visitedList: List<Boolean> = emptyList(),
    val moveCount: Int = 0,
    val finish: Boolean = false
)

sealed interface GraphIntent {
    data class ButtonEnableForwardAndBackward(val forwardButtonEnable: Boolean, val backwardButtonEnable: Boolean): GraphIntent
    data class StartButtonEnable(val enable: Boolean): GraphIntent
    data class PlayButtonState(val playState: PlayState): GraphIntent
    data class ElementList(val list: List<Boolean>): GraphIntent
    data class MoveCount(val moveCount: Int): GraphIntent
    data class Finish(val finish: Boolean): GraphIntent
}

class GraphViewModel
    : AlgorithmViewModel<GraphUiState, GraphIntent>(GraphUiState())
{
    val logger = Logger("GraphViewModel")

    lateinit var originArr: Array<IntArray>
    private var resultHistoryList: MutableList<Array<BooleanArray>> = mutableListOf()
    private var algorithm: IGraphAlgorithm? = null

    var arrColSize = 0
    val start = intArrayOf(0, 4)
    val dest = intArrayOf(4, 4)
    val startIdx: Int
        get() = getFlatArrayIndex(start)
    val destIdx: Int
        get() = getFlatArrayIndex(dest)

    init {
        initArray()
    }

    private fun getFlatArrayIndex(pos: IntArray): Int {
        return pos[0] * arrColSize + pos[1]
    }
    private fun getAlogritm(type: String): IGraphAlgorithm =
        when (type) {
            GraphList.BFS.name -> GraphBFSAlgorithm()
            GraphList.DFS.name -> GraphDFSAlgorithm()
            else -> { GraphBFSAlgorithm() }
        }

    fun setSpeedValue(speed: Float) {
        this.speed = speed
    }

    fun startButtonEnabled(enable: Boolean) {
        execute(GraphIntent.StartButtonEnable(enable))
    }

    override fun initArray() {
        val mazeInit = arrayOf(
            intArrayOf(0, 0, 1, 0, 0),
            intArrayOf(0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 1, 0),
            intArrayOf(1, 1, 0, 1, 1),
            intArrayOf(0, 0, 0, 0, 0)
        )
        arrColSize = mazeInit.size
        originArr = mazeInit
    }

    override fun initValue(type: String) {
        this.type = type

        algorithm = getAlogritm(type)
        algorithm?.initValue(
            viewModelScope = viewModelScope,
            graphListInit = originArr,
            iDisplayGraphUpdateEvent = object: IDisplayGraphUpdateEvent {

                override fun finish(resultVisitedArray: MutableList<Array<BooleanArray>>) {
                    execute(GraphIntent.Finish(true))

                    resultHistoryList = resultVisitedArray
                    runAlgorithmProcess()
                }
            }
        )
    }

    override fun runAlgorithmProcess() {
        viewModelScope.launch {
            while (progressIndex < resultHistoryList.size) {
                checkPaused()
                progressIndex++
                updateBars()
                checkPaused() // When reaching the end, check once more and switch to pause mode if it is the end.
                decideForwardBackwardEnable()
            }
        }
    }

    override suspend fun updateBars() {
        try {
            resultHistoryList[progressIndex].run {
                displayBars(this)
                delay(speed.toLong())
                logger { "updateBars speed.toLong(): ${speed.toLong()}" }
            }
            moveCount = progressIndex
        } catch (e: IndexOutOfBoundsException) {
            logger { "updateBars sortingIndexException: $e" }
        }
    }

    override suspend fun checkPaused() {
        if (progressIndex >= resultHistoryList.size -1) {
            curPlayState = PlayState.PAUSE
            pause()
        }
        if (curPlayState == PlayState.PAUSE) {
            suspendCancellableCoroutine<Unit> { continuation = it }
        }
    }

    override fun start() {
        viewModelScope.launch {
            algorithm?.start(start, dest)
            setPlayButtonState(PlayState.PLAYING)
        }
    }

    override fun restart() {
        moveCount = 0
        progressIndex = 0
        viewModelScope.launch {
            initArray()
            algorithm?.restart(start, dest)
            setPlayButtonState(PlayState.PLAYING)
        }
    }
    override fun pause() {
        setPlayButtonState(PlayState.PAUSE)
    }

    override fun resume() {
        setPlayButtonState(PlayState.RESUME)
        continuation?.resume(Unit)
        continuation = null
    }


    override fun forward() {
        viewModelScope.launch {
            if (resultHistoryList.size -1 > progressIndex) {
                progressIndex++
                logger { "forward progressIndex 1:$progressIndex" }
                updateBars()
            } else {
                logger { "forward progressIndex over:$progressIndex" }
                forwardBackwardEnable(
                    forwardButtonEnable = false,
                    backwardButtonEnable = true,
                )
            }
            decideForwardBackwardEnable()
        }
    }

    override fun backward() {
        viewModelScope.launch {
            if (0 < progressIndex) {
                progressIndex--
                logger { "backward progressIndex over:$progressIndex" }
                updateBars()
            } else {
                logger { "backward progressIndex over:$progressIndex" }
                forwardBackwardEnable(
                    forwardButtonEnable = true,
                    backwardButtonEnable = false,
                )
            }
            decideForwardBackwardEnable()
        }
    }

    override fun setPlayButtonState(playState: PlayState) {
        curPlayState = playState
        execute(GraphIntent.PlayButtonState(playState))
        logger { "setPlayButtonState:$playState" }
        decideForwardBackwardEnable()
    }

    override fun decideForwardBackwardEnable() {
        val (forward, backward) = if (curPlayState == PlayState.PAUSE) {
            if (progressIndex >= resultHistoryList.size -1) {
                Pair(false, true)
            } else if (progressIndex <= 0) {
                Pair(true, false)
            } else {
                Pair(true, true)
            }
        } else {
            Pair(false, false)
        }
        forwardBackwardEnable(
            forwardButtonEnable = forward,
            backwardButtonEnable = backward,
        )
    }


    override fun forwardBackwardEnable(
        forwardButtonEnable: Boolean,
        backwardButtonEnable: Boolean
    ) {
        execute(GraphIntent.ButtonEnableForwardAndBackward(forwardButtonEnable, backwardButtonEnable))
    }

    private fun displayBars(
        trackingList: Array<BooleanArray>
    )
    {
        val flatList = trackingList.flatMap { it.asList() }
        execute(GraphIntent.ElementList(list = flatList))
        execute(GraphIntent.MoveCount(moveCount = moveCount))
    }

    override suspend fun GraphUiState.reduce(intent: GraphIntent): GraphUiState =
        when (intent) {
            is GraphIntent.StartButtonEnable -> copy(startButtonEnable = intent.enable)
            is GraphIntent.ButtonEnableForwardAndBackward ->
                copy(
                    forwardButtonEnable = intent.forwardButtonEnable,
                    backwardButtonEnable = intent.backwardButtonEnable
                )
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