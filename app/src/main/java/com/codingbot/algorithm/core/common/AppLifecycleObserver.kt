package com.codingbot.algorithm.core.common

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppLifecycleObserver: LifecycleEventObserver {
    private val logger = Logger("AppLifecycleObserverLogger")

    private val _isInBackground: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isInBackground = _isInBackground.asStateFlow()

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_START -> _isInBackground.update { false }
            Lifecycle.Event.ON_STOP -> _isInBackground.update { true }
            else -> {}
        }
    }

}