package uk.co.zac_h.spacex.feature.assets.vehicles.spacecraft

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import uk.co.zac_h.spacex.feature.assets.vehicles.VehicleItem
import uk.co.zac_h.spacex.feature.assets.vehicles.VehiclePaginatedList

@Composable
fun SpacecraftScreen(
    viewModel: SpacecraftViewModel = hiltViewModel(),
    selectedId: String?,
    state: LazyListState,
    onItemClick: (VehicleItem) -> Unit
) {
    val spacecraft = viewModel.spacecraftLiveData.collectAsLazyPagingItems()

    VehiclePaginatedList(
        vehicles = spacecraft,
        state = state,
        selectedId = selectedId,
        navigate = onItemClick
    )
}
