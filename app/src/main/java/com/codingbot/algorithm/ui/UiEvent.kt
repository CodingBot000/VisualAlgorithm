package com.codingbot.algorithm.ui

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.yield

/**
 *  뷰모델이 뷰(Ui)를 직접적으로 참조하기 않기 위한 방법으로서
 *  뷰모델이 뷰(Ui)에 명령할 어떤 동작등을 Event 라 칭하고 이를 전달 및 수신하는 방법을 제공
 */
interface UiEvent<Event> {

    /**
     *  뷰 레이어에서 Event 수신을 위한 방법. 주로 뷰에서 참조해서 사용
     */
    val uiEvent: Flow<Event>

    /**
     * Event 를 뷰 레이어에 전달 하기 위한 방법
     */
    suspend fun postEvent(event: Event)
}

/**
 * [Channel] 을 이용한 UiEvent.
 *  Channel 특징: 각 이벤트 전달시 수신자가 돌아가면서 한번씩 수신
 */
class ChannelUiEvent<Event> : UiEvent<Event> {
    private val channel = Channel<Event>()

    override val uiEvent: Flow<Event> = channel.receiveAsFlow()

    override suspend fun postEvent(event: Event) = channel.send(event)
}

/**
 * [SharedFlow] 를 이용한 UiEvent.
 *  SharedFlow 특징: 각 이벤트 전달시 모든 수신자가 동일한 이벤트 수신
 */
class SharedUiEvent<Event> : UiEvent<Event> {
    private val sharedFlow = MutableSharedFlow<Event>()

    override val uiEvent: Flow<Event> = sharedFlow

    override suspend fun postEvent(event: Event) {
        yield()
        sharedFlow.emit(event)
    }
}