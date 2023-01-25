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

import com.google.samples.apps.sunflower.shared.data.UnsplashPhoto
import com.google.samples.apps.sunflower.shared.data.UnsplashRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class GalleryState(
    val plantPictures: List<UnsplashPhoto> = emptyList(),
    val page: Int = 1,
    val totalPages: Int = -1
) : State {
    val hasMore = page < totalPages
}

class GalleryStore(
    scope: CoroutineScope,
    private val queryString: String?,
    private val repository: UnsplashRepository
) : Store<GalleryState, Action, SideEffect>, CoroutineScope by scope {
    private val _state = MutableStateFlow(GalleryState())
    override val state: StateFlow<GalleryState> = _state.asStateFlow()
    override val event: Flow<SideEffect> = MutableSharedFlow()

    init {
        launch { refresh() }
    }

    private suspend fun refresh() {
        _state.update {
            val response = repository.getSearchResultStream(queryString ?: "", page = 1)
            it.copy(
                plantPictures = response.results,
                page = 1,
                totalPages = response.totalPages
            )
        }
    }

    private suspend fun loadMore() {
        _state.update {
            val nextPage = _state.value.page + 1
            val current = _state.value.plantPictures
            val response = repository.getSearchResultStream(queryString ?: "", page = nextPage)
            it.copy(
                plantPictures = current + response.results,
                page = nextPage,
                totalPages = response.totalPages
            )
        }
    }

    override fun dispatch(action: Action) {
        launch {
            when (action) {
                Refresh -> refresh()
                LoadMore -> {
                    val current = _state.value
                    if (current.hasMore) {
                        loadMore()
                    }
                }
                else -> {}
            }
        }
    }
}