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

package com.google.samples.apps.sunflower.compose.garden

import androidx.activity.compose.ReportDrawn
import androidx.activity.compose.ReportDrawnWhen
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.google.accompanist.themeadapter.material.MdcTheme
import com.google.samples.apps.sunflower.R
import com.google.samples.apps.sunflower.compose.card
import com.google.samples.apps.sunflower.compose.utils.SunflowerImage
import com.google.samples.apps.sunflower.shared.data.GardenPlanting
import com.google.samples.apps.sunflower.shared.data.Plant
import com.google.samples.apps.sunflower.shared.data.PlantAndGardenPlantings
import com.google.samples.apps.sunflower.shared.model.PlantAndGardenPlantingsUiModel
import com.google.samples.apps.sunflower.shared.store.GardenPlantingListStore
import org.koin.androidx.compose.get
import org.koin.core.parameter.parametersOf
import java.util.*

@Composable
fun GardenScreen(
    onAddPlantClick: () -> Unit,
    onPlantClick: (PlantAndGardenPlantings) -> Unit
) {
    val scope = rememberCoroutineScope()
    val store = get<GardenPlantingListStore> { parametersOf(scope) }
    val state by store.state.collectAsState()
    GardenScreen(
        gardenPlants = state.plantAndGardenPlantings,
        onAddPlantClick = onAddPlantClick,
        onPlantClick = onPlantClick
    )
}

@Composable
private fun GardenScreen(
    gardenPlants: List<PlantAndGardenPlantings>,
    onAddPlantClick: () -> Unit = {},
    onPlantClick: (PlantAndGardenPlantings) -> Unit = {}
) {
    if (gardenPlants.isEmpty()) {
        EmptyGarden(onAddPlantClick)
    } else {
        GardenList(gardenPlants, onPlantClick = onPlantClick)
    }
}

@Composable
private fun GardenList(
    gardenPlants: List<PlantAndGardenPlantings>,
    onPlantClick: (PlantAndGardenPlantings) -> Unit
) {
    // Call reportFullyDrawn when the garden list has been rendered
    val gridState = rememberLazyGridState()
    ReportDrawnWhen { gridState.layoutInfo.totalItemsCount > 0 }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = gridState,
        contentPadding = PaddingValues(
            horizontal = dimensionResource(id = R.dimen.card_side_margin),
            vertical = dimensionResource(id = R.dimen.margin_normal)
        )
    ) {
        items(count = gardenPlants.size) { index ->
            val plant = gardenPlants[index]
            GardenListItem(plant = plant, onPlantClick = onPlantClick)
        }
    }
}

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
private fun GardenListItem(
    plant: PlantAndGardenPlantings,
    onPlantClick: (PlantAndGardenPlantings) -> Unit
) {
    val vm = PlantAndGardenPlantingsUiModel(plant)

    // Dimensions
    val cardSideMargin = dimensionResource(id = R.dimen.card_side_margin)
    val marginNormal = dimensionResource(id = R.dimen.margin_normal)

    Card(
        onClick = { onPlantClick(plant) },
        modifier = Modifier.padding(
            start = cardSideMargin,
            end = cardSideMargin,
            bottom = dimensionResource(id = R.dimen.card_bottom_margin)
        ),
        elevation = dimensionResource(id = R.dimen.card_elevation),
        shape = MaterialTheme.shapes.card,
    ) {
        Column(Modifier.fillMaxWidth()) {
            SunflowerImage(
                model = vm.imageUrl,
                contentDescription = plant.plant.description,
                Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.plant_item_image_height)),
                contentScale = ContentScale.Crop,
            )

            // Plant name
            Text(
                text = vm.plantName,
                Modifier
                    .padding(vertical = marginNormal)
                    .align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.subtitle1,
            )

            // Planted date
            Text(
                text = stringResource(id = R.string.plant_date_header),
                Modifier.align(Alignment.CenterHorizontally),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primaryVariant,
                style = MaterialTheme.typography.subtitle2
            )
            Text(
                text = vm.plantDateString,
                Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.subtitle2
            )

            // Last Watered
            Text(
                text = stringResource(id = R.string.watered_date_header),
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = marginNormal),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primaryVariant,
                style = MaterialTheme.typography.subtitle2
            )
            Text(
                text = vm.waterDateString,
                Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.subtitle2
            )
            Text(
                text = pluralStringResource(
                    id = R.plurals.watering_next,
                    count = vm.wateringInterval,
                    vm.wateringInterval
                ),
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = marginNormal),
                style = MaterialTheme.typography.subtitle2
            )
        }
    }
}

@Composable
private fun EmptyGarden(onAddPlantClick: () -> Unit) {
    // Calls reportFullyDrawn when this composable is composed.
    ReportDrawn()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.garden_empty),
            style = MaterialTheme.typography.h5
        )
        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.onPrimary),
            shape = RoundedCornerShape(
                topStart = 0.dp,
                topEnd = dimensionResource(id = R.dimen.button_corner_radius),
                bottomStart = dimensionResource(id = R.dimen.button_corner_radius),
                bottomEnd = 0.dp,
            ),
            onClick = onAddPlantClick
        ) {
            Text(
                color = MaterialTheme.colors.primary,
                text = stringResource(id = R.string.add_plant)
            )
        }
    }
}

@Preview
@Composable
private fun GardenScreenPreview(
    @PreviewParameter(GardenScreenPreviewParamProvider::class) gardenPlants: List<PlantAndGardenPlantings>
) {
    MdcTheme {
        GardenScreen(gardenPlants)
    }
}

private class GardenScreenPreviewParamProvider :
    PreviewParameterProvider<List<PlantAndGardenPlantings>> {
    override val values: Sequence<List<PlantAndGardenPlantings>> =
        sequenceOf(
            emptyList(),
            listOf(
                PlantAndGardenPlantings(
                    plant = Plant(
                        plantId = "1",
                        name = "Apple",
                        description = "An apple.",
                        growZoneNumber = 1,
                        wateringInterval = 2,
                        imageUrl = "https://images.unsplash.com/photo-1417325384643-aac51acc9e5d?q=75&fm=jpg&w=400&fit=max",
                    ),
                    gardenPlantings = listOf(
                        GardenPlanting(
                            gardenPlantingId = 1,
                            plantId = "1",
                        )
                    )
                )
            )
        )
}