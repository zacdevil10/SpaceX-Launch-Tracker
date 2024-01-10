package uk.co.zac_h.spacex.feature.assets.vehicles.spacecraft

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import uk.co.zac_h.spacex.core.common.NetworkContent
import uk.co.zac_h.spacex.core.ui.Vehicle
import uk.co.zac_h.spacex.feature.assets.R
import uk.co.zac_h.spacex.feature.assets.vehicles.VehicleItem

@Composable
fun SpacecraftScreen(
    viewModel: SpacecraftViewModel = hiltViewModel(),
    openedAsset: VehicleItem?,
    onItemClick: (VehicleItem) -> Unit
) {
    val spacecraft = viewModel.spacecraftLiveData.collectAsLazyPagingItems()

    val spacecraftListState = rememberLazyListState()

    SpacecraftContent(
        vehicles = spacecraft,
        state = spacecraftListState,
        openedAsset = openedAsset,
        onItemClick = onItemClick
    )
}

@Composable
fun SpacecraftContent(
    modifier: Modifier = Modifier,
    vehicles: LazyPagingItems<SpacecraftItem>,
    state: LazyListState,
    openedAsset: VehicleItem?,
    onItemClick: (VehicleItem) -> Unit
) {
    NetworkContent(
        modifier = modifier,
        result = vehicles,
        state = state
    ) {
        items(
            count = vehicles.itemCount,
            key = vehicles.itemKey { it.id }
        ) { index ->
            val vehicle = vehicles[index]

            vehicle?.let {
                Vehicle(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    image = vehicle.imageUrl,
                    title = vehicle.title,
                    buttonText = stringResource(R.string.vehicle_list_item_specs_button_label),
                    navigate = {
                        onItemClick(vehicle)
                    }
                ) {
                    vehicle.description?.let {
                        Text(
                            text = it,
                            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 24.dp)
                        )
                    }
                }
            }
        }
    }
}