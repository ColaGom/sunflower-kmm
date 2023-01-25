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

import androidx.lifecycle.*
import com.google.samples.apps.sunflower.PlantDetailFragment
import com.google.samples.apps.sunflower.shared.store.PlantDetailAction
import com.google.samples.apps.sunflower.shared.store.PlantDetailStore
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

/**
 * The ViewModel used in [PlantDetailFragment].
 */
class PlantDetailViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), KoinComponent {
    private val plantId: String = savedStateHandle.get<String>(PLANT_ID_SAVED_STATE_KEY)!!
    private val store = get<PlantDetailStore> {
        parametersOf(plantId, viewModelScope)
    }

    val state = store.state

    private val _showSnackbar = MutableLiveData(false)
    val showSnackbar: LiveData<Boolean>
        get() = _showSnackbar

    fun addPlantToGarden() {
        viewModelScope.launch {
            _showSnackbar.value = true
        }
        store.dispatch(PlantDetailAction.AddPlantToGarden)
    }

    fun dismissSnackbar() {
        _showSnackbar.value = false
    }

    companion object {
        private const val PLANT_ID_SAVED_STATE_KEY = "plantId"
    }
}
