package uk.co.zac_h.spacex.feature.launch

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import uk.co.zac_h.spacex.core.ui.LaunchContainer

@Composable
fun PreviousLaunchesScreen(
    modifier: Modifier = Modifier,
    previous: LazyPagingItems<LaunchItem>,
    listState: LazyListState,
    openedLaunch: LaunchItem?,
    onItemClick: (LaunchItem) -> Unit
) {
    if (previous.loadState.refresh is LoadState.Loading) {
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
        )
    } else {
        LazyColumn(
            modifier = modifier,
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
                        isOpened = openedLaunch?.id == launch.id
                    ) {
                        onItemClick(launch)
                    }
                }
            }

            item {
                when (previous.loadState.append) {
                    is LoadState.Loading -> LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    is LoadState.Error -> Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Button(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp),
                            onClick = { previous.retry() }
                        ) {
                            Text(text = "Retry")
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}
