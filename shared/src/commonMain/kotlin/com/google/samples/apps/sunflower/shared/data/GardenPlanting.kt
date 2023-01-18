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

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

/**
 * [GardenPlanting] represents when a user adds a [Plant] to their garden, with useful metadata.
 * Properties such as [lastWateringDate] are used for notifications (such as when to water the
 * plant).
 *
 * Declaring the column info allows for the renaming of variables without implementing a
 * database migration, as the column name would not change.
 */
data class GardenPlanting(
    val gardenPlantingId: Long,
    val plantId: String,
    /**
     * Indicates when the [Plant] was planted. Used for showing notification when it's time
     * to harvest the plant.
     */
    val plantDate: Instant = Clock.System.now(),
    /**
     * Indicates when the [Plant] was last watered. Used for showing notification when it's
     * time to water the plant.
     */
    val lastWateringDate: Instant = Clock.System.now(),
) {
    companion object {
        fun of(
            gardenPlantingId: Long,
            plantId: String,
            plantDate: Long,
            lastWateringDate: Long
        ) = GardenPlanting(
            gardenPlantingId,
            plantId,
            Instant.fromEpochMilliseconds(plantDate),
            Instant.fromEpochMilliseconds(lastWateringDate)
        )
    }
}