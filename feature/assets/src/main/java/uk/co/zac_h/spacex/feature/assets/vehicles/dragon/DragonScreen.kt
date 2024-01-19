package uk.co.zac_h.spacex.feature.assets.vehicles.dragon

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.retry
import uk.co.zac_h.spacex.core.common.ContentType
import uk.co.zac_h.spacex.feature.assets.vehicles.VehicleItem
import uk.co.zac_h.spacex.feature.assets.vehicles.VehiclesList
import uk.co.zac_h.spacex.network.ApiResult

@Composable
fun DragonScreen(
    contentType: ContentType,
    viewModel: DragonViewModel = hiltViewModel(),
    openedAsset: VehicleItem?,
    onItemClick: (VehicleItem) -> Unit
) {
    val dragons by viewModel.dragons.collectAsStateWithLifecycle(ApiResult.Pending)
    val filter by viewModel.filter.collectAsStateWithLifecycle(DragonFilterState())

    val dragonLazyListState = rememberLazyListState()

    DragonContent(
        dragons = dragons,
        contentType = contentType,
        listState = dragonLazyListState,
        retry = { viewModel.dragons.retry() },
        openedAsset = openedAsset,
        onItemClick = onItemClick
    )
}

@Composable
fun DragonContent(
    modifier: Modifier = Modifier,
    dragons: ApiResult<List<VehicleItem>>,
    contentType: ContentType,
    listState: LazyListState,
    retry: () -> Unit,
    openedAsset: VehicleItem?,
    onItemClick: (VehicleItem) -> Unit
) {
    VehiclesList(
        modifier = modifier,
        vehicles = dragons,
        contentType = contentType,
        state = listState,
        openedAsset = openedAsset,
        retry = { retry() },
        navigate = {
            onItemClick(it)
        }
    )
}
