package uk.co.zac_h.spacex.feature.assets.vehicles

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import uk.co.zac_h.spacex.core.common.ContentType
import uk.co.zac_h.spacex.core.common.NetworkContent
import uk.co.zac_h.spacex.core.ui.Vehicle
import uk.co.zac_h.spacex.feature.assets.R
import uk.co.zac_h.spacex.network.ApiResult

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VehiclesList(
    modifier: Modifier = Modifier,
    vehicles: ApiResult<List<VehicleItem>>,
    contentType: ContentType,
    state: LazyListState,
    openedAsset: VehicleItem?,
    retry: () -> Unit,
    navigate: (VehicleItem) -> Unit,
    filter: @Composable () -> Unit = {}
) {
    NetworkContent(
        modifier = modifier,
        result = vehicles,
        retry = { retry() }
    ) { list ->
        LazyColumn(
            modifier = modifier,
            state = state
        ) {
            items(
                items = list,
                key = { it.id }
            ) { vehicle ->
                Vehicle(
                    modifier = Modifier
                        .animateItemPlacement()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    image = vehicle.imageUrl,
                    title = vehicle.title,
                    buttonText = stringResource(R.string.vehicle_list_item_specs_button_label),
                    isSelected = openedAsset?.id == vehicle.id && contentType == ContentType.DUAL_PANE,
                    navigate = {
                        navigate(vehicle)
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
        filter()
    }
}

@Composable
fun <T : VehicleItem> VehiclePaginatedList(
    modifier: Modifier = Modifier,
    vehicles: LazyPagingItems<T>,
    contentType: ContentType,
    state: LazyListState,
    openedAsset: VehicleItem?,
    navigate: (VehicleItem) -> Unit
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
                    isSelected = openedAsset?.id == vehicle.id && contentType == ContentType.DUAL_PANE,
                    navigate = {
                        navigate(vehicle)
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
