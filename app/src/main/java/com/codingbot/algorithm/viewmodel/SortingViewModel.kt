package com.codingbot.algorithm.viewmodel

import androidx.lifecycle.viewModelScope
import com.codingbot.algorithm.core.common.Const
import com.codingbot.algorithm.core.common.Logger
import com.codingbot.algorithm.core.common.SortingList
import com.codingbot.algorithm.core.utils.scaledNumber
import com.codingbot.algorithm.domain.model.SortingData
import com.codingbot.algorithm.domain.model.SortingDataResult
import com.codingbot.algorithm.domain.algorithm.sorting.BubbleSortAlgorithm
import com.codingbot.algorithm.domain.algorithm.sorting.InsertionSortAlgorithm
import com.codingbot.algorithm.domain.algorithm.sorting.QuickSortAlgorithm
import com.codingbot.algorithm.domain.algorithm.sorting.SelectionSortAlgorithm
import com.codingbot.algorithm.domain.algorithm.sorting.contract.IDisplaySortingUpdateEvent
import com.codingbot.algorithm.domain.algorithm.sorting.contract.ISortingAlgorithm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.random.Random

data class SortingUiState(
    val startButtonEnable: Boolean = true,
    val forwardButtonEnable: Boolean = false,
    val backwardButtonEnable: Boolean = false,
    val playState: PlayState = PlayState.INIT,
    val elementList: MutableList<SortingData> = mutableListOf<SortingData>(),
    val resultList: MutableList<MutableList<SortingDataResult>> = mutableListOf<MutableList<SortingDataResult>>(),
    val finish: Boolean = false,
    val moveCount: Int = 0
)

sealed interface SortingIntent {
    data class ButtonEnableForwardAndBackward(val forwardButtonEnable: Boolean, val backwardButtonEnable: Boolean): SortingIntent
    data class PlayButtonState(val playState: PlayState): SortingIntent
    data class ElementList(val list: MutableList<SortingData>): SortingIntent
    data class MoveCount(val moveCount: Int): SortingIntent
    data class FinishSorting(val sortingType: String, val enable: Boolean): SortingIntent
}

enum class PlayState {
    INIT, RESUME, PLAYING, PAUSE, BACKWARD, FORWARD
}
@HiltViewModel
class SortingViewModel @Inject constructor()
    : AlgorithmViewModel<SortingUiState, SortingIntent>(SortingUiState())
{
    val logger = Logger("SortingViewModel")

    private val originArr = mutableListOf<SortingData>()
    private var resultHistoryList: MutableList<SortingDataResult> = mutableListOf()
    private var algorithm: ISortingAlgorithm? = null

    init {
        initArray()
    }

    fun getHistoryList(): StringBuilder {
        val sb = StringBuilder()
        if (resultHistoryList.isNotEmpty()) {
            for (i in 0..progressIndex) {
                sb.append(makeLogHistory(i, resultHistoryList[i]) + "\n")
            }
        }
        return sb
    }

    private fun makeLogHistory(index: Int, data: SortingDataResult): String =
        "step:$index  [swaping] target 1:(index:${data.swapTargetIdx1} - element:${data.sortingDataList[data.swapTargetIdx1].element})  <-->  target 2: (index:${data.swapTargetIdx2} - element:${data.sortingDataList[data.swapTargetIdx2].element})"

    private fun getAlgorithm(sortingType: String): ISortingAlgorithm =
        when (sortingType) {
            SortingList.BUBBLE_SORT.name -> {
                BubbleSortAlgorithm()
            }
            SortingList.SELECTION_SORT.name -> {
                SelectionSortAlgorithm()
            }
            SortingList.INSERTION_SORT.name -> {
                InsertionSortAlgorithm()
            }
            SortingList.QUICK_SORT.name -> {
                QuickSortAlgorithm()
            }
            else -> {
                BubbleSortAlgorithm()
            }
        }

    fun setSpeedValue(speed: Float) {
        this.speed = speed
    }

    override fun initArray() {
        moveCount = 0
        progressIndex = 0
        speed = INIT_SPEED

        val randomValues = Array(Const.ARRAYS_SIZE) { Random.nextInt(ELEMENT_RANDOM_FROM, ELEMENT_RANDOM_TO) }
        val scaledNumberList = scaledNumber(
            randomValues = randomValues,
            from = Const.GRAPH_HEIGHT_FROM,
            to = Const.GRAPH_HEIGHT_TO
        )
        randomValues.forEachIndexed { index, randomNum ->
            originArr.add(
                SortingData(element = randomNum,
                    scaledNum = scaledNumberList[index])
            )
        }

        execute(SortingIntent.ElementList(originArr))
    }

    override fun initValue(type: String) {
        this.type = type

        algorithm = getAlgorithm(type)
        algorithm?.initValue(
            viewModelScope = viewModelScope,
            sortingListInit = originArr,
            iDisplaySortingUpdateEvent = object: IDisplaySortingUpdateEvent {
                override fun elementList(
                    list: MutableList<SortingData>,
                    swapTargetIdx1: Int,
                    swapTargetIdx2: Int
                ) {
                    displayBars(list, swapTargetIdx1, swapTargetIdx2)
                }

                override fun finish(resultList: MutableList<SortingDataResult>) {

                    execute(SortingIntent.FinishSorting(
                        sortingType = type,
                        enable = true))

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
                checkPaused() // When reaching the end, check once more and switch to pause mode if it is the end.
                decideForwardBackwardEnable()
            }
        }
    }
    override suspend fun updateBars() {
        try {
            with(resultHistoryList[progressIndex]) {
                displayBars(sortingDataList, swapTargetIdx1, swapTargetIdx2)
                delay(speed.toLong())
            }
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

    private fun isLoadingEnded() {
        if (progressIndex >= resultHistoryList.size -1) {
            curPlayState = PlayState.PAUSE
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
        execute(SortingIntent.ElementList(originArr))
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
        execute(SortingIntent.PlayButtonState(playState))
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
        execute(SortingIntent.ButtonEnableForwardAndBackward(forwardButtonEnable, backwardButtonEnable))
    }

    private fun displayBars(
        sortingList: MutableList<SortingData>,
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
                        swap2 = false)
                } else if (i == swapTargetIdx2) {
                    SortingData(
                        element = sortingList[i].element,
                        scaledNum = sortingList[i].scaledNum,
                        swap1 = false,
                        swap2 = true)
                } else {
                    SortingData(
                        element = sortingList[i].element,
                        scaledNum = sortingList[i].scaledNum,
                        swap1 = false,
                        swap2 = false)
                }
        }
        moveCount = progressIndex
        execute(SortingIntent.ElementList(list = sortingList))
        execute(SortingIntent.MoveCount(moveCount = moveCount))
    }

    override suspend fun SortingUiState.reduce(intent: SortingIntent): SortingUiState =
        when (intent) {
            is SortingIntent.ButtonEnableForwardAndBackward -> {
                copy(forwardButtonEnable = intent.forwardButtonEnable,
                    backwardButtonEnable = intent.backwardButtonEnable)
            }
            is SortingIntent.PlayButtonState -> {
                copy(playState = intent.playState)
            }
            is SortingIntent.ElementList -> {
                val newList = intent.list.toMutableList()
                copy(elementList = newList)
            }
            is SortingIntent.FinishSorting -> {
                copy(finish = intent.enable)
            }
            is SortingIntent.MoveCount -> {
                copy(moveCount = intent.moveCount)
            }
        }
}