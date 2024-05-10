package com.codingbot.algorithm.viewmodel

import androidx.lifecycle.viewModelScope
import com.codingbot.algorithm.core.common.Const
import com.codingbot.algorithm.core.common.Logger
import com.codingbot.algorithm.core.utils.scaledNumber
import com.codingbot.algorithm.data.SortingData
import com.codingbot.algorithm.data.SortingHeapDataResult
import com.codingbot.algorithm.data.model.sorting.HeapSortAlgorithm
import com.codingbot.algorithm.data.model.sorting.contract.IDisplayHeapSortingUpdateEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.random.Random

data class HeapSortingUiState(
    val startButtonEnable: Boolean = true,
    val forwardButtonEnable: Boolean = false,
    val backwardButtonEnable: Boolean = false,
    val playState: PlayState = PlayState.INIT,
    val elementList: MutableList<SortingData> = mutableListOf<SortingData>(),
    val heapSortingResultList: MutableList<SortingData> = mutableListOf<SortingData>(),
    val resultList: MutableList<MutableList<SortingHeapDataResult>> = mutableListOf<MutableList<SortingHeapDataResult>>(),
    val finish: Boolean = false,
    val moveCount: Int = 0
)

sealed interface HeapSortingIntent {
    data class StartButtonEnable(val enable: Boolean): HeapSortingIntent
    data class ButtonEnableForwardAndBackward(val forwardButtonEnable: Boolean, val backwardButtonEnable: Boolean): HeapSortingIntent
    data class PlayButtonState(val playState: PlayState): HeapSortingIntent
    data class ElementList(val list: MutableList<SortingData>): HeapSortingIntent
    data class HeapSortingResultList(val heapSortingResultList: MutableList<SortingData>): HeapSortingIntent
    data class MoveCount(val moveCount: Int): HeapSortingIntent
    data class FinishSorting(val sortingType: String, val enable: Boolean): HeapSortingIntent
}

@HiltViewModel
class SortingHeapSortingViewModel @Inject constructor()
    : AlgorithmViewModel<HeapSortingUiState, HeapSortingIntent>(HeapSortingUiState())
{
    val logger = Logger("SortingHeapSortingViewModel")

    private val originArr = mutableListOf<SortingData>()
    private var resultHistoryList: MutableList<SortingHeapDataResult> = mutableListOf()
    private var algorithm: HeapSortAlgorithm? = null

    init {
        initArray()
    }

    private fun getAlogrithm() = HeapSortAlgorithm()


    fun setSpeedValue(speed: Float) {
        this.speed = speed
    }

    override fun initArray() {
        moveCount = 0
        progressIndex = 0
        speed = INIT_SPEED

        val randomValues = Array(Const.ARRAYS_SIZE) { Random.nextInt(ELEMENT_RANDOM_FROM, ELEMENT_RANDOM_TO) }
        val scaledNumberList = scaledNumber(
            randomValues =randomValues,
            from = Const.GRAPH_HEIGHT_FROM,
            to = Const.GRAPH_HEIGHT_TO
        )
        randomValues.forEachIndexed { index, randomNum ->
            originArr.add(
                SortingData(element = randomNum,
                    scaledNum = scaledNumberList[index])
            )
        }

        var resultsEmpty = MutableList(originArr.count()) { SortingData() }
        execute(HeapSortingIntent.HeapSortingResultList(heapSortingResultList = resultsEmpty))
        execute(HeapSortingIntent.ElementList(originArr))
    }
    override fun initValue(type: String) {
        this.type = type

        algorithm = getAlogrithm()
        algorithm?.initValue(
            viewModelScope = viewModelScope,
            sortingListInit = originArr,
            iDisplayHeapSortingUpdateEvent = object: IDisplayHeapSortingUpdateEvent {

                override fun finish(resultList: MutableList<SortingHeapDataResult>) {
                    execute(HeapSortingIntent.FinishSorting(type, true))

                    resultHistoryList = resultList
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
                decideForwardBackwardEnable()
            }
        }
    }

    override suspend fun updateBars() {
        try {
            with(resultHistoryList[progressIndex]) {
                displayBars(sortingDataList, resultList, swapTargetIdx1, swapTargetIdx2)
                delay(speed.toLong())
            }
        } catch (e: IndexOutOfBoundsException) {
            logger { "updateBars sortingIndexException: $e" }
        }
    }

    override suspend fun checkPaused() {
        if (curPlayState == PlayState.PAUSE) {
            suspendCancellableCoroutine<Unit> { continuation = it }
        }
    }

    override fun start() {
        viewModelScope.launch {
            algorithm?.start()
            setPlayButtonState(PlayState.PLAYING)
        }
    }

    override fun restart() {
        moveCount = 0
        progressIndex = 0
        execute(HeapSortingIntent.ElementList(originArr))
        viewModelScope.launch {
            algorithm?.restart()
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
        execute(HeapSortingIntent.PlayButtonState(playState))
        logger { "setPlayButtonState:$playState" }
        decideForwardBackwardEnable()
    }

    override fun decideForwardBackwardEnable() {
        val (forward, backward) =
            if (curPlayState == PlayState.PAUSE) {
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
        execute(HeapSortingIntent.ButtonEnableForwardAndBackward(forwardButtonEnable, backwardButtonEnable))
    }

    fun startButtonEnabled(enable: Boolean) {
        execute(HeapSortingIntent.StartButtonEnable(enable))
    }

    private fun displayBars(
        sortingList: MutableList<SortingData>,
        resultList: MutableList<SortingData>,
        swapTargetIdx1: Int,
        swapTargetIdx2: Int)
    {
        for (i in sortingList.indices) {
            sortingList[i] =
                if (i == swapTargetIdx1) {
                    SortingData(
                        element = sortingList[i].element,
                        scaledNum = sortingList[i].scaledNum,
                        swap1 = true,
                        swap2 = false
                    )
                } else if (i == swapTargetIdx2) {
                    SortingData(
                        element = sortingList[i].element,
                        scaledNum = sortingList[i].scaledNum,
                        swap1 = false,
                        swap2 = true
                    )
                } else {
                    SortingData(
                        element = sortingList[i].element,
                        scaledNum = sortingList[i].scaledNum,
                        swap1 = false,
                        swap2 = false
                    )
                }
        }

        moveCount = progressIndex

        execute(HeapSortingIntent.HeapSortingResultList(heapSortingResultList = resultList))
        execute(HeapSortingIntent.ElementList(list = sortingList))
        execute(HeapSortingIntent.MoveCount(moveCount = moveCount))
    }

    override suspend fun HeapSortingUiState.reduce(intent: HeapSortingIntent): HeapSortingUiState =
        when (intent) {
            is HeapSortingIntent.ButtonEnableForwardAndBackward -> {
                copy(forwardButtonEnable = intent.forwardButtonEnable,
                    backwardButtonEnable = intent.backwardButtonEnable)
            }
            is HeapSortingIntent.StartButtonEnable -> copy(startButtonEnable = intent.enable)
            is HeapSortingIntent.PlayButtonState -> {
                copy(playState = intent.playState)
            }
            is HeapSortingIntent.ElementList -> {
                val newList = intent.list.toMutableList()
                copy(elementList = newList)
            }
            is HeapSortingIntent.HeapSortingResultList -> {
                val newList = intent.heapSortingResultList.toMutableList()
                copy(heapSortingResultList = newList)
            }
            is HeapSortingIntent.FinishSorting -> {
                copy(finish = intent.enable)
            }
            is HeapSortingIntent.MoveCount -> {
                copy(moveCount = intent.moveCount)
            }
        }
}