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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.distinctUntilChanged
import uk.co.zac_h.spacex.core.common.ContentType
import uk.co.zac_h.spacex.core.ui.PagerItem
import uk.co.zac_h.spacex.core.ui.SpaceXTabLayout
import uk.co.zac_h.spacex.feature.assets.astronauts.AstronautsScreen
import uk.co.zac_h.spacex.feature.assets.vehicles.VehicleDetailsScreen
import uk.co.zac_h.spacex.feature.assets.vehicles.VehicleItem
import uk.co.zac_h.spacex.feature.assets.vehicles.dragon.DragonScreen
import uk.co.zac_h.spacex.feature.assets.vehicles.launcher.LauncherScreen
import uk.co.zac_h.spacex.feature.assets.vehicles.rockets.RocketsScreen
import uk.co.zac_h.spacex.feature.assets.vehicles.spacecraft.SpacecraftScreen

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AssetsScreen(
    modifier: Modifier = Modifier,
    contentType: ContentType,
    viewModel: AssetsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState(pageCount = { 5 })

    LaunchedEffect(key1 = pagerState) {
        snapshotFlow { pagerState.currentPage }.distinctUntilChanged().collect { page ->
            viewModel.closeDetailScreen()
        }
    }

    val astronautsLazyListState = rememberLazyListState()

    var expanded by rememberSaveable { mutableIntStateOf(-1) }

    when (contentType) {
        ContentType.DUAL_PANE -> AssetsTwoPaneContent(
            modifier = modifier,
            contentType = contentType,
            uiState = uiState,
            pagerState = pagerState,
            openedAsset = uiState.openedAsset,
            navigateToDetail = { asset, pane ->
                viewModel.setOpenedAsset(asset, pane)
            },
            expanded = expanded,
            state = astronautsLazyListState,
            setExpanded = {
                expanded = it
            }
        )

        ContentType.SINGLE_PANE -> AssetsSinglePaneContent(
            modifier = modifier,
            contentType = contentType,
            uiState = uiState,
            pagerState = pagerState,
            openedAsset = uiState.openedAsset,
            closeDetailScreen = {
                viewModel.closeDetailScreen()
            },
            navigateToDetail = { asset, pane ->
                viewModel.setOpenedAsset(asset, pane)
            },
            expanded = expanded,
            state = astronautsLazyListState,
            setExpanded = {
                expanded = it
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AssetsTwoPaneContent(
    modifier: Modifier = Modifier,
    contentType: ContentType,
    uiState: AssetsUIState,
    pagerState: PagerState,
    openedAsset: VehicleItem?,
    navigateToDetail: (VehicleItem, ContentType) -> Unit,
    state: LazyListState,
    expanded: Int,
    setExpanded: (Int) -> Unit,
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
                state = state,
                setExpanded = setExpanded
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AssetsSinglePaneContent(
    modifier: Modifier = Modifier,
    contentType: ContentType,
    uiState: AssetsUIState,
    pagerState: PagerState,
    openedAsset: VehicleItem?,
    closeDetailScreen: () -> Unit,
    navigateToDetail: (VehicleItem, ContentType) -> Unit,
    state: LazyListState,
    expanded: Int,
    setExpanded: (Int) -> Unit,
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
                state = state,
                setExpanded = setExpanded
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AssetsList(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    contentType: ContentType,
    openedAsset: VehicleItem?,
    navigateToDetail: (VehicleItem, ContentType) -> Unit,
    state: LazyListState,
    expanded: Int,
    setExpanded: (Int) -> Unit,
) {
    val screens = listOf(
        PagerItem(label = "Astronauts") {
            AstronautsScreen(
                contentType = contentType,
                openedAsset = openedAsset,
                expanded = expanded,
                state = state,
                setExpanded = setExpanded
            ) {
                navigateToDetail(it, ContentType.SINGLE_PANE)
            }
        },
        PagerItem(label = "Rockets") {
            RocketsScreen(
                openedAsset = openedAsset
            ) {
                navigateToDetail(it, ContentType.SINGLE_PANE)
            }
        },
        PagerItem(label = "Second stage") {
            DragonScreen(
                openedAsset = openedAsset
            ) {
                navigateToDetail(it, ContentType.SINGLE_PANE)
            }
        },
        PagerItem(label = "Core") {
            LauncherScreen(
                openedAsset = openedAsset
            ) {
                navigateToDetail(it, ContentType.SINGLE_PANE)
            }
        },
        PagerItem(label = "Capsules") {
            SpacecraftScreen(
                openedAsset = openedAsset
            ) {
                navigateToDetail(it, ContentType.SINGLE_PANE)
            }
        }
    )

    Scaffold(
        modifier = modifier,
        topBar = {
            SpaceXTabLayout(
                pagerState = pagerState,
                screens = screens,
                scrollable = true
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
            screens[page].screen()
        }
    }
}