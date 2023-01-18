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

package com.google.samples.apps.sunflower.shared.data

import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.days

@kotlinx.serialization.Serializable
data class Plant(
    val plantId: String,
    val name: String,
    val description: String,
    val growZoneNumber: Int,
    val wateringInterval: Int = 7, // how often the plant should be watered, in days
    val imageUrl: String = ""
) {

    /**
     * Determines if the plant should be watered.  Returns true if [since]'s date > date of last
     * watering + watering Interval; false otherwise.
     */
    fun shouldBeWatered(since: Instant, lastWateringDate: Instant) =
        since > lastWateringDate + wateringInterval.days

    override fun toString() = name

    companion object {
        fun of(
            plantId: String,
            name: String,
            description: String,
            growZoneNumber: Int,
            wateringInterval: Int,
            imageUrl: String
        ) = Plant(plantId, name, description, growZoneNumber, wateringInterval, imageUrl)
    }
}
