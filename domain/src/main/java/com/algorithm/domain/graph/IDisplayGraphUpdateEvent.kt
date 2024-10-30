package com.algorithm.domain.graph

import com.algorithm.model.TrackingDataResult


interface IDisplayGraphUpdateEvent {
    fun finish(resultVisitedArray: MutableList<TrackingDataResult>)
}