package com.google.samples.apps.sunflower.shared

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun interface Closeable {
    fun close()
}

fun <T> Flow<T>.watch(block: (T) -> Unit): Closeable {
    val scope = MainScope()
    onEach(block).launchIn(scope)
    return Closeable {
        scope.cancel()
    }
}