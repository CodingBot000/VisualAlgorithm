package com.codingbot.algorithm.data.model.graph.contract


interface IDisplayGraphUpdateEvent {
    fun visitedList(visitedList: Array<BooleanArray>)
    fun finish()
}