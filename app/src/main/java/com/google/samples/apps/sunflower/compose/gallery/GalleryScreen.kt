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

package com.google.samples.apps.sunflower.compose.gallery

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.google.samples.apps.sunflower.R
import com.google.samples.apps.sunflower.compose.plantlist.PhotoListItem
import com.google.samples.apps.sunflower.shared.data.UnsplashPhoto
import com.google.samples.apps.sunflower.shared.data.UnsplashPhotoUrls
import com.google.samples.apps.sunflower.shared.data.UnsplashUser
import com.google.samples.apps.sunflower.viewmodels.GalleryViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.androidx.compose.getViewModel


@Composable
fun GalleryScreen(
    plantPictures: List<UnsplashPhoto>,
    onPhotoClick: (UnsplashPhoto) -> Unit = {},
    onUpClick: () -> Unit = {},
    onLoadMore: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.gallery_title))
                },
                Modifier.statusBarsPadding(),
                navigationIcon = {
                    IconButton(onClick = { onUpClick() }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
            )
        },
    ) { padding ->
        val gridState = rememberLazyGridState()

        LoadMore(listState = gridState) {
            onLoadMore()
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(all = dimensionResource(id = R.dimen.card_side_margin)),
            state = gridState
        ) {
            items(
                count = plantPictures.size,
            ) { index ->
                val photo = plantPictures[index]
                PhotoListItem(photo = photo) {
                    onPhotoClick(photo)
                }
            }
        }
    }
}

@Composable
fun GalleryScreen(
    onPhotoClick: (UnsplashPhoto) -> Unit = {},
    onUpClick: () -> Unit = {},
) {
    val viewModel = getViewModel<GalleryViewModel>()
    val state by viewModel.state.collectAsState()
    val plantPictures = state.plantPictures
    GalleryScreen(
        plantPictures = plantPictures,
        onPhotoClick,
        onUpClick,
        viewModel::onLoadMore
    )
}

@Preview
@Composable
private fun GalleryScreenPreview(
    @PreviewParameter(GalleryScreenPreviewParamProvider::class) plantPictures: List<UnsplashPhoto>
) {
    GalleryScreen(plantPictures = plantPictures)
}

private class GalleryScreenPreviewParamProvider :
    PreviewParameterProvider<List<UnsplashPhoto>> {

    override val values: Sequence<List<UnsplashPhoto>> =
        sequenceOf(
            listOf(
                UnsplashPhoto(
                    id = "1",
                    urls = UnsplashPhotoUrls("https://images.unsplash.com/photo-1417325384643-aac51acc9e5d?q=75&fm=jpg&w=400&fit=max"),
                    user = UnsplashUser("John Smith", "johnsmith")
                ),
                UnsplashPhoto(
                    id = "2",
                    urls = UnsplashPhotoUrls("https://images.unsplash.com/photo-1417325384643-aac51acc9e5d?q=75&fm=jpg&w=400&fit=max"),
                    user = UnsplashUser("Sally Smith", "sallysmith")
                )
            )
        )
}

@Composable
fun LoadMore(
    listState: LazyGridState,
    threshold: Int = 3,
    onLoadMore: () -> Unit
) {
    val loadMore = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val total = layoutInfo.totalItemsCount
            val lastItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1

            lastItemIndex > (total - threshold)
        }
    }

    LaunchedEffect(loadMore) {
        snapshotFlow { loadMore.value }
            .distinctUntilChanged()
            .collect {
                onLoadMore()
            }
    }
}