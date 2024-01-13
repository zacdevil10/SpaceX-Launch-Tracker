package uk.co.zac_h.spacex.feature.assets.vehicles.spacecraft

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import uk.co.zac_h.spacex.core.common.ContentType
import uk.co.zac_h.spacex.feature.assets.vehicles.VehicleItem
import uk.co.zac_h.spacex.feature.assets.vehicles.VehiclePaginatedList

@Composable
fun SpacecraftScreen(
    contentType: ContentType,
    viewModel: SpacecraftViewModel = hiltViewModel(),
    openedAsset: VehicleItem?,
    onItemClick: (VehicleItem) -> Unit
) {
    val spacecraft = viewModel.spacecraftLiveData.collectAsLazyPagingItems()

    val spacecraftListState = rememberLazyListState()

    VehiclePaginatedList(
        vehicles = spacecraft,
        contentType = contentType,
        state = spacecraftListState,
        openedAsset = openedAsset,
        navigate = onItemClick
    )
}
