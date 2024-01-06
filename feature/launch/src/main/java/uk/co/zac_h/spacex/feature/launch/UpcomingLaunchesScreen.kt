package uk.co.zac_h.spacex.feature.launch

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import uk.co.zac_h.spacex.core.common.NetworkContent
import uk.co.zac_h.spacex.core.common.ContentType
import uk.co.zac_h.spacex.core.ui.LaunchContainer
import uk.co.zac_h.spacex.core.ui.LaunchState
import uk.co.zac_h.spacex.network.ApiResult

@Composable
fun UpcomingLaunchesScreen(
    modifier: Modifier = Modifier,
    upcoming: ApiResult<List<LaunchItem>>,
    contentType: ContentType,
    listState: LazyListState,
    openedLaunch: LaunchItem?,
    retry: () -> Unit,
    onItemClick: (LaunchItem) -> Unit
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
            itemsIndexed(it.result) { position, launch ->
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
                    state = when (contentType) {
                        ContentType.SINGLE_PANE -> {
                            if (position == 0) LaunchState.FULL else LaunchState.COMPACT
                        }

                        ContentType.DUAL_PANE -> {
                            if (position == 0) LaunchState.EXPANDED else LaunchState.COMPACT
                        }
                    },
                    isOpened = openedLaunch?.id == launch.id
                ) {
                    onItemClick(launch)
                }
            }
        }
    }
}