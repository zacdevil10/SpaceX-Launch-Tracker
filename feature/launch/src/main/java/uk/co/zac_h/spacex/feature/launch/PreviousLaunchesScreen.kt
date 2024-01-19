package uk.co.zac_h.spacex.feature.launch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.MutableStateFlow
import uk.co.zac_h.spacex.core.common.ContentType
import uk.co.zac_h.spacex.core.common.NetworkContent
import uk.co.zac_h.spacex.core.ui.DevicePreviews
import uk.co.zac_h.spacex.core.ui.LaunchContainer
import uk.co.zac_h.spacex.core.ui.SpaceXTheme
import uk.co.zac_h.spacex.feature.launch.preview.LaunchesPreviewParameterProvider

@Composable
fun PreviousLaunchesScreen(
    modifier: Modifier = Modifier,
    previous: LazyPagingItems<LaunchItem>,
    contentType: ContentType,
    listState: LazyListState,
    openedLaunch: LaunchItem?,
    onItemClick: (LaunchItem) -> Unit
) {
    NetworkContent(
        modifier = modifier,
        result = previous,
        state = listState
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        items(
            count = previous.itemCount,
            key = previous.itemKey { it.id }
        ) { index ->
            val launch = previous[index]

            launch?.let {
                LaunchContainer(
                    patch = launch.missionPatch,
                    missionName = launch.missionName,
                    date = launch.launchDate,
                    vehicle = launch.rocket,
                    reused = launch.rocket == "Falcon 9" && launch.firstStage?.firstOrNull()?.reused ?: false,
                    landingPad = launch.firstStage?.firstOrNull()?.landingLocation?.let {
                        if (launch.rocket != "Falcon 9" && it == "N/A") null else it
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
fun PreviousLaunchesScreenPreview(
    @PreviewParameter(LaunchesPreviewParameterProvider::class) launchList: List<LaunchItem>
) {
    SpaceXTheme {
        PreviousLaunchesScreen(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
            previous = MutableStateFlow(PagingData.from(launchList)).collectAsLazyPagingItems(),
            contentType = ContentType.SINGLE_PANE,
            listState = rememberLazyListState(),
            openedLaunch = null
        ) {

        }
    }
}

@DevicePreviews
@Composable
fun PreviousLaunchesPendingScreenPreview(
    @PreviewParameter(LaunchesPreviewParameterProvider::class) launchList: List<LaunchItem>
) {
    SpaceXTheme {
        PreviousLaunchesScreen(
            modifier = Modifier
                .fillMaxSize(),
            previous = MutableStateFlow<PagingData<LaunchItem>>(
                PagingData.empty(
                    sourceLoadStates = LoadStates(
                        refresh = LoadState.Loading,
                        prepend = LoadState.Loading,
                        append = LoadState.Loading
                    )
                )
            ).collectAsLazyPagingItems(),
            contentType = ContentType.SINGLE_PANE,
            listState = rememberLazyListState(),
            openedLaunch = null
        ) {

        }
    }
}

@DevicePreviews
@Composable
fun PreviousLaunchesErrorScreenPreview(
    @PreviewParameter(LaunchesPreviewParameterProvider::class) launchList: List<LaunchItem>
) {
    SpaceXTheme {
        PreviousLaunchesScreen(
            modifier = Modifier
                .fillMaxSize(),
            previous = MutableStateFlow<PagingData<LaunchItem>>(
                PagingData.empty(
                    sourceLoadStates = LoadStates(
                        refresh = LoadState.Error(Exception()),
                        prepend = LoadState.Loading,
                        append = LoadState.Loading
                    )
                )
            ).collectAsLazyPagingItems(),
            contentType = ContentType.SINGLE_PANE,
            listState = rememberLazyListState(),
            openedLaunch = null
        ) {

        }
    }
}
