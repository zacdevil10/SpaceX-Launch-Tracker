package uk.co.zac_h.spacex.feature.launch

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import uk.co.zac_h.spacex.core.common.ContentType
import uk.co.zac_h.spacex.core.ui.component.SpaceXTabLayout
import uk.co.zac_h.spacex.core.ui.component.SpaceXTabRowDefaults
import uk.co.zac_h.spacex.core.ui.component.Tab
import uk.co.zac_h.spacex.network.ApiResult

/**
 * The upcoming and past launch lists.
 *
 * @param selectedLaunchId the launch open in the detail pane, highlighted on wide screens.
 * @param onLaunchClick opens a launch's details.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun LaunchesScreen(
    contentType: ContentType,
    selectedLaunchId: String?,
    onLaunchClick: (launchId: String) -> Unit,
    viewModel: LaunchesViewModel = hiltViewModel()
) {
    val upcoming by viewModel.upcomingLaunches.collectAsStateWithLifecycle(ApiResult.Pending)
    val nextLaunchUpdate by viewModel.nextLaunchUpdate.collectAsStateWithLifecycle(NextLaunchUpdate())
    val previous = viewModel.previousLaunchesLiveData.collectAsLazyPagingItems()

    val upcomingLazyListState = rememberLazyListState()
    val previousLazyListState = rememberLazyListState()

    val scrollBehavior = SpaceXTabRowDefaults.pinnedScrollBehavior()

    // Keep the next launch current only while this screen is on screen.
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.keepNextLaunchFresh()
        }
    }

    val launchListPagerState = rememberPagerState(pageCount = { 2 })

    LaunchListScreen(
        modifier = Modifier.fillMaxSize(),
        pagerState = launchListPagerState,
        contentType = contentType,
        upcoming = upcoming,
        previous = previous,
        upcomingLazyListState = upcomingLazyListState,
        previousLazyListState = previousLazyListState,
        selectedLaunchId = selectedLaunchId,
        retry = { viewModel.getUpcoming(force = true) },
        onLaunchClick = { onLaunchClick(it.id) },
        scrollBehavior = scrollBehavior,
        nextLaunchUpdate = nextLaunchUpdate,
        onRefreshNextLaunch = { viewModel.refreshNextLaunch() }
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
    selectedLaunchId: String?,
    retry: () -> Unit,
    onLaunchClick: (LaunchItem) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    nextLaunchUpdate: NextLaunchUpdate = NextLaunchUpdate(),
    onRefreshNextLaunch: () -> Unit = {}
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
        },
        contentWindowInsets = WindowInsets.ime
    ) { padding ->
        HorizontalPager(
            modifier = Modifier
                .padding(padding)
                .fillMaxHeight(),
            state = pagerState,
            beyondViewportPageCount = 1,
            verticalAlignment = Alignment.Top,
            key = { tabs[it].label }
        ) { page ->
            when (page) {
                0 -> UpcomingLaunchesScreen(
                    upcoming = upcoming,
                    contentType = contentType,
                    listState = upcomingLazyListState,
                    selectedLaunchId = selectedLaunchId,
                    retry = retry,
                    onItemClick = onLaunchClick,
                    nextLaunchUpdate = nextLaunchUpdate,
                    onRefreshNextLaunch = onRefreshNextLaunch
                )
                1 -> PreviousLaunchesScreen(
                    previous = previous,
                    contentType = contentType,
                    listState = previousLazyListState,
                    selectedLaunchId = selectedLaunchId,
                    onItemClick = onLaunchClick
                )
            }
        }
    }
}
