package uk.co.zac_h.spacex.feature.assets.astronauts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import uk.co.zac_h.spacex.core.common.ContentType
import uk.co.zac_h.spacex.core.ui.Astronaut
import uk.co.zac_h.spacex.feature.assets.vehicles.VehicleItem

@Composable
fun AstronautsScreen(
    contentType: ContentType,
    viewModel: AstronautViewModel = hiltViewModel(),
    state: LazyListState,
    expanded: Int,
    setExpanded: (Int) -> Unit,
    openedAsset: VehicleItem?,
    onItemClick: (VehicleItem) -> Unit
) {
    val astronauts = viewModel.astronautLiveData.collectAsLazyPagingItems()

    AstronautsContent(
        astronauts = astronauts,
        contentType = contentType,
        state = state,
        expanded = expanded,
        setExpanded = setExpanded,
        onItemClick = onItemClick
    )
}

@Composable
fun AstronautsContent(
    modifier: Modifier = Modifier,
    astronauts: LazyPagingItems<AstronautItem>,
    contentType: ContentType,
    state: LazyListState,
    expanded: Int,
    setExpanded: (Int) -> Unit,
    onItemClick: (VehicleItem) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        if (astronauts.loadState.refresh is LoadState.Loading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
            )
        } else {
            LazyColumn(
                state = state,
                contentPadding = PaddingValues(top = 8.dp)
            ) {
                items(
                    count = astronauts.itemCount,
                    key = astronauts.itemKey { it.id }
                ) { index ->
                    val astronaut = astronauts[index]

                    astronaut?.let {
                        Astronaut(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            image = astronaut.imageUrl,
                            role = astronaut.nationality,
                            title = astronaut.title,
                            agency = astronaut.agency,
                            status = astronaut.status.status,
                            firstFlight = astronaut.firstFlight,
                            description = astronaut.description,
                            expanded = contentType == ContentType.SINGLE_PANE && expanded == index,
                            isFullscreen = contentType == ContentType.SINGLE_PANE
                        ) {
                            setExpanded(if (expanded != index) index else -1)
                            onItemClick(it)
                        }
                    }
                }

                item {
                    when (astronauts.loadState.append) {
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
                                onClick = { astronauts.retry() }
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
}