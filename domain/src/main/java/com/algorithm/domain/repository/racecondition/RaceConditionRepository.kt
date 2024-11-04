package com.algorithm.domain.repository.racecondition

import com.algorithm.model.AsynchronousDataResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface RaceConditionRepository {
    suspend fun getRaceConditionRepository(): AsynchronousDataResult
    fun getTryCnt(): StateFlow<Int>
}