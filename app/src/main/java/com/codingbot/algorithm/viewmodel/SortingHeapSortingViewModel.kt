package com.codingbot.algorithm.viewmodel

import androidx.lifecycle.viewModelScope
import com.codingbot.algorithm.core.common.Const
import com.codingbot.algorithm.core.common.Logger
import com.codingbot.algorithm.core.common.SortingList
import com.codingbot.algorithm.core.utils.scaledNumber
import com.codingbot.algorithm.data.SortingData
import com.codingbot.algorithm.data.SortingDataResult
import com.codingbot.algorithm.data.model.sorting.HeapSortAlgorithm
import com.codingbot.algorithm.data.model.sorting.contract.IDisplayHeapSortingUpdateEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

data class HeapSortingUiState(
    val startButtonEnable: Boolean = true,
    val elementList: MutableList<SortingData> = mutableListOf<SortingData>(),
    val heapSortingResultList: MutableList<SortingData> = mutableListOf<SortingData>(),
    val finish: Boolean = false,
    val moveCount: Int = 0
)

sealed interface HeapSortingIntent {
    data class StartButtonEnable(val enable: Boolean): HeapSortingIntent
    data class ElementList(val list: MutableList<SortingData>): HeapSortingIntent
    data class HeapSortingResultList(val heapSortingResultList: MutableList<SortingData>): HeapSortingIntent
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
    private var arrSize = 0

    private var sortingAlgorithm: HeapSortAlgorithm? = null


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
                    result: MutableList<SortingData>?,
                    swapTargetIdx1: Int,
                    swapTargetIdx2: Int
                ) {
                    displayBars(list, result, swapTargetIdx1, swapTargetIdx2)
                }

                override fun finish(resultList: MutableList<SortingDataResult>?) {
                    execute(HeapSortingIntent.FinishSorting(sortingType, true))
                }
            }
        )
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

    fun startButtonEnabled(enable: Boolean) {
        execute(HeapSortingIntent.StartButtonEnable(enable))
    }

    fun start() {
        viewModelScope.launch {
            sortingAlgorithm?.start()
        }
    }

    fun restart() {
        moveCount = 0
        execute(HeapSortingIntent.ElementList(originArr))
        viewModelScope.launch {
            sortingAlgorithm?.restart()
        }
    }

    private fun displayBars(
        sortingList: MutableList<SortingData>,
        resultList: MutableList<SortingData>?,
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
        if (resultList != null) {
            execute(HeapSortingIntent.HeapSortingResultList(heapSortingResultList = resultList))
        }
        execute(HeapSortingIntent.ElementList(list = sortingList))
        execute(HeapSortingIntent.MoveCount(moveCount = moveCount))
    }

    override suspend fun HeapSortingUiState.reduce(intent: HeapSortingIntent): HeapSortingUiState =
        when (intent) {
            is HeapSortingIntent.StartButtonEnable -> copy(startButtonEnable = intent.enable)
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