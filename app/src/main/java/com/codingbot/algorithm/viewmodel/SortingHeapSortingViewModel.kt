package com.codingbot.algorithm.viewmodel

import androidx.lifecycle.viewModelScope
import com.codingbot.algorithm.core.common.Const
import com.codingbot.algorithm.core.common.Logger
import com.codingbot.algorithm.core.common.SortingList
import com.codingbot.algorithm.core.utils.scaledNumber
import com.codingbot.algorithm.data.SortingData
import com.codingbot.algorithm.data.SortingDataResult
import com.codingbot.algorithm.data.SortingHeapDataResult
import com.codingbot.algorithm.data.model.sorting.HeapSortAlgorithm
import com.codingbot.algorithm.data.model.sorting.contract.IDisplayHeapSortingUpdateEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.Continuation
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
//    data class HeapSortingResultList(val heapSortingResultList: MutableList<SortingHeapDataResult>): HeapSortingIntent
    data class MoveCount(val moveCount: Int): HeapSortingIntent
    data class FinishSorting(val sortingType: String, val enable: Boolean): HeapSortingIntent
}

@HiltViewModel
class SortingHeapSortingViewModel @Inject constructor()
    : BaseViewModel<HeapSortingUiState, HeapSortingIntent>(HeapSortingUiState())
{
    val logger = Logger("SortingHeapSortingViewModel")
    private val ELEMENT_RANDOM_FROM = -20
    private val ELEMENT_RANDOM_TO = 21
    private val INIT_SPEED = 500f
    private var speed = INIT_SPEED
    private var moveCount = 0
    private var type: String = SortingList.BUBBLE_SORT.name
    private val originArr = mutableListOf<SortingData>()
    private var heapSortingResultList = mutableListOf<SortingData>()
    private var arrSize = 0
    private var sortingListInitSize = 0
    private var sortingResultList: MutableList<SortingHeapDataResult> = mutableListOf()

    private var sortingAlgorithm: HeapSortAlgorithm? = null

    private var continuation: Continuation<Unit>? = null
    var curPlayState = PlayState.INIT
    private var sortingProgressIndex = 0

    init {
        initValues()
        initArray()
    }

    fun initSorting(sortingType: String) {
        this.type = sortingType

        sortingAlgorithm = getAlogrithm()
        sortingAlgorithm?.initValue(
            viewModelScope = viewModelScope,
            sortingListInit = originArr,
            iDisplayHeapSortingUpdateEvent = object: IDisplayHeapSortingUpdateEvent {
                override fun elementList(
                    list: MutableList<SortingData>,
                    result: MutableList<SortingData>,
                    swapTargetIdx1: Int,
                    swapTargetIdx2: Int
                ) {
                    heapSortingResultList = result
                    displayBars(list, result, swapTargetIdx1, swapTargetIdx2)
                }

                override fun finish(resultList: MutableList<SortingHeapDataResult>) {
                    execute(HeapSortingIntent.FinishSorting(sortingType, true))

                    sortingResultList = resultList

                    viewModelScope.launch {
                        while (sortingProgressIndex < sortingResultList.size) {
                            checkPaused()
                            updateBars()
                            decideForwardBackwardEnable()
                            sortingProgressIndex++
                        }
                    }
                }
            }
        )
    }

    private suspend fun updateBars() {
        try {
            with(sortingResultList[sortingProgressIndex]) {
                displayBars(sortingDataList, resultList, swapTargetIdx1, swapTargetIdx2)
                delay(speed.toLong())
            }
        } catch (e: IndexOutOfBoundsException) {
            logger { "updateBars sortingIndexException: $e" }
        }
    }
    private fun getAlogrithm() =
        HeapSortAlgorithm()

    private fun initArray() {

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

        arrSize = originArr.size
        var resultsEmpty = MutableList(originArr.count()) { SortingData() }
        execute(HeapSortingIntent.HeapSortingResultList(heapSortingResultList = resultsEmpty))
        execute(HeapSortingIntent.ElementList(originArr))
    }

    private fun initValues() {
        moveCount = 0
        speed = INIT_SPEED
    }

    fun setSortingSpeed(sortingSpeed: Float) {
        sortingAlgorithm?.setSpeed(sortingSpeed)
    }

    fun setPlayButtonState(playState: PlayState) {
        curPlayState = playState
        execute(HeapSortingIntent.PlayButtonState(playState))
        logger { "setPlayButtonState:$playState" }
        decideForwardBackwardEnable()
    }

    private fun decideForwardBackwardEnable() {
        val (forward, backward) = if (curPlayState == PlayState.PAUSE) {
            if (sortingProgressIndex >= sortingResultList.size -1) {
                Pair(false, true)
            } else if (sortingProgressIndex <= 0) {
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

    private suspend fun checkPaused() {
        if (curPlayState == PlayState.PAUSE) {
            suspendCancellableCoroutine<Unit> { continuation = it }
        }
    }

    fun pauseSorting() {
        setPlayButtonState(PlayState.PAUSE)

    }

    fun resumeSorting() {
        setPlayButtonState(PlayState.RESUME)
        continuation?.resume(Unit)
        continuation = null
    }

    fun forward() {
        viewModelScope.launch {
            if (sortingResultList.size -1 > sortingProgressIndex) {
                sortingProgressIndex++
                logger { "forward sortingProgressIndex 1:$sortingProgressIndex" }
                updateBars()
            } else {
                logger { "forward sortingProgressIndex over:$sortingProgressIndex" }
                forwardBackwardEnable(
                    forwardButtonEnable = false,
                    backwardButtonEnable = true,
                )
            }
            decideForwardBackwardEnable()
        }
    }

    fun backward() {
        viewModelScope.launch {
            if (0 < sortingProgressIndex) {
                sortingProgressIndex--
                logger { "backward sortingProgressIndex over:$sortingProgressIndex" }
                updateBars()
            } else {
                logger { "backward sortingProgressIndex over:$sortingProgressIndex" }
                forwardBackwardEnable(
                    forwardButtonEnable = true,
                    backwardButtonEnable = false,
                )
            }
            decideForwardBackwardEnable()
        }
    }

    private fun forwardBackwardEnable(
        forwardButtonEnable: Boolean = true,
        backwardButtonEnable: Boolean = true
    ) {
        execute(HeapSortingIntent.ButtonEnableForwardAndBackward(forwardButtonEnable, backwardButtonEnable))
    }

    fun startButtonEnabled(enable: Boolean) {
        execute(HeapSortingIntent.StartButtonEnable(enable))
    }

    fun start() {
        viewModelScope.launch {
            sortingAlgorithm?.start()
            setPlayButtonState(PlayState.PLAYING)
        }
    }

    fun restart() {
        moveCount = 0
        execute(HeapSortingIntent.ElementList(originArr))
        viewModelScope.launch {
            sortingAlgorithm?.restart()
            setPlayButtonState(PlayState.PLAYING)
        }
    }

    private fun displayBars(
        sortingList: MutableList<SortingData>,
        resultList: MutableList<SortingData>,
        swapTargetIdx1: Int,
        swapTargetIdx2: Int)
    {
        for (k in sortingList.indices) {
            sortingList[k] =
                if (k == swapTargetIdx1) {
                    SortingData(
                        element = sortingList[k].element,
                        scaledNum = sortingList[k].scaledNum,
                        swap1 = true,
                        swap2 = false
                    )
                } else if (k == swapTargetIdx2) {
                    SortingData(
                        element = sortingList[k].element,
                        scaledNum = sortingList[k].scaledNum,
                        swap1 = false,
                        swap2 = true
                    )
                } else {
                    SortingData(
                        element = sortingList[k].element,
                        scaledNum = sortingList[k].scaledNum,
                        swap1 = false,
                        swap2 = false
                    )
                }
        }

        moveCount++

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