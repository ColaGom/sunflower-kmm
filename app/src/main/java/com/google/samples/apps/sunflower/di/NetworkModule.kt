/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.sunflower.di

import com.google.samples.apps.sunflower.api.UnsplashService
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


private const val BASE_URL = "https://api.unsplash.com/"

val networkModule = module {
    single {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json()
            }

            defaultRequest {
                url(BASE_URL)
            }
        }
    }
    singleOf(::UnsplashService)
}