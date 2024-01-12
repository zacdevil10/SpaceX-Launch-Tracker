package uk.co.zac_h.spacex.feature.launch

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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import uk.co.zac_h.spacex.core.common.ContentType
import uk.co.zac_h.spacex.core.ui.PagerItem
import uk.co.zac_h.spacex.core.ui.SpaceXTabLayout
import uk.co.zac_h.spacex.feature.launch.details.LaunchContainerScreen
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.CachePolicy

@Composable
fun LaunchListScreen(
    modifier: Modifier = Modifier,
    contentType: ContentType,
    viewModel: LaunchesViewModel = hiltViewModel()
) {
    viewModel.getUpcomingLaunches()

    val upcoming by viewModel.upcomingLaunchesLiveData.observeAsState(ApiResult.Pending)
    val previous = viewModel.previousLaunchesLiveData.collectAsLazyPagingItems()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val upcomingLazyListState = rememberLazyListState()
    val previousLazyListState = rememberLazyListState()

    when (contentType) {
        ContentType.DUAL_PANE -> LaunchesTwoPaneContent(
            modifier = modifier,
            uiState = uiState,
            upcoming = upcoming,
            previous = previous,
            upcomingLazyListState = upcomingLazyListState,
            previousLazyListState = previousLazyListState,
            openedLaunch = uiState.openedLaunch,
            retry = { viewModel.getUpcomingLaunches(CachePolicy.REFRESH) },
            navigateToDetail = { launch, pane -> viewModel.setOpenedLaunch(launch, pane) }
        )

        ContentType.SINGLE_PANE -> LaunchesSinglePaneContent(
            modifier = modifier,
            uiState = uiState,
            upcoming = upcoming,
            previous = previous,
            upcomingLazyListState = upcomingLazyListState,
            previousLazyListState = previousLazyListState,
            openedLaunch = uiState.openedLaunch,
            retry = { viewModel.getUpcomingLaunches(CachePolicy.REFRESH) },
            closeDetailScreen = { viewModel.closeDetailScreen() },
            navigateToDetail = { launch, pane -> viewModel.setOpenedLaunch(launch, pane) }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LaunchesTwoPaneContent(
    modifier: Modifier = Modifier,
    uiState: LaunchesUIState,
    upcoming: ApiResult<List<LaunchItem>>,
    previous: LazyPagingItems<LaunchItem>,
    upcomingLazyListState: LazyListState,
    previousLazyListState: LazyListState,
    openedLaunch: LaunchItem?,
    retry: () -> Unit,
    navigateToDetail: (LaunchItem, ContentType) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 2 })

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
            LaunchList(
                modifier = Modifier
                    .fillMaxSize(),
                pagerState = pagerState,
                contentType = ContentType.DUAL_PANE,
                upcoming = upcoming,
                previous = previous,
                upcomingLazyListState = upcomingLazyListState,
                previousLazyListState = previousLazyListState,
                openedLaunch = openedLaunch,
                retry = retry,
                navigateToDetail = navigateToDetail
            )
        }
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .weight(1f)
        ) {
            uiState.openedLaunch?.let {
                key(it.id) {
                    LaunchContainerScreen(
                        modifier = Modifier
                            .fillMaxSize(),
                        launch = it,
                        isFullscreen = false
                    )
                }
            } ?: Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Select a launch")
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LaunchesSinglePaneContent(
    modifier: Modifier = Modifier,
    uiState: LaunchesUIState,
    upcoming: ApiResult<List<LaunchItem>>,
    previous: LazyPagingItems<LaunchItem>,
    upcomingLazyListState: LazyListState,
    previousLazyListState: LazyListState,
    openedLaunch: LaunchItem?,
    retry: () -> Unit,
    closeDetailScreen: () -> Unit,
    navigateToDetail: (LaunchItem, ContentType) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 2 })

    AnimatedContent(
        targetState = uiState.isDetailOnlyOpen,
        label = "",
        transitionSpec = {
            if (targetState) {
                (slideInHorizontally { width -> width / 2 } + fadeIn())
                    .togetherWith(slideOutHorizontally { width -> -(width / 2) } + fadeOut())
            } else {
                (slideInHorizontally { width -> -(width / 2) } + fadeIn())
                    .togetherWith(slideOutHorizontally { width -> width / 2 } + fadeOut())
            }.using(SizeTransform(clip = false)).apply {
                targetContentZIndex = if (targetState) 1f else 0f
            }
        }
    ) {
        if (it && uiState.openedLaunch != null) {
            BackHandler {
                closeDetailScreen()
            }
            LaunchContainerScreen(
                modifier = modifier,
                launch = uiState.openedLaunch
            ) {
                closeDetailScreen()
            }
        } else {
            LaunchList(
                modifier = modifier,
                pagerState = pagerState,
                contentType = ContentType.SINGLE_PANE,
                upcoming = upcoming,
                previous = previous,
                upcomingLazyListState = upcomingLazyListState,
                previousLazyListState = previousLazyListState,
                openedLaunch = openedLaunch,
                retry = retry,
                navigateToDetail = navigateToDetail
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LaunchList(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    contentType: ContentType,
    upcoming: ApiResult<List<LaunchItem>>,
    previous: LazyPagingItems<LaunchItem>,
    upcomingLazyListState: LazyListState,
    previousLazyListState: LazyListState,
    openedLaunch: LaunchItem?,
    retry: () -> Unit,
    navigateToDetail: (LaunchItem, ContentType) -> Unit
) {
    val screens = listOf(
        PagerItem(label = "Upcoming", icon = R.drawable.ic_baseline_schedule_24) {
            UpcomingLaunchesScreen(
                upcoming = upcoming,
                contentType = contentType,
                listState = upcomingLazyListState,
                openedLaunch = openedLaunch,
                retry = retry
            ) {
                navigateToDetail(it, ContentType.SINGLE_PANE)
            }
        },
        PagerItem(label = "Past", icon = R.drawable.ic_history_black_24dp) {
            PreviousLaunchesScreen(
                previous = previous,
                contentType = contentType,
                listState = previousLazyListState,
                openedLaunch = openedLaunch
            ) {
                navigateToDetail(it, ContentType.SINGLE_PANE)
            }
        }
    )

    Scaffold(
        modifier = modifier,
        topBar = {
            SpaceXTabLayout(pagerState = pagerState, screens = screens)
        }
    ) { padding ->
        HorizontalPager(
            modifier = Modifier
                .padding(padding)
                .fillMaxHeight(),
            state = pagerState,
            beyondBoundsPageCount = 1,
            verticalAlignment = Alignment.Top,
            key = { screens[it].label }
        ) { page ->
            screens[page].screen()
        }
    }
}
