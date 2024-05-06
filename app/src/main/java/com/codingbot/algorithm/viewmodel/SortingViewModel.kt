package com.codingbot.algorithm.viewmodel

import androidx.lifecycle.viewModelScope
import com.codingbot.algorithm.core.common.Const
import com.codingbot.algorithm.core.common.Logger
import com.codingbot.algorithm.core.common.SortingList
import com.codingbot.algorithm.core.utils.scaledNumber
import com.codingbot.algorithm.data.SortingData
import com.codingbot.algorithm.data.SortingDataResult
import com.codingbot.algorithm.data.model.sorting.BubbleSortAlgorithm
import com.codingbot.algorithm.data.model.sorting.contract.IDisplaySortingUpdateEvent
import com.codingbot.algorithm.data.model.sorting.ISortingAlgorithm
import com.codingbot.algorithm.data.model.sorting.InsertionSortAlgorithm
import com.codingbot.algorithm.data.model.sorting.QuickSortAlgorithm
import com.codingbot.algorithm.data.model.sorting.SelectionSortAlgorithm
import com.codingbot.algorithm.ui.ChannelUiEvent
import com.codingbot.algorithm.ui.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.random.Random

data class SortingUiState(
    val startButtonEnable: Boolean = true,
    val forwardButtonEnable: Boolean = false,
    val backwardButtonEnable: Boolean = false,
    val playState: PlayState = PlayState.INIT,
    val elementSelected: Int = 0,
    val elementList: MutableList<SortingData> = mutableListOf<SortingData>(),
    val resultList: MutableList<MutableList<SortingDataResult>> = mutableListOf<MutableList<SortingDataResult>>(),
    val finish: Boolean = false,
    val moveCount: Int = 0
)

sealed interface SortingIntent {
    data class ButtonEnableForwardAndBackward(val forwardButtonEnable: Boolean, val backwardButtonEnable: Boolean): SortingIntent
    data class PlayButtonState(val playState: PlayState): SortingIntent
    data class ElementSelected(val index: Int): SortingIntent
    data class ElementList(val list: MutableList<SortingData>): SortingIntent
    data class MoveCount(val moveCount: Int): SortingIntent
    data class FinishSorting(val sortingType: String, val enable: Boolean): SortingIntent
}

sealed interface SortingUiEvent {

}

enum class PlayState {
    INIT, RESUME, PLAYING, PAUSE, BACKWARD, FORWARD
}
@HiltViewModel
class SortingViewModel @Inject constructor()
    : BaseViewModel<SortingUiState, SortingIntent>(SortingUiState()),
    UiEvent<SortingUiEvent> by ChannelUiEvent()
{
    val logger = Logger("SortingViewModel")
    private val ELEMENT_RANDOM_FROM = -20
    private val ELEMENT_RANDOM_TO = 21
    private val INIT_SPEED = 500f
    private var speed = INIT_SPEED
    private var moveCount = 0
    private var type: String = SortingList.BUBBLE_SORT.name
    private val sortingListInit = mutableListOf<SortingData>()
    private var sortingListInitSize = 0
    private lateinit var sortingResultList: MutableList<SortingDataResult>

    private var sortingAlgorithm: ISortingAlgorithm? = null

    private var continuation: Continuation<Unit>? = null
    var curPlayState = PlayState.INIT
    private var sortingProgressIndex = 0

    init {
        initValues()
        initArray()
    }

    fun initSorting(sortingType: String) {
        this.type = sortingType

        sortingAlgorithm = getAlogritm(sortingType)
        sortingAlgorithm?.initValue(
            viewModelScope = viewModelScope,
            sortingListInit = sortingListInit,
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
                        sortingType = sortingType,
                        enable = true))

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
                displayBars(sortingDataList, swapTargetIdx1, swapTargetIdx2)
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

    fun pauseSorting() {
//        playState = PlayState.PAUSE
        setPlayButtonState(PlayState.PAUSE)

    }

    fun resumeSorting() {
//        playState = PlayState.RESUME
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
//        var forwardButtonEnable = true
//        var backwardButtonEnable = true
//        if (curPlayState != PlayState.PAUSE) {
//            forwardButtonEnable = false
//            backwardButtonEnable = false
//        }
        execute(SortingIntent.ButtonEnableForwardAndBackward(forwardButtonEnable, backwardButtonEnable))
//        buttonEnableForwardAndBackward(
//            forwardButtonEnable = forwardButtonEnable,
//            backwardButtonEnable = backwardButtonEnable
//        )
    }


//    fun buttonEnableForwardAndBackward(forwardButtonEnable: Boolean, backwardButtonEnable: Boolean) {
//        execute(SortingIntent.ButtonEnableForwardAndBackward(forwardButtonEnable, backwardButtonEnable))
//    }

    private fun getAlogritm(sortingType: String): ISortingAlgorithm =
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

    private fun initArray() {

        val randomValues = Array(Const.ARRAYS_SIZE) { Random.nextInt(ELEMENT_RANDOM_FROM, ELEMENT_RANDOM_TO) }
        val scaledNumberList = scaledNumber(
            randomValues = randomValues,
            from = Const.GRAPH_HEIGHT_FROM,
            to = Const.GRAPH_HEIGHT_TO
            )
        randomValues.forEachIndexed { index, randomNum ->
            sortingListInit.add(
                SortingData(element = randomNum,
                    scaledNum = scaledNumberList[index])
            )
        }

        sortingListInitSize = sortingListInit.size
        execute(SortingIntent.ElementList(sortingListInit))
    }

    private fun initValues() {
        moveCount = 0
        speed = INIT_SPEED
    }

    fun setSortingSpeed(sortingSpeed: Float) {
        speed = sortingSpeed
    }

    fun setPlayButtonState(playState: PlayState) {
        curPlayState = playState
        execute(SortingIntent.PlayButtonState(playState))
        logger { "setPlayButtonState:$playState" }
        decideForwardBackwardEnable()
    }

    private fun decideForwardBackwardEnable() {
        if (curPlayState == PlayState.PAUSE) {
            if (sortingProgressIndex >= sortingResultList.size -1) {
                forwardBackwardEnable(
                    forwardButtonEnable = false,
                    backwardButtonEnable = true,
                )
            } else if (sortingProgressIndex <= 0) {
                forwardBackwardEnable(
                    forwardButtonEnable = true,
                    backwardButtonEnable = false,
                )
            } else {
                forwardBackwardEnable(
                    forwardButtonEnable = true,
                    backwardButtonEnable = true,
                )
            }
        } else {
            forwardBackwardEnable(
                forwardButtonEnable = false,
                backwardButtonEnable = false,
            )
        }
    }

    fun start() {
        viewModelScope.launch {
            sortingAlgorithm?.start()
            setPlayButtonState(PlayState.PLAYING)
        }
    }

    fun restart() {
        moveCount = 0
        execute(SortingIntent.ElementList(sortingListInit))
        viewModelScope.launch {
            sortingAlgorithm?.restart()
            setPlayButtonState(PlayState.PLAYING)
        }
    }

    private fun displayBars(
        sortingList: MutableList<SortingData>,
        swapTargetIdx1: Int,
        swapTargetIdx2: Int)
    {
        for (k in sortingList.indices) {
            if (k == swapTargetIdx1) {
                sortingList[k] = SortingData(
                    element = sortingList[k].element,
                    scaledNum = sortingList[k].scaledNum,
                    swap1 = true,
                    swap2 = false)
            } else if (k == swapTargetIdx2) {
                sortingList[k] = SortingData(
                    element = sortingList[k].element,
                    scaledNum = sortingList[k].scaledNum,
                    swap1 = false,
                    swap2 = true)
            } else {
                sortingList[k] = SortingData(
                    element = sortingList[k].element,
                    scaledNum = sortingList[k].scaledNum,
                    swap1 = false,
                    swap2 = false)
            }
        }
        moveCount++
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
//                curPlayState = intent.playState
                copy(playState = intent.playState)
            }
            is SortingIntent.ElementSelected -> {
                copy(elementSelected = intent.index)
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