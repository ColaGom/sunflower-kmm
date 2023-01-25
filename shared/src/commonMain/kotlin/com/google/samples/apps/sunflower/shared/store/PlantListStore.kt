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

import com.google.samples.apps.sunflower.shared.data.Plant
import com.google.samples.apps.sunflower.shared.data.PlantRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val NO_GROW_ZONE = -1

data class PlantListState(
    val growZone: Int = NO_GROW_ZONE,
    val plants: List<Plant> = emptyList()
) : State

sealed interface PlantListAction : Action {
    data class ChangeGrowZone(
        val growZone: Int
    ) : PlantListAction

    object ClearGrowZone : PlantListAction
}

class PlantListStore(
    growZone: Int,
    scope: CoroutineScope,
    private val plantRepository: PlantRepository,
) : Store<PlantListState, PlantListAction, SideEffect>, CoroutineScope by scope {
    private val _state = MutableStateFlow(PlantListState())
    override val state: StateFlow<PlantListState> = _state.asStateFlow()
    override val event: Flow<SideEffect> = MutableSharedFlow()

    init {
        launch { updateGrowZone(growZone) }
    }

    private suspend fun updateGrowZone(growZone: Int) {
        _state.update {
            val plants = if (growZone == NO_GROW_ZONE) plantRepository.getPlants()
            else plantRepository.getPlantsWithGrowZoneNumber(growZone)

            it.copy(
                growZone = growZone,
                plants = plants.first()
            )
        }
    }


    override fun dispatch(action: PlantListAction) {
        launch {
            when (action) {
                is PlantListAction.ChangeGrowZone -> updateGrowZone(action.growZone)
                PlantListAction.ClearGrowZone -> updateGrowZone(NO_GROW_ZONE)
            }
        }
    }
}