package com.codingbot.algorithm.data.model.graph.contract

import com.codingbot.algorithm.data.TrackingDataResult


interface IDisplayGraphUpdateEvent {
    fun finish(resultVisitedArray: MutableList<TrackingDataResult>)
}