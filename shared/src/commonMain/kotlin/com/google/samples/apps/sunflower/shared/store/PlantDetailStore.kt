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

package com.google.samples.apps.sunflower.shared.store

import com.google.samples.apps.sunflower.shared.common.API_ACCESS_KEY
import com.google.samples.apps.sunflower.shared.data.GardenPlantingRepository
import com.google.samples.apps.sunflower.shared.data.Plant
import com.google.samples.apps.sunflower.shared.data.PlantRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class PlantDetailState(
    val plantId: String,
    val plant: Plant? = null,
    val isPlanted: Boolean = false,
    val hasValidUnsplashKey: Boolean = API_ACCESS_KEY.isNotBlank()
) : State

sealed interface PlantDetailAction : Action {
    object AddPlantToGarden : PlantDetailAction
}

class PlantDetailStore(
    private val plantId: String,
    plantRepository: PlantRepository,
    private val gardenPlantingRepository: GardenPlantingRepository,
    scope: CoroutineScope
) : Store<PlantDetailState, PlantDetailAction, SideEffect>, CoroutineScope by scope {
    private val _state = MutableStateFlow(PlantDetailState(plantId))
    override val state: StateFlow<PlantDetailState> = _state.asStateFlow()
    override val event: Flow<SideEffect> = MutableSharedFlow()

    init {
        launch {
            plantRepository.getPlant(plantId)
                .combine(gardenPlantingRepository.isPlanted(plantId)) { plant, isPlanted ->
                    plant to isPlanted
                }
                .distinctUntilChanged()
                .collect { (plant, isPlanted) ->
                    _state.update { it.copy(plant = plant, isPlanted = isPlanted) }
                }
        }
    }

    override fun dispatch(action: PlantDetailAction) {
        launch {
            when (action) {
                PlantDetailAction.AddPlantToGarden -> {
                    gardenPlantingRepository.createGardenPlanting(plantId)
                }
            }
        }
    }
}