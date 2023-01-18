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

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

/**
 * The Data Access Object for the [GardenPlanting] class.
 */
//@Dao
class GardenPlantingDao(
    private val queries: GardenPlantingsQueries
) {
    fun getGardenPlantings(): Flow<List<GardenPlanting>> =
        queries.findAll(GardenPlanting.Companion::of).asFlow().mapToList()

    fun isPlanted(plantId: String): Flow<Boolean> =
        queries.findByPlantId(plantId).asFlow().mapToOneOrNull().map { it != null }

    fun getPlantedGardens(): Flow<List<PlantAndGardenPlantings>> =
        queries.findAllWithPlant().asFlow().mapToList().map {
            it.groupBy { it.plant_id }
                .map { (_, values) ->
                    val first = values.first()
                    val plant = Plant.of(
                        first.plant_id,
                        first.name,
                        first.description,
                        first.grow_zone_number,
                        first.watering_interval,
                        first.image_url
                    )

                    PlantAndGardenPlantings(
                        plant,
                        values.map {
                            GardenPlanting.of(
                                it.garden_plantings_id,
                                it.plant_id,
                                it.plant_date,
                                it.last_watering_date
                            )
                        }
                    )
                }
        }

    fun insertGardenPlanting(plantId: String): Long =
        queries.transactionWithResult {
            val now = Clock.System.now().toEpochMilliseconds()
            queries.insert(
                plantId,
                now,
                now
            )

            queries.lastInsertRowId().executeAsOne()
        }

    fun deleteGardenPlanting(gardenPlanting: GardenPlanting): Unit {
        queries.deleteById(gardenPlanting.gardenPlantingId)
    }
}