package uk.co.zac_h.spacex.feature.assets.vehicles.rockets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import uk.co.zac_h.spacex.core.common.NetworkContent
import uk.co.zac_h.spacex.core.common.filter.FilterOrder
import uk.co.zac_h.spacex.core.common.filter.RocketFamilyFilter
import uk.co.zac_h.spacex.core.common.filter.RocketTypeFilter
import uk.co.zac_h.spacex.core.common.isScrollingUp
import uk.co.zac_h.spacex.core.common.types.Order
import uk.co.zac_h.spacex.core.common.types.RocketFamily
import uk.co.zac_h.spacex.core.common.types.RocketType
import uk.co.zac_h.spacex.core.ui.DevicePreviews
import uk.co.zac_h.spacex.core.ui.SpaceXTheme
import uk.co.zac_h.spacex.feature.assets.R
import uk.co.zac_h.spacex.feature.assets.vehicles.VehicleItem
import uk.co.zac_h.spacex.feature.assets.vehicles.VehiclesList
import uk.co.zac_h.spacex.feature.assets.vehicles.rockets.filter.RocketsFilter
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.CachePolicy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RocketsScreen(
    viewModel: RocketViewModel = hiltViewModel(),
    openedAsset: VehicleItem?,
    onItemClick: (VehicleItem) -> Unit
) {
    viewModel.getRockets()

    val rockets by viewModel.rockets.observeAsState(ApiResult.Pending)
    val filter by viewModel.filter.observeAsState(
        RocketsFilter(
            RocketFamilyFilter(),
            RocketTypeFilter(),
            FilterOrder()
        )
    )

    val rocketsLazyListState = rememberLazyListState()

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    RocketsContent(
        rockets = rockets,
        listState = rocketsLazyListState,
        retry = {
            viewModel.getRockets(CachePolicy.REFRESH)
        },
        showBottomSheet = showBottomSheet,
        setSheetState = {
            showBottomSheet = it
        },
        sheetState = sheetState,
        filter = filter,
        setFamily = {
            viewModel.filter.filterByFamily(if (filter.family.family == it) RocketFamily.NONE else it)

            if (it != RocketFamily.FALCON) viewModel.filter.filterByRocketType(null)
        },
        setRocketType = {
            val list = filter.type.rockets.orEmpty().toMutableList()

            if (it in list) list.remove(it) else list.add(it)

            viewModel.filter.filterByRocketType(list)
        },
        setOrder = {
            viewModel.filter.setOrder(it)
        },
        reset = {
            viewModel.filter.clear()
        },
        openedAsset = openedAsset,
        onItemClick = onItemClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RocketsContent(
    modifier: Modifier = Modifier,
    rockets: ApiResult<List<VehicleItem>>,
    listState: LazyListState,
    retry: () -> Unit,
    showBottomSheet: Boolean,
    setSheetState: (Boolean) -> Unit,
    sheetState: SheetState,
    filter: RocketsFilter,
    setFamily: (RocketFamily) -> Unit,
    setRocketType: (RocketType) -> Unit,
    setOrder: (Order) -> Unit,
    reset: () -> Unit,
    openedAsset: VehicleItem?,
    onItemClick: (VehicleItem) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            AnimatedVisibility(
                visible = listState.isScrollingUp(),
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
        NetworkContent(
            modifier = modifier
                .padding(contentPadding),
            result = rockets,
            retry = { retry() }
        ) {
            VehiclesList(
                vehicles = it,
                state = listState,
                navigate = {
                    onItemClick(it)
                }
            )

            if (showBottomSheet) {
                RocketsFilterModalBottomSheet(
                    setSheetState = setSheetState,
                    sheetState = sheetState,
                    filter = filter,
                    setFamily = setFamily,
                    setRocketType = setRocketType,
                    setOrder = setOrder,
                    reset = reset
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RocketsFilterModalBottomSheet(
    setSheetState: (Boolean) -> Unit,
    sheetState: SheetState,
    filter: RocketsFilter,
    setFamily: (RocketFamily) -> Unit,
    setRocketType: (RocketType) -> Unit,
    setOrder: (Order) -> Unit,
    reset: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = {
            setSheetState(false)
        },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 48.dp)
        ) {
            Row {
                Text(
                    modifier = Modifier
                        .height(48.dp)
                        .weight(0.25f)
                        .wrapContentHeight(align = Alignment.CenterVertically)
                        .padding(start = 16.dp),
                    text = "Family:"
                )
                Spacer(modifier = Modifier.width(16.dp))
                FlowRow(
                    modifier = Modifier.weight(0.8f),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FilterChip(
                        selected = filter.family.family == RocketFamily.FALCON,
                        onClick = { setFamily(RocketFamily.FALCON) },
                        label = {
                            Text(text = "Falcon")
                        }
                    )
                    FilterChip(
                        selected = filter.family.family == RocketFamily.STARSHIP,
                        onClick = { setFamily(RocketFamily.STARSHIP) },
                        label = {
                            Text(text = "Starship")
                        }
                    )
                }
            }
            Row(
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(
                    modifier = Modifier
                        .height(48.dp)
                        .weight(0.25f)
                        .wrapContentHeight(align = Alignment.CenterVertically)
                        .padding(start = 16.dp),
                    text = "Type:"
                )
                Spacer(modifier = Modifier.width(16.dp))
                FlowRow(
                    modifier = Modifier.weight(0.8f),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FilterChip(
                        selected = RocketType.FALCON_ONE in filter.type.rockets.orEmpty(),
                        enabled = filter.family.family == RocketFamily.FALCON,
                        onClick = { setRocketType(RocketType.FALCON_ONE) },
                        label = {
                            Text(text = "Falcon 1")
                        }
                    )
                    FilterChip(
                        selected = RocketType.FALCON_NINE in filter.type.rockets.orEmpty(),
                        enabled = filter.family.family == RocketFamily.FALCON,
                        onClick = { setRocketType(RocketType.FALCON_NINE) },
                        label = {
                            Text(text = "Falcon 9")
                        }
                    )
                    FilterChip(
                        selected = RocketType.FALCON_HEAVY in filter.type.rockets.orEmpty(),
                        enabled = filter.family.family == RocketFamily.FALCON,
                        onClick = { setRocketType(RocketType.FALCON_HEAVY) },
                        label = {
                            Text(text = "Falcon Heavy")
                        }
                    )
                }
            }
            Row(
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(
                    modifier = Modifier
                        .height(48.dp)
                        .weight(0.25f)
                        .wrapContentHeight(align = Alignment.CenterVertically)
                        .padding(start = 16.dp),
                    text = "Orbit:"
                )
                Spacer(modifier = Modifier.width(16.dp))
                FlowRow(
                    modifier = Modifier.weight(0.8f),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FilterChip(
                        selected = filter.order.order == Order.ASCENDING,
                        onClick = { setOrder(Order.ASCENDING) },
                        label = {
                            Text(text = "Ascending")
                        }
                    )
                    FilterChip(
                        selected = filter.order.order == Order.DESCENDING,
                        onClick = { setOrder(Order.DESCENDING) },
                        label = {
                            Text(text = "Descending")
                        }
                    )
                }
            }
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp),
                onClick = { reset() }
            ) {
                Text(text = "Reset")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@DevicePreviews
@Composable
fun RocketsFilterModalBottomSheetPreview() {
    SpaceXTheme {
        RocketsFilterModalBottomSheet(
            setSheetState = {},
            sheetState = SheetState(
                skipPartiallyExpanded = true,
                initialValue = SheetValue.Expanded
            ),
            filter = RocketsFilter(
                RocketFamilyFilter(),
                RocketTypeFilter(),
                FilterOrder()
            ),
            setOrder = {},
            setRocketType = {},
            setFamily = {},
            reset = {}
        )
    }
}