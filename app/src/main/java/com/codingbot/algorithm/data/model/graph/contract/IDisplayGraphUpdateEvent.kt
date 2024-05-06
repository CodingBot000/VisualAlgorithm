package com.codingbot.algorithm.data.model.graph.contract


interface IDisplayGraphUpdateEvent {
    fun visitedList(list: Array<BooleanArray>)
    fun finish()
}