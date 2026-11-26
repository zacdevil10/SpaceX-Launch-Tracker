package uk.co.zac_h.spacex.feature.assets.vehicles.launcher

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import uk.co.zac_h.spacex.feature.assets.vehicles.VehicleItem
import uk.co.zac_h.spacex.feature.assets.vehicles.VehiclePaginatedList

@Composable
fun LauncherScreen(
    viewModel: LauncherViewModel = hiltViewModel(),
    selectedId: String?,
    state: LazyListState,
    onItemClick: (VehicleItem) -> Unit
) {
    val launchers = viewModel.launcherLiveData.collectAsLazyPagingItems()

    VehiclePaginatedList(
        vehicles = launchers,
        state = state,
        selectedId = selectedId,
        navigate = onItemClick
    )
}
