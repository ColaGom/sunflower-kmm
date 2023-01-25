/*
 * Copyright 2018 Google LLC
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

package com.google.samples.apps.sunflower.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.samples.apps.sunflower.PlantListFragment
import com.google.samples.apps.sunflower.shared.store.PlantListAction
import com.google.samples.apps.sunflower.shared.store.PlantListStore
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

/**
 * The ViewModel for [PlantListFragment].
 */
class PlantListViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), KoinComponent {

    private val initialGrowZone = savedStateHandle[GROW_ZONE_SAVED_STATE_KEY] ?: NO_GROW_ZONE

    private val store = get<PlantListStore> {
        parametersOf(initialGrowZone, viewModelScope)
    }

    val plants = store.state.map {
        it.plants
    }.distinctUntilChanged()

    init {
        viewModelScope.launch {
            store.state.map { it.growZone }
                .distinctUntilChanged()
                .collect { newGrowZone ->
                    savedStateHandle[GROW_ZONE_SAVED_STATE_KEY] = newGrowZone
                }
        }
    }

    fun toggleGrowZone() {
        val action = if (isFiltered()) PlantListAction.ClearGrowZone
        else PlantListAction.ChangeGrowZone(9)

        store.dispatch(action)
    }

    private fun isFiltered() = store.state.value.growZone != NO_GROW_ZONE

    companion object {
        private const val NO_GROW_ZONE = -1
        private const val GROW_ZONE_SAVED_STATE_KEY = "GROW_ZONE_SAVED_STATE_KEY"
    }
}
