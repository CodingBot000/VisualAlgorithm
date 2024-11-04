package com.algorithm.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.algorithm.domain.repository.racecondition.RaceConditionRepository
import com.algorithm.model.AsynchronousDataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AsynchronousUiState(
    val reTryCnt: Int = 0,
    val dataResult: AsynchronousDataResult? = null
)

sealed interface AsynchronousIntent {
    data class ReTryCnt(val reTryCnt: Int): AsynchronousIntent
    data class ElementList(val dataResult: AsynchronousDataResult): AsynchronousIntent
}


@HiltViewModel
class AsynchronousViewModel @Inject constructor(
    private val raceConditionRepository: RaceConditionRepository
)
    : BaseViewModel<AsynchronousUiState, AsynchronousIntent>(AsynchronousUiState())
{
    val logger = com.algorithm.utils.Logger("AsynchronousViewModel")

    init {
        initArray()
    }

    fun initArray() {
        viewModelScope.launch {
            raceConditionRepository.getTryCnt().collect { tryCnt ->
                execute(AsynchronousIntent.ReTryCnt(tryCnt))
            }
        }

        viewModelScope.launch {
            val result = raceConditionRepository.getRaceConditionRepository()
//                for (i in 0 until result.size) {
//                    val newList = result.subList(0, i + 1).toList()
//                    resultList.add(newList)
//                }
            execute(AsynchronousIntent.ElementList(result))
        }
    }

    override suspend fun AsynchronousUiState.reduce(intent: AsynchronousIntent): AsynchronousUiState =
        when (intent) {
            is AsynchronousIntent.ReTryCnt -> {
                val newTryCnt = intent.reTryCnt
                copy(reTryCnt = newTryCnt)
            }
            is AsynchronousIntent.ElementList -> {
                val newList = intent.dataResult
                copy(dataResult = newList)
            }
        }
}