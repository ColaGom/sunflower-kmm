/*
 * Copyright 2020 Google LLC
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
import com.google.samples.apps.sunflower.shared.store.GalleryStore
import com.google.samples.apps.sunflower.shared.store.LoadMore
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

class GalleryViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel(), KoinComponent {

    private var queryString: String? = savedStateHandle["plantName"]
    private val store = get<GalleryStore> {
        parametersOf(viewModelScope, queryString)
    }
    val state = store.state

    fun onLoadMore() {
        store.dispatch(LoadMore)
    }
}