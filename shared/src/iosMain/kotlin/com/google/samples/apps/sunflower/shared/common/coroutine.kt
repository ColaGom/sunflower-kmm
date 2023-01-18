package com.google.samples.apps.sunflower.shared.common

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext


actual val ioDispatcher: CoroutineContext = Dispatchers.Default