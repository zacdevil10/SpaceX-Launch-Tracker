package uk.co.zac_h.spacex.feature.assets

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uk.co.zac_h.spacex.core.common.ContentType
import uk.co.zac_h.spacex.core.ui.component.PagerItem
import uk.co.zac_h.spacex.core.ui.component.SpaceXTabLayout
import uk.co.zac_h.spacex.core.ui.component.TabRowDefaults
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

    val scrollBehavior = TabRowDefaults.pinnedScrollBehavior()

    LaunchedEffect(key1 = pagerState) {
        if (uiState.openedAssetPage != pagerState.currentPage) viewModel.closeDetailScreen()
    }

    val astronautsLazyListState = rememberLazyListState()
    val rocketsLazyListState = rememberLazyListState()
    val dragonLazyListState = rememberLazyListState()
    val coreLazyListState = rememberLazyListState()
    val capsulesLazyListState = rememberLazyListState()

    var expanded by rememberSaveable { mutableIntStateOf(-1) }

    when (contentType) {
        ContentType.DUAL_PANE -> AssetsTwoPaneContent(
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
            setExpanded = {
                expanded = it
            },
            scrollBehavior = scrollBehavior
        )

        ContentType.SINGLE_PANE -> AssetsSinglePaneContent(
            contentType = contentType,
            uiState = uiState,
            pagerState = pagerState,
            openedAsset = uiState.openedAsset,
            closeDetailScreen = viewModel::closeDetailScreen,
            navigateToDetail = viewModel::setOpenedAsset,
            expanded = expanded,
            astronautListState = astronautsLazyListState,
            rocketsListState = rocketsLazyListState,
            dragonListState = dragonLazyListState,
            coreListState = coreLazyListState,
            capsulesListState = capsulesLazyListState,
            setExpanded = {
                expanded = it
            },
            scrollBehavior = scrollBehavior
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AssetsTwoPaneContent(
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
    expanded: Int,
    setExpanded: (Int) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.inverseOnSurface)
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .weight(1f)
        ) {
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
        }
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .weight(1f)
        ) {
            uiState.openedAsset?.let {
                key(it.id) {
                    VehicleDetailsScreen(
                        modifier = Modifier
                            .fillMaxSize(),
                        vehicle = it,
                        isFullscreen = false
                    )
                }
            } ?: Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Select an asset")
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AssetsSinglePaneContent(
    modifier: Modifier = Modifier,
    contentType: ContentType,
    uiState: AssetsUIState,
    pagerState: PagerState,
    openedAsset: VehicleItem?,
    closeDetailScreen: () -> Unit,
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
    AnimatedContent(
        targetState = uiState.openedAsset != null && uiState.isDetailOnlyOpen,
        label = "",
        transitionSpec = {
            if (targetState > initialState) {
                (slideInHorizontally { width -> width / 2 } + fadeIn())
                    .togetherWith(slideOutHorizontally { width -> -(width / 2) } + fadeOut())
            } else {
                (slideInHorizontally { width -> -(width / 2) } + fadeIn())
                    .togetherWith(slideOutHorizontally { width -> width / 2 } + fadeOut())
            }.using(SizeTransform(clip = false))
        }
    ) {
        if (it && uiState.openedAsset != null) {
            BackHandler {
                closeDetailScreen()
            }
            VehicleDetailsScreen(
                modifier = modifier,
                vehicle = uiState.openedAsset
            ) {
                closeDetailScreen()
            }
        } else {
            AssetsList(
                modifier = modifier.fillMaxSize(),
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
        }
    }
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
    val screens = listOf(
        PagerItem(label = "Astronauts") { page ->
            AstronautsScreen(
                contentType = contentType,
                openedAsset = openedAsset,
                expanded = expanded,
                state = astronautListState,
                setExpanded = setExpanded
            ) {
                navigateToDetail(it, page, ContentType.SINGLE_PANE)
            }
        },
        PagerItem(label = "Rockets") { page ->
            RocketsScreen(
                contentType = contentType,
                openedAsset = openedAsset,
                state = rocketsListState
            ) {
                navigateToDetail(it, page, ContentType.SINGLE_PANE)
            }
        },
        PagerItem(label = "Second stage") { page ->
            DragonScreen(
                contentType = contentType,
                openedAsset = openedAsset,
                state = dragonListState
            ) {
                navigateToDetail(it, page, ContentType.SINGLE_PANE)
            }
        },
        PagerItem(label = "Core") { page ->
            LauncherScreen(
                contentType = contentType,
                openedAsset = openedAsset,
                state = coreListState
            ) {
                navigateToDetail(it, page, ContentType.SINGLE_PANE)
            }
        },
        PagerItem(label = "Capsules") { page ->
            SpacecraftScreen(
                contentType = contentType,
                openedAsset = openedAsset,
                state = capsulesListState
            ) {
                navigateToDetail(it, page, ContentType.SINGLE_PANE)
            }
        }
    )

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SpaceXTabLayout(
                pagerState = pagerState,
                screens = screens,
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
            key = { screens[it].label }
        ) { page ->
            screens[page].screen(page)
        }
    }
}