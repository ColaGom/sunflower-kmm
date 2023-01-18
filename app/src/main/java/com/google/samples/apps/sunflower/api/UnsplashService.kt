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

package com.google.samples.apps.sunflower.api

import com.google.samples.apps.sunflower.BuildConfig
import com.google.samples.apps.sunflower.data.UnsplashSearchResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

/**
 * Used to connect to the Unsplash API to fetch photos
 */
class UnsplashService(
    private val client: HttpClient
) {
    suspend fun searchPhotos(
        query: String,
        page: Int,
        perPage: Int,
        clientId: String = BuildConfig.UNSPLASH_ACCESS_KEY
    ): UnsplashSearchResponse = client.get("search/photos") {
        url {
            parameters.append("query", query)
            parameters.append("page", page.toString())
            parameters.append("per_page", perPage.toString())
            parameters.append("clientId", clientId)
        }
    }.body()
}