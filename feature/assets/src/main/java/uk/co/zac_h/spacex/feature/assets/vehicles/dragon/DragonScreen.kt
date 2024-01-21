package uk.co.zac_h.spacex.feature.assets.vehicles.dragon

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uk.co.zac_h.spacex.core.common.ContentType
import uk.co.zac_h.spacex.core.common.isScrollingUp
import uk.co.zac_h.spacex.core.common.types.Order
import uk.co.zac_h.spacex.core.ui.FilterRow
import uk.co.zac_h.spacex.core.ui.ModalFilterBottomSheet
import uk.co.zac_h.spacex.feature.assets.R
import uk.co.zac_h.spacex.feature.assets.vehicles.VehicleItem
import uk.co.zac_h.spacex.feature.assets.vehicles.VehiclesList
import uk.co.zac_h.spacex.network.ApiResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DragonScreen(
    contentType: ContentType,
    viewModel: DragonViewModel = hiltViewModel(),
    openedAsset: VehicleItem?,
    state: LazyListState,
    onItemClick: (VehicleItem) -> Unit
) {
    val dragons by viewModel.dragons.collectAsStateWithLifecycle(ApiResult.Pending)
    val filter by viewModel.filter.collectAsStateWithLifecycle(DragonFilterState())

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = filter) {
        state.animateScrollToItem(index = 0)
    }

    DragonContent(
        dragons = dragons,
        contentType = contentType,
        state = state,
        retry = { viewModel.getDragons() },
        showBottomSheet = showBottomSheet,
        setSheetState = { showBottomSheet = it },
        sheetState = sheetState,
        filter = filter,
        setOrder = viewModel::updateOrder,
        reset = viewModel::resetFilter,
        openedAsset = openedAsset,
        onItemClick = onItemClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DragonContent(
    modifier: Modifier = Modifier,
    dragons: ApiResult<List<VehicleItem>>,
    contentType: ContentType,
    state: LazyListState,
    retry: () -> Unit,
    showBottomSheet: Boolean,
    setSheetState: (Boolean) -> Unit,
    sheetState: SheetState,
    filter: DragonFilterState,
    setOrder: (Order) -> Unit,
    reset: () -> Unit,
    openedAsset: VehicleItem?,
    onItemClick: (VehicleItem) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            AnimatedVisibility(
                visible = state.isScrollingUp() && dragons is ApiResult.Success,
                enter = scaleIn(),
                exit = scaleOut(),
            ) {
                FloatingActionButton(
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_sort_black_24dp),
                            contentDescription = ""
                        )
                    },
                    onClick = {
                        setSheetState(true)
                    }
                )
            }
        }
    ) { contentPadding ->
        VehiclesList(
            modifier = modifier
                .padding(contentPadding),
            vehicles = dragons,
            contentType = contentType,
            state = state,
            openedAsset = openedAsset,
            retry = { retry() },
            navigate = {
                onItemClick(it)
            },
            filter = {
                if (showBottomSheet) {
                    DragonFilterModalBottomSheet(
                        setSheetState = setSheetState,
                        sheetState = sheetState,
                        filter = filter,
                        setOrder = setOrder,
                        reset = reset
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DragonFilterModalBottomSheet(
    setSheetState: (Boolean) -> Unit,
    sheetState: SheetState,
    filter: DragonFilterState,
    setOrder: (Order) -> Unit,
    reset: () -> Unit
) {
    ModalFilterBottomSheet(
        setSheetState = setSheetState,
        sheetState = sheetState,
        reset = reset
    ) {
        FilterRow(label = "Order:") {
            FilterChip(
                selected = filter.order.value == Order.ASCENDING,
                onClick = { setOrder(Order.ASCENDING) },
                label = { Text(text = "Ascending") }
            )
            FilterChip(
                selected = filter.order.value == Order.DESCENDING,
                onClick = { setOrder(Order.DESCENDING) },
                label = { Text(text = "Descending") }
            )
        }
    }
}
