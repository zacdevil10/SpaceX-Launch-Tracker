package uk.co.zac_h.spacex.feature.assets.vehicles.dragon

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import uk.co.zac_h.spacex.core.common.NetworkContent
import uk.co.zac_h.spacex.feature.assets.vehicles.VehicleItem
import uk.co.zac_h.spacex.feature.assets.vehicles.VehiclesList
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.CachePolicy

@Composable
fun DragonScreen(
    viewModel: DragonViewModel = hiltViewModel(),
    openedAsset: VehicleItem?,
    onItemClick: (VehicleItem) -> Unit
) {
    viewModel.getDragons()

    val dragons by viewModel.dragons.observeAsState(ApiResult.Pending)

    val dragonLazyListState = rememberLazyListState()

    DragonContent(
        dragons = dragons,
        listState = dragonLazyListState,
        retry = {
            viewModel.getDragons(CachePolicy.REFRESH)
        },
        openedAsset = openedAsset,
        onItemClick = onItemClick
    )
}

@Composable
fun DragonContent(
    modifier: Modifier = Modifier,
    dragons: ApiResult<List<VehicleItem>>,
    listState: LazyListState,
    retry: () -> Unit,
    openedAsset: VehicleItem?,
    onItemClick: (VehicleItem) -> Unit
) {
    NetworkContent(
        modifier = modifier,
        result = dragons,
        retry = { retry() }
    ) {
        VehiclesList(
            vehicles = it,
            state = listState,
            navigate = {
                onItemClick(it)
            }
        )
    }
}
