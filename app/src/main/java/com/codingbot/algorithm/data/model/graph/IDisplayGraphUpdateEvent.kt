package com.codingbot.algorithm.data.model.graph


interface IDisplayGraphUpdateEvent {
    fun visitedList(list: Array<BooleanArray>)
    fun finish()
}