package uk.co.zac_h.spacex.feature.assets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uk.co.zac_h.spacex.core.common.ContentType
import uk.co.zac_h.spacex.core.ui.component.SelectableContent
import uk.co.zac_h.spacex.core.ui.component.SpaceXTabLayout
import uk.co.zac_h.spacex.core.ui.component.SpaceXTabRowDefaults
import uk.co.zac_h.spacex.core.ui.component.Tab
import uk.co.zac_h.spacex.core.ui.component.TwoPane
import uk.co.zac_h.spacex.feature.assets.astronauts.AstronautsScreen
import uk.co.zac_h.spacex.feature.assets.vehicles.VehicleDetailsScreen
import uk.co.zac_h.spacex.feature.assets.vehicles.VehicleItem
import uk.co.zac_h.spacex.feature.assets.vehicles.dragon.DragonScreen
import uk.co.zac_h.spacex.feature.assets.vehicles.launcher.LauncherScreen
import uk.co.zac_h.spacex.feature.assets.vehicles.rockets.RocketsScreen
import uk.co.zac_h.spacex.feature.assets.vehicles.spacecraft.SpacecraftScreen

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AssetsScreen(
    contentType: ContentType,
    viewModel: AssetsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState(pageCount = { 5 })

    val scrollBehavior = SpaceXTabRowDefaults.pinnedScrollBehavior()

    LaunchedEffect(key1 = pagerState) {
        if (uiState.openedAssetPage != pagerState.currentPage) viewModel.closeDetailScreen()
    }

    val astronautsLazyListState = rememberLazyListState()
    val rocketsLazyListState = rememberLazyListState()
    val dragonLazyListState = rememberLazyListState()
    val coreLazyListState = rememberLazyListState()
    val capsulesLazyListState = rememberLazyListState()

    var expanded by rememberSaveable { mutableIntStateOf(-1) }

    AssetsContent(
        contentType = contentType,
        uiState = uiState,
        pagerState = pagerState,
        openedAsset = uiState.openedAsset,
        navigateToDetail = viewModel::setOpenedAsset,
        expanded = expanded,
        astronautListState = astronautsLazyListState,
        rocketsListState = rocketsLazyListState,
        dragonListState = dragonLazyListState,
        coreListState = coreLazyListState,
        capsulesListState = capsulesLazyListState,
        setExpanded = { expanded = it },
        scrollBehavior = scrollBehavior,
        closeDetailScreen = { viewModel.closeDetailScreen() }
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AssetsContent(
    modifier: Modifier = Modifier,
    contentType: ContentType,
    uiState: AssetsUIState,
    pagerState: PagerState,
    openedAsset: VehicleItem?,
    navigateToDetail: (VehicleItem, Int, ContentType) -> Unit,
    astronautListState: LazyListState,
    rocketsListState: LazyListState,
    dragonListState: LazyListState,
    coreListState: LazyListState,
    capsulesListState: LazyListState,
    closeDetailScreen: () -> Unit,
    expanded: Int,
    setExpanded: (Int) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TwoPane(
        modifier = modifier,
        first = {
            AssetsList(
                modifier = Modifier.fillMaxSize(),
                pagerState = pagerState,
                contentType = contentType,
                openedAsset = openedAsset,
                navigateToDetail = navigateToDetail,
                expanded = expanded,
                astronautListState = astronautListState,
                rocketsListState = rocketsListState,
                dragonListState = dragonListState,
                coreListState = coreListState,
                capsulesListState = capsulesListState,
                setExpanded = setExpanded,
                scrollBehavior = scrollBehavior
            )
        },
        second = {
            SelectableContent(
                value = uiState.openedAsset,
                content = {
                    key(it.id) {
                        VehicleDetailsScreen(
                            modifier = Modifier
                                .fillMaxSize(),
                            vehicle = it,
                            isFullscreen = false
                        )
                    }
                },
                label = "Select an asset"
            )
        },
        isTablet = contentType == ContentType.DUAL_PANE,
        isDetailOpen = uiState.isDetailOnlyOpen,
        hasDetailContent = uiState.openedAsset != null,
        closeDetailScreen = closeDetailScreen,
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AssetsList(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    contentType: ContentType,
    openedAsset: VehicleItem?,
    navigateToDetail: (VehicleItem, Int, ContentType) -> Unit,
    astronautListState: LazyListState,
    rocketsListState: LazyListState,
    dragonListState: LazyListState,
    coreListState: LazyListState,
    capsulesListState: LazyListState,
    expanded: Int,
    setExpanded: (Int) -> Unit,
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
        }
    ) { padding ->
        HorizontalPager(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            state = pagerState,
            beyondBoundsPageCount = 1,
            verticalAlignment = Alignment.Top,
            key = { tabs[it].label }
        ) { page ->
            when (page) {
                0 -> AstronautsScreen(
                    contentType = contentType,
                    openedAsset = openedAsset,
                    expanded = expanded,
                    state = astronautListState,
                    setExpanded = setExpanded,
                    onItemClick = { navigateToDetail(it, page, ContentType.SINGLE_PANE) }
                )
                1 -> RocketsScreen(
                    contentType = contentType,
                    openedAsset = openedAsset,
                    state = rocketsListState,
                    onItemClick = { navigateToDetail(it, page, ContentType.SINGLE_PANE) }
                )
                2 -> DragonScreen(
                    contentType = contentType,
                    openedAsset = openedAsset,
                    state = dragonListState,
                    onItemClick = { navigateToDetail(it, page, ContentType.SINGLE_PANE) }
                )
                3 -> LauncherScreen(
                    contentType = contentType,
                    openedAsset = openedAsset,
                    state = coreListState,
                    onItemClick = { navigateToDetail(it, page, ContentType.SINGLE_PANE) }
                )
                4 -> SpacecraftScreen(
                    contentType = contentType,
                    openedAsset = openedAsset,
                    state = capsulesListState,
                    onItemClick = { navigateToDetail(it, page, ContentType.SINGLE_PANE) }
                )
            }
        }
    }
}
