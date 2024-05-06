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
    val playState: PlayState = PlayState.INIT,
    val elementSelected: Int = 0,
    val elementList: MutableList<SortingData> = mutableListOf<SortingData>(),
    val resultList: MutableList<MutableList<SortingDataResult>> = mutableListOf<MutableList<SortingDataResult>>(),
    val finish: Boolean = false,
    val moveCount: Int = 0
)

sealed interface SortingIntent {
    data class StartButtonEnable(val enable: Boolean): SortingIntent
    data class PlayButtonState(val playState: PlayState): SortingIntent
    data class ElementSelected(val index: Int): SortingIntent
    data class ElementList(val list: MutableList<SortingData>): SortingIntent
    data class MoveCount(val moveCount: Int): SortingIntent
    data class FinishSorting(val sortingType: String, val enable: Boolean, val resultList: MutableList<SortingDataResult>): SortingIntent
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
    private val arr = mutableListOf<SortingData>()
    private var arrSize = 0

    private var sortingAlgorithm: ISortingAlgorithm? = null

    private var continuation: Continuation<Unit>? = null
    var curPlayState = PlayState.INIT


    init {
        initValues()
        initArray()
    }

    fun initSorting(sortingType: String) {
        this.type = sortingType

        sortingAlgorithm = getAlogritm(sortingType)
        sortingAlgorithm?.initValue(
            viewModelScope = viewModelScope,
            arr = arr,
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
                        enable = true,
                        resultList = resultList))

                    viewModelScope.launch {
                        var index = 0
                        var size = resultList.size

                        while (index < size) {
                            // 일시 정지 상태에서 대기
                            checkPaused()

                            with(resultList[index]) {
                                displayBars(sortingDataList, swapTargetIdx1, swapTargetIdx2)
                                delay(Const.sortingSpeed.toLong())
                            }
                            index++

                        }
//                        resultList.forEach { sortingDataResult ->
//                            val swapTargetIdx1 = sortingDataResult.swapTargetIdx1
//                            val swapTargetIdx2 = sortingDataResult.swapTargetIdx2
//                            displayBars(sortingDataResult.sortingDataList, swapTargetIdx1, swapTargetIdx2)
//                            delay(Const.sortingSpeed.toLong())
//                        }
                    }
                }
            }
        )
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
            arr.add(
                SortingData(element = randomNum,
                    scaledNum = scaledNumberList[index])
            )
        }

        arrSize = arr.size
        execute(SortingIntent.ElementList(arr))
    }

    private fun initValues() {
        moveCount = 0
        speed = INIT_SPEED
    }

    fun setSortingSpeed(sortingSpeed: Float) {
        sortingAlgorithm?.setSpeed(sortingSpeed)
    }

    fun startButtonEnabled(enable: Boolean) {
        execute(SortingIntent.StartButtonEnable(enable))
    }

    fun setPlayButtonState(playState: PlayState) {
        curPlayState = playState
        execute(SortingIntent.PlayButtonState(playState))
    }

    fun start() {
        viewModelScope.launch {
            sortingAlgorithm?.start()
            setPlayButtonState(PlayState.PLAYING)
        }
    }

    fun restart() {
        moveCount = 0
        execute(SortingIntent.ElementList(arr))
        viewModelScope.launch {
            sortingAlgorithm?.restart()
            setPlayButtonState(PlayState.PLAYING)
        }
    }

    private fun displayBars(
        arr: MutableList<SortingData>,
        swapTargetIdx1: Int,
        swapTargetIdx2: Int)
    {
        for (k in arr.indices) {
            if (k == swapTargetIdx1) {
                arr[k] = SortingData(
                    element = arr[k].element,
                    scaledNum = arr[k].scaledNum,
                    swap1 = true,
                    swap2 = false)
            } else if (k == swapTargetIdx2) {
                arr[k] = SortingData(
                    element = arr[k].element,
                    scaledNum = arr[k].scaledNum,
                    swap1 = false,
                    swap2 = true)
            } else {
                arr[k] = SortingData(
                    element = arr[k].element,
                    scaledNum = arr[k].scaledNum,
                    swap1 = false,
                    swap2 = false)
            }
        }
        moveCount++
        execute(SortingIntent.ElementList(list = arr))
        execute(SortingIntent.MoveCount(moveCount = moveCount))
    }

    override suspend fun SortingUiState.reduce(intent: SortingIntent): SortingUiState =
        when (intent) {
            is SortingIntent.StartButtonEnable -> {
                copy(startButtonEnable = intent.enable)
            }
            is SortingIntent.PlayButtonState -> {
                curPlayState = intent.playState
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