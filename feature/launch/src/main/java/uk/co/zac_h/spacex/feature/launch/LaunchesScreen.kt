package uk.co.zac_h.spacex.feature.launch

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import uk.co.zac_h.spacex.core.common.ContentType
import uk.co.zac_h.spacex.core.ui.component.SelectableContent
import uk.co.zac_h.spacex.core.ui.component.SpaceXTabLayout
import uk.co.zac_h.spacex.core.ui.component.SpaceXTabRowDefaults
import uk.co.zac_h.spacex.core.ui.component.Tab
import uk.co.zac_h.spacex.core.ui.component.TwoPane
import uk.co.zac_h.spacex.feature.launch.container.LaunchContainerScreen
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.dto.news.ArticleResponse

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun LaunchesScreen(
    contentType: ContentType,
    viewModel: LaunchesViewModel = hiltViewModel()
) {
    val upcoming by viewModel.upcomingLaunches.collectAsStateWithLifecycle(ApiResult.Pending)
    val previous = viewModel.previousLaunchesLiveData.collectAsLazyPagingItems()
    val articles = viewModel.articlesFlow.collectAsLazyPagingItems()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val upcomingLazyListState = rememberLazyListState()
    val previousLazyListState = rememberLazyListState()

    val scrollBehavior = SpaceXTabRowDefaults.pinnedScrollBehavior()

    val launchListPagerState = rememberPagerState(pageCount = { 2 })

    LaunchesContent(
        uiState = uiState,
        upcoming = upcoming,
        previous = previous,
        articles = articles,
        pagerState = launchListPagerState,
        upcomingLazyListState = upcomingLazyListState,
        previousLazyListState = previousLazyListState,
        openedLaunch = uiState.openedLaunch,
        retry = { viewModel.getUpcoming() },
        navigateToDetail = { launch, pane -> viewModel.setOpenedLaunch(launch, pane) },
        scrollBehavior = scrollBehavior,
        contentType = contentType,
        closeDetailScreen = { viewModel.closeDetailScreen() }
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LaunchesContent(
    contentType: ContentType,
    uiState: LaunchesUIState,
    upcoming: ApiResult<List<LaunchItem>>,
    previous: LazyPagingItems<LaunchItem>,
    articles: LazyPagingItems<ArticleResponse>,
    pagerState: PagerState,
    upcomingLazyListState: LazyListState,
    previousLazyListState: LazyListState,
    openedLaunch: LaunchItem?,
    retry: () -> Unit,
    closeDetailScreen: () -> Unit,
    navigateToDetail: (LaunchItem, ContentType) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TwoPane(
        modifier = Modifier
            .fillMaxSize(),
        first = {
            LaunchListScreen(
                modifier = Modifier
                    .fillMaxSize(),
                pagerState = pagerState,
                contentType = contentType,
                upcoming = upcoming,
                previous = previous,
                upcomingLazyListState = upcomingLazyListState,
                previousLazyListState = previousLazyListState,
                openedLaunch = openedLaunch,
                retry = retry,
                navigateToDetail = navigateToDetail,
                scrollBehavior = scrollBehavior
            )
        },
        second = {
            SelectableContent(
                value = uiState.openedLaunch,
                content = {
                    key(it.id) {
                        LaunchContainerScreen(
                            modifier = Modifier
                                .fillMaxSize(),
                            launch = it,
                            articles = articles,
                            isFullscreen = contentType == ContentType.SINGLE_PANE,
                            navigateUp = closeDetailScreen
                        )
                    }
                },
                label = when (pagerState.currentPage) {
                    0 -> if (upcoming is ApiResult.Pending) "" else "Select a launch"
                    1 -> if (previous.loadState.refresh is LoadState.Loading) "" else "Select a launch"
                    else -> "Select a launch"
                }
            )
        },
        isTablet = contentType == ContentType.DUAL_PANE,
        isDetailOpen = uiState.isDetailOnlyOpen,
        hasDetailContent = uiState.openedLaunch != null,
        closeDetailScreen = closeDetailScreen
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LaunchListScreen(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    contentType: ContentType,
    upcoming: ApiResult<List<LaunchItem>>,
    previous: LazyPagingItems<LaunchItem>,
    upcomingLazyListState: LazyListState,
    previousLazyListState: LazyListState,
    openedLaunch: LaunchItem?,
    retry: () -> Unit,
    navigateToDetail: (LaunchItem, ContentType) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val tabs = listOf(
        Tab(label = "Upcoming", icon = R.drawable.ic_baseline_schedule_24),
        Tab(label = "Past", icon = R.drawable.ic_history_black_24dp)
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
                .fillMaxHeight(),
            state = pagerState,
            beyondBoundsPageCount = 1,
            verticalAlignment = Alignment.Top,
            key = { tabs[it].label }
        ) { page ->
            when (page) {
                0 -> UpcomingLaunchesScreen(
                    upcoming = upcoming,
                    contentType = contentType,
                    listState = upcomingLazyListState,
                    openedLaunch = openedLaunch,
                    retry = retry,
                    onItemClick = { navigateToDetail(it, ContentType.SINGLE_PANE) }
                )
                1 -> PreviousLaunchesScreen(
                    previous = previous,
                    contentType = contentType,
                    listState = previousLazyListState,
                    openedLaunch = openedLaunch,
                    onItemClick = { navigateToDetail(it, ContentType.SINGLE_PANE) }
                )
            }
        }
    }
}
