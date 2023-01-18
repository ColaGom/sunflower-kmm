package com.google.samples.apps.sunflower.shared.di

import io.ktor.client.engine.darwin.*
import org.koin.dsl.module

actual val platformModule = module {
    single { Darwin.create { } }
}

actual val API_ACCESS_KEY = ""