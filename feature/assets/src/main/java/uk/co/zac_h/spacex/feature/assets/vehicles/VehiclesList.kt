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
import uk.co.zac_h.spacex.core.ui.Vehicle
import uk.co.zac_h.spacex.feature.assets.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VehiclesList(
    modifier: Modifier = Modifier,
    vehicles: List<VehicleItem>,
    state: LazyListState,
    navigate: (VehicleItem) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        state = state
    ) {
        items(
            vehicles,
            key = { it.id }
        ) { vehicle ->
            Vehicle(
                modifier = Modifier
                    .animateItemPlacement()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                image = vehicle.imageUrl,
                title = vehicle.title,
                buttonText = stringResource(R.string.vehicle_list_item_specs_button_label),
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
