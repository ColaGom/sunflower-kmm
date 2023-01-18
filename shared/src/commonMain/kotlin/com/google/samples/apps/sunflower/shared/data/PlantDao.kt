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
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import kotlinx.coroutines.flow.Flow

/**
 * The Data Access Object for the Plant class.
 */
class PlantDao(
    private val queries: PlantsQueries
) {
    fun getPlants(): Flow<List<Plant>> = queries.findAll(Plant.Companion::of).asFlow().mapToList()

    fun getPlantsWithGrowZoneNumber(growZoneNumber: Int): Flow<List<Plant>> =
        queries.findByGrowZoneNumber(growZoneNumber, Plant.Companion::of).asFlow().mapToList()

    fun getPlant(plantId: String): Flow<Plant> =
        queries.findById(plantId, Plant.Companion::of).asFlow().mapToOne()

    fun insertAll(plants: List<Plant>) {
        queries.transaction {
            plants.forEach {
                queries.insert(
                    Plants(
                        it.plantId,
                        it.name,
                        it.description,
                        it.growZoneNumber,
                        it.wateringInterval,
                        it.imageUrl
                    )
                )
            }
        }
    }
}
