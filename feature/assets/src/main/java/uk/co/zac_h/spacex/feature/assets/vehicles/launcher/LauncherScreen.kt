package uk.co.zac_h.spacex.feature.assets.vehicles.launcher

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import uk.co.zac_h.spacex.core.common.ContentType
import uk.co.zac_h.spacex.feature.assets.vehicles.VehicleItem
import uk.co.zac_h.spacex.feature.assets.vehicles.VehiclePaginatedList

@Composable
fun LauncherScreen(
    contentType: ContentType,
    viewModel: LauncherViewModel = hiltViewModel(),
    openedAsset: VehicleItem?,
    state: LazyListState,
    onItemClick: (VehicleItem) -> Unit
) {
    val launchers = viewModel.launcherLiveData.collectAsLazyPagingItems()

    VehiclePaginatedList(
        vehicles = launchers,
        contentType = contentType,
        state = state,
        openedAsset = openedAsset,
        navigate = onItemClick
    )
}
