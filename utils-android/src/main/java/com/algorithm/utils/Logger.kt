package com.algorithm.utils

import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.function.Supplier

class Logger(baseTag: String, logInit: Boolean = false, logInitPrefix: String = "") {

    private val tag: String = "${prefix}_$baseTag"

    init {
        if (logInit) {
            log { "$logInitPrefix$baseTag" }
        }
    }

    fun log(throwable: Throwable? = null, msg: Supplier<String>) {
        val logMsg = msg.get()
        callback?.invoke("${DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now())} ${
            DateTimeFormatter.ISO_LOCAL_TIME.format(
                LocalDateTime.now())} $tag\t$logMsg")
        
        Timber.tag(tag).d(t = throwable, message = logMsg)
    }

    operator fun invoke(throwable: Throwable? = null, msg: Supplier<String>) = log(throwable, msg)

    companion object {
        private const val prefix = "VisualAlgorithm"
        private var callback: ((String) -> Unit)? = null

        fun setCallback(callback: (String) -> Unit) {
            Companion.callback = callback
        }

        init {
            Timber.plant(Timber.DebugTree())
        }
    }

}