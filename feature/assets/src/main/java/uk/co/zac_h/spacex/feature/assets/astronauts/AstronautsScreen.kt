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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.MutableStateFlow
import uk.co.zac_h.spacex.core.common.ContentType
import uk.co.zac_h.spacex.core.common.NetworkContent
import uk.co.zac_h.spacex.core.ui.Astronaut
import uk.co.zac_h.spacex.core.ui.DevicePreviews
import uk.co.zac_h.spacex.core.ui.SpaceXTheme
import uk.co.zac_h.spacex.feature.assets.preview.AstronautsPreviewParameterProvider
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
        openedAsset = openedAsset,
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
    openedAsset: VehicleItem?,
    onItemClick: (VehicleItem) -> Unit
) {
    NetworkContent(
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
                    status = astronaut.status.status,
                    firstFlight = astronaut.firstFlight,
                    description = astronaut.description,
                    expanded = contentType == ContentType.SINGLE_PANE && expanded == index,
                    isFullscreen = contentType == ContentType.SINGLE_PANE,
                    isSelected = openedAsset?.id == astronaut.id && contentType == ContentType.DUAL_PANE
                ) {
                    setExpanded(if (expanded != index) index else -1)
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
            contentType = ContentType.SINGLE_PANE,
            state = rememberLazyListState(),
            expanded = 1,
            setExpanded = {},
            openedAsset = null,
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
            contentType = ContentType.DUAL_PANE,
            state = rememberLazyListState(),
            expanded = 1,
            setExpanded = {},
            openedAsset = astronauts[1],
            onItemClick = {}
        )
    }
}
