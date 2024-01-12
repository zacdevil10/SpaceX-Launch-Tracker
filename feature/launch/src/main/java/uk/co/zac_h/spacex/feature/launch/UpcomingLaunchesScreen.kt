package uk.co.zac_h.spacex.feature.launch

import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import uk.co.zac_h.spacex.core.common.ContentType
import uk.co.zac_h.spacex.core.common.NetworkContent
import uk.co.zac_h.spacex.core.ui.DevicePreviews
import uk.co.zac_h.spacex.core.ui.LaunchContainer
import uk.co.zac_h.spacex.core.ui.LaunchState
import uk.co.zac_h.spacex.core.ui.SpaceXTheme
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
                    state = when (contentType) {
                        ContentType.SINGLE_PANE -> {
                            if (position == 0) LaunchState.FULL else LaunchState.COMPACT
                        }

                        ContentType.DUAL_PANE -> {
                            if (position == 0) LaunchState.EXPANDED else LaunchState.COMPACT
                        }
                    },
                    isSelected = openedLaunch?.id == launch.id && contentType == ContentType.DUAL_PANE
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
    @PreviewParameter(LaunchListPreviewParameterProvider::class) launchList: List<LaunchItem>
) {
    SpaceXTheme {
        UpcomingLaunchesScreen(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),
            upcoming = ApiResult.Success(launchList),
            contentType = ContentType.SINGLE_PANE,
            listState = rememberLazyListState(),
            openedLaunch = null,
            retry = {},
            onItemClick = {}
        )
    }
}