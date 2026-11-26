package uk.co.zac_h.spacex.feature.assets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import uk.co.zac_h.spacex.core.common.ContentType
import uk.co.zac_h.spacex.core.common.ListPosition
import uk.co.zac_h.spacex.core.common.rememberTrackedLazyListState
import uk.co.zac_h.spacex.core.ui.component.SpaceXTabLayout
import uk.co.zac_h.spacex.core.ui.component.SpaceXTabRowDefaults
import uk.co.zac_h.spacex.core.ui.component.Tab
import uk.co.zac_h.spacex.feature.assets.astronauts.AstronautsScreen
import uk.co.zac_h.spacex.feature.assets.vehicles.VehicleItem
import uk.co.zac_h.spacex.feature.assets.vehicles.VehicleType
import uk.co.zac_h.spacex.feature.assets.vehicles.dragon.DragonScreen
import uk.co.zac_h.spacex.feature.assets.vehicles.launcher.LauncherScreen
import uk.co.zac_h.spacex.feature.assets.vehicles.rockets.RocketsScreen
import uk.co.zac_h.spacex.feature.assets.vehicles.spacecraft.SpacecraftScreen

/**
 * The asset lists: astronauts, rockets, second stages, cores and capsules.
 *
 * @param selectedType the type of the asset open in the detail pane, if any.
 * @param selectedId the id of the asset open in the detail pane, highlighted on wide screens.
 * @param onVehicleClick opens an asset's details.
 * @param onCloseDetail closes the open asset's details, when another tab is picked.
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AssetsScreen(
    contentType: ContentType,
    selectedType: VehicleType?,
    selectedId: String?,
    onVehicleClick: (type: VehicleType, id: String) -> Unit,
    onCloseDetail: () -> Unit,
    viewModel: AssetsViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState(initialPage = viewModel.selectedPage, pageCount = { 5 })

    val scrollBehavior = SpaceXTabRowDefaults.pinnedScrollBehavior()

    // Picking another tab closes the open asset, which belongs to the tab being left.
    val currentSelectedType by rememberUpdatedState(selectedType)
    val currentOnCloseDetail by rememberUpdatedState(onCloseDetail)
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            viewModel.selectedPage = page
            if (currentSelectedType != null && currentSelectedType?.page != page) currentOnCloseDetail()
        }
    }

    val astronautListState = rememberTrackedListState(viewModel, VehicleType.ASTRONAUT)
    val rocketsListState = rememberTrackedListState(viewModel, VehicleType.ROCKET)
    val dragonListState = rememberTrackedListState(viewModel, VehicleType.SECOND_STAGE)
    val coreListState = rememberTrackedListState(viewModel, VehicleType.CORE)
    val capsulesListState = rememberTrackedListState(viewModel, VehicleType.CAPSULE)

    var expanded by rememberSaveable { mutableStateOf(viewModel.expandedAstronaut) }
    fun setExpanded(id: String?) {
        expanded = id
        viewModel.expandedAstronaut = id
    }

    // The picked astronaut follows the layout: it expands inline in the single-pane layout and
    // opens in the detail pane in the two-pane one, so folding or unfolding keeps it in view.
    val currentOnVehicleClick by rememberUpdatedState(onVehicleClick)
    LaunchedEffect(contentType, selectedType, selectedId) {
        val layoutChanged = viewModel.lastContentType != contentType
        viewModel.lastContentType = contentType
        when {
            selectedType == VehicleType.ASTRONAUT -> {
                setExpanded(selectedId)
                if (contentType == ContentType.SINGLE_PANE) currentOnCloseDetail()
            }
            contentType == ContentType.DUAL_PANE && selectedType == null -> {
                val id = expanded
                if (!layoutChanged) {
                    // The detail pane was closed, by back or by another tab.
                    setExpanded(null)
                } else if (id != null && pagerState.currentPage == VehicleType.ASTRONAUT.page) {
                    currentOnVehicleClick(VehicleType.ASTRONAUT, id)
                }
            }
        }
    }

    AssetsList(
        modifier = Modifier.fillMaxSize(),
        pagerState = pagerState,
        contentType = contentType,
        selectedType = selectedType,
        selectedId = selectedId,
        onVehicleClick = { type, vehicle ->
            val id = vehicle.id.toString()
            when {
                type != VehicleType.ASTRONAUT || contentType == ContentType.DUAL_PANE ->
                    onVehicleClick(type, id)
                // In the single-pane layout astronauts expand in the list instead.
                else -> setExpanded(if (expanded == id) null else id)
            }
        },
        expanded = expanded,
        astronautListState = astronautListState,
        rocketsListState = rocketsListState,
        dragonListState = dragonListState,
        coreListState = coreListState,
        capsulesListState = capsulesListState,
        scrollBehavior = scrollBehavior
    )
}

/** A tab's list state, starting from and saving to its position in [viewModel]. */
@Composable
private fun rememberTrackedListState(viewModel: AssetsViewModel, type: VehicleType): LazyListState =
    rememberTrackedLazyListState(viewModel.listPositions[type.page] ?: ListPosition()) {
        viewModel.listPositions[type.page] = it
    }

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AssetsList(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    contentType: ContentType,
    selectedType: VehicleType?,
    selectedId: String?,
    onVehicleClick: (VehicleType, VehicleItem) -> Unit,
    astronautListState: LazyListState,
    rocketsListState: LazyListState,
    dragonListState: LazyListState,
    coreListState: LazyListState,
    capsulesListState: LazyListState,
    expanded: String?,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val tabs = listOf(
        Tab(label = "Astronauts"),
        Tab(label = "Rockets"),
        Tab(label = "Second stage"),
        Tab(label = "Core"),
        Tab(label = "Capsules")
    )

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SpaceXTabLayout(
                pagerState = pagerState,
                tabs = tabs,
                scrollBehavior = scrollBehavior
            )
        },
        contentWindowInsets = WindowInsets.ime
    ) { padding ->
        HorizontalPager(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            state = pagerState,
            beyondViewportPageCount = 1,
            verticalAlignment = Alignment.Top,
            key = { tabs[it].label }
        ) { page ->
            val type = VehicleType.forPage(page)
            val selectedIdOnPage = selectedId.takeIf { selectedType == type }
            when (page) {
                0 -> AstronautsScreen(
                    contentType = contentType,
                    selectedId = selectedIdOnPage,
                    expandedId = expanded,
                    state = astronautListState,
                    onItemClick = { onVehicleClick(type, it) }
                )
                1 -> RocketsScreen(
                    contentType = contentType,
                    selectedId = selectedIdOnPage,
                    state = rocketsListState,
                    onItemClick = { onVehicleClick(type, it) }
                )
                2 -> DragonScreen(
                    contentType = contentType,
                    selectedId = selectedIdOnPage,
                    state = dragonListState,
                    onItemClick = { onVehicleClick(type, it) }
                )
                3 -> LauncherScreen(
                    contentType = contentType,
                    selectedId = selectedIdOnPage,
                    state = coreListState,
                    onItemClick = { onVehicleClick(type, it) }
                )
                4 -> SpacecraftScreen(
                    contentType = contentType,
                    selectedId = selectedIdOnPage,
                    state = capsulesListState,
                    onItemClick = { onVehicleClick(type, it) }
                )
            }
        }
    }
}
