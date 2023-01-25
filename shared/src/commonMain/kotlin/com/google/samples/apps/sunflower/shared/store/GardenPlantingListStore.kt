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

import com.google.samples.apps.sunflower.shared.data.GardenPlantingRepository
import com.google.samples.apps.sunflower.shared.data.PlantAndGardenPlantings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

data class GardenPlantingListState(
    val plantAndGardenPlantings: List<PlantAndGardenPlantings> = emptyList()
) : State

class GardenPlantingListStore(
    scope: CoroutineScope,
    gardenPlantingRepository: GardenPlantingRepository
) : Store<GardenPlantingListState, Action, SideEffect> {

    private val _state = MutableStateFlow(GardenPlantingListState())
    override val state: StateFlow<GardenPlantingListState> = _state.asStateFlow()
    override val event: Flow<SideEffect> = MutableSharedFlow()

    init {
        gardenPlantingRepository.getPlantedGardens()
            .onEach { items ->
                _state.update {
                    it.copy(plantAndGardenPlantings = items)
                }
            }.launchIn(scope)
    }

    override fun dispatch(action: Action) {}
}