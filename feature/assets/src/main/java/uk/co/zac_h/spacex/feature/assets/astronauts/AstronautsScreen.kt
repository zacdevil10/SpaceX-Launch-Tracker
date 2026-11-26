package uk.co.zac_h.spacex.feature.assets.astronauts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.MutableStateFlow
import uk.co.zac_h.spacex.core.common.NetworkVerticalListContent
import uk.co.zac_h.spacex.core.ui.Astronaut
import uk.co.zac_h.spacex.core.ui.DevicePreviews
import uk.co.zac_h.spacex.core.ui.SpaceXTheme
import uk.co.zac_h.spacex.feature.assets.preview.AstronautsPreviewParameterProvider
import uk.co.zac_h.spacex.feature.assets.vehicles.VehicleItem

@Composable
fun AstronautsScreen(
    viewModel: AstronautViewModel = hiltViewModel(),
    state: LazyListState,
    selectedId: String?,
    onItemClick: (VehicleItem) -> Unit
) {
    val astronauts = viewModel.astronautLiveData.collectAsLazyPagingItems()

    AstronautsContent(
        astronauts = astronauts,
        state = state,
        selectedId = selectedId,
        onItemClick = onItemClick
    )
}

@Composable
fun AstronautsContent(
    modifier: Modifier = Modifier,
    astronauts: LazyPagingItems<AstronautItem>,
    state: LazyListState,
    selectedId: String?,
    onItemClick: (VehicleItem) -> Unit
) {
    NetworkVerticalListContent(
        modifier = modifier
            .fillMaxSize(),
        result = astronauts,
        state = state
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
                    // Details open in their own screen or pane, like every other asset.
                    expandable = false,
                    isSelected = selectedId == astronaut.id.toString()
                ) {
                    onItemClick(it)
                }
            }
        }
    }
}

@DevicePreviews
@Composable
fun AstronautContentSinglePanePreview(
    @PreviewParameter(AstronautsPreviewParameterProvider::class) astronauts: List<AstronautItem>
) {
    SpaceXTheme {
        AstronautsContent(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            astronauts = MutableStateFlow(PagingData.from(astronauts)).collectAsLazyPagingItems(),
            state = rememberLazyListState(),
            selectedId = null,
            onItemClick = {}
        )
    }
}

@DevicePreviews
@Composable
fun AstronautContentDualPanePreview(
    @PreviewParameter(AstronautsPreviewParameterProvider::class) astronauts: List<AstronautItem>
) {
    SpaceXTheme {
        AstronautsContent(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            astronauts = MutableStateFlow(PagingData.from(astronauts)).collectAsLazyPagingItems(),
            state = rememberLazyListState(),
            selectedId = astronauts[1].id.toString(),
            onItemClick = {}
        )
    }
}
