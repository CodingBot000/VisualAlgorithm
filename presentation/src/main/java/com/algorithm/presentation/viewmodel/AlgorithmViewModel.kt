package com.algorithm.presentation.viewmodel

import com.algorithm.common.PlayState
import kotlinx.coroutines.flow.SharingStarted
import kotlin.coroutines.Continuation


abstract class AlgorithmViewModel<STATE, INTENT>(
    initialState: STATE,
    sharingStarted: SharingStarted = SharingStarted.Lazily
) : BaseViewModel<STATE, INTENT>(initialState, sharingStarted) {
    val ELEMENT_RANDOM_FROM = -20
    val ELEMENT_RANDOM_TO = 21
    val INIT_SPEED = 500f
    private var _speed = INIT_SPEED
    open var speed: Float
        get() = _speed
        protected set(value) {
            _speed = value
        }

    open var _moveCount = 0
    open var moveCount: Int
        get() = _moveCount
        protected set(value) {
            _moveCount = value
        }

    open var _progressIndex = 0
    open var progressIndex: Int
        get() = _progressIndex
        protected set(value) {
            _progressIndex = value
        }

    open var _continuation: Continuation<Unit>? = null
     open var continuation: Continuation<Unit>?
        get() = _continuation
        protected set(value) {
            _continuation = value
        }

    open var _curPlayState = PlayState.INIT
    open var curPlayState: PlayState
        get() = _curPlayState
        protected set(value) {
            _curPlayState = value
        }
    var _type: String = ""
    open var type: String
        get() = _type
        protected set(value) {
            _type = value
        }

    protected abstract fun initArray()
    abstract fun initValue(type: String)
    protected abstract fun runAlgorithmProcess()
    protected abstract suspend fun updateBars()
    protected abstract suspend fun checkPaused()
    abstract fun start()
    abstract fun restart()
    abstract fun pause()
    abstract fun resume()
    abstract fun forward()
    abstract fun backward()
    protected abstract fun setPlayButtonState(playState: PlayState)
    protected abstract fun decideForwardBackwardEnable()
    protected abstract fun forwardBackwardEnable(
        forwardButtonEnable: Boolean = true,
        backwardButtonEnable: Boolean = true
    )

}
