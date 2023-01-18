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

import com.google.samples.apps.sunflower.shared.common.ioDispatcher
import kotlinx.coroutines.withContext

class GardenPlantingRepository(
    private val gardenPlantingDao: GardenPlantingDao
) {
    suspend fun createGardenPlanting(plantId: String) = withContext(ioDispatcher) {
        gardenPlantingDao.insertGardenPlanting(plantId)
    }

    suspend fun removeGardenPlanting(gardenPlanting: GardenPlanting) = withContext(ioDispatcher) {
        gardenPlantingDao.deleteGardenPlanting(gardenPlanting)
    }

    fun isPlanted(plantId: String) =
        gardenPlantingDao.isPlanted(plantId)

    fun getPlantedGardens() = gardenPlantingDao.getPlantedGardens()
}
