package com.codingbot.algorithm


import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.codingbot.algorithm.core.common.AppLifecycleObserver
import com.algorithm.utils.Logger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    private val logger = com.algorithm.utils.Logger("AppLogger")

    private val appLifecycleObserver = AppLifecycleObserver()

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(appLifecycleObserver)
    }
}