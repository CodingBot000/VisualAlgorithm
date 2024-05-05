package com.codingbot.algorithm


import android.app.Application
import com.codingbot.algorithm.core.common.AppLifecycleObserver
import com.codingbot.algorithm.core.common.Logger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    companion object {
        private var IS_CREATED = false
    }

    private val logger = Logger("AppLogger")

    private val appLifecycleObserver = AppLifecycleObserver()

    override fun onCreate() {
        super.onCreate()
//        ProcessLifecycleOwner.get().lifecycle.addObserver(appLifecycleObserver)
    }
}