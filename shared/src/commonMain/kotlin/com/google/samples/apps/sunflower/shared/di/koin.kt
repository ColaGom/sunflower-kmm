/*
 * Copyright 2023 Google LLC
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

package com.google.samples.apps.sunflower.shared.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

expect val platformModule: Module
expect val API_ACCESS_KEY: String

fun initKoin(
    vararg appModule: Module,
    appDeclaration: KoinAppDeclaration
): KoinApplication = startKoin {
    appDeclaration(this)
    modules(
        *appModule,
        platformModule,
        databaseModule,
        networkModule,
        repositoryModule,
        storeModule
    )
}