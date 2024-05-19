package com.codingbot.algorithm.domain.algorithm.graph.contract

import com.codingbot.algorithm.domain.model.TrackingDataResult


interface IDisplayGraphUpdateEvent {
    fun finish(resultVisitedArray: MutableList<TrackingDataResult>)
}