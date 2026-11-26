package uk.co.zac_h.spacex.feature.launch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import uk.co.zac_h.spacex.core.common.NetworkContent
import uk.co.zac_h.spacex.core.ui.DevicePreviews
import uk.co.zac_h.spacex.core.ui.LaunchContainer
import uk.co.zac_h.spacex.core.ui.LaunchState
import uk.co.zac_h.spacex.core.ui.SpaceXTheme
import uk.co.zac_h.spacex.feature.launch.preview.LaunchesPreviewParameterProvider
import uk.co.zac_h.spacex.network.ApiResult

@Composable
fun UpcomingLaunchesScreen(
    modifier: Modifier = Modifier,
    upcoming: ApiResult<List<LaunchItem>>,
    detailPaneVisible: Boolean,
    listState: LazyListState,
    selectedLaunchId: String?,
    retry: () -> Unit,
    onItemClick: (LaunchItem) -> Unit,
    nextLaunchUpdate: NextLaunchUpdate = NextLaunchUpdate(),
    onRefreshNextLaunch: () -> Unit = {}
) {
    NetworkContent(
        modifier = modifier,
        result = upcoming,
        retry = { retry() }
    ) {
        LazyColumn(
            modifier = Modifier,
            state = listState
        ) {
            item(key = "next_launch_update") {
                NextLaunchUpdateRow(update = nextLaunchUpdate, onRefresh = onRefreshNextLaunch)
            }
            itemsIndexed(it) { position, launch ->
                LaunchContainer(
                    patch = launch.missionPatch,
                    missionName = launch.missionName,
                    date = launch.launchDate,
                    vehicle = launch.rocket,
                    reused = launch.rocket == "Falcon 9" && launch.firstStage?.firstOrNull()?.reused ?: false,
                    landingPad = launch.firstStage?.firstOrNull()?.landingLocation?.let {
                        if (launch.rocket != "Falcon 9" && it == "N/A") null else it
                    },
                    launchSite = launch.launchLocation,
                    description = launch.description,
                    dateUnix = launch.launchDateUnix,
                    state = when {
                        position != 0 -> LaunchState.COMPACT
                        detailPaneVisible -> LaunchState.EXPANDED
                        else -> LaunchState.FULL
                    },
                    isSelected = selectedLaunchId == launch.id
                ) {
                    onItemClick(launch)
                }
            }
        }
    }
}

@DevicePreviews
@Composable
fun UpcomingLaunchesScreenPreview(
    @PreviewParameter(LaunchesPreviewParameterProvider::class) launchList: List<LaunchItem>
) {
    SpaceXTheme {
        UpcomingLaunchesScreen(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),
            upcoming = ApiResult.Success(launchList),
            detailPaneVisible = false,
            listState = rememberLazyListState(),
            selectedLaunchId = null,
            retry = {},
            onItemClick = {}
        )
    }
}

@DevicePreviews
@Composable
fun UpcomingLaunchesPendingScreenPreview() {
    SpaceXTheme {
        UpcomingLaunchesScreen(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
            upcoming = ApiResult.Pending,
            detailPaneVisible = false,
            listState = rememberLazyListState(),
            selectedLaunchId = null,
            retry = {},
            onItemClick = {}
        )
    }
}

@DevicePreviews
@Composable
fun UpcomingLaunchesFailureScreenPreview() {
    SpaceXTheme {
        UpcomingLaunchesScreen(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
            upcoming = ApiResult.Failure(Exception()),
            detailPaneVisible = false,
            listState = rememberLazyListState(),
            selectedLaunchId = null,
            retry = {},
            onItemClick = {}
        )
    }
}
