package uk.co.zac_h.spacex.feature.launch.container.crew

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import uk.co.zac_h.spacex.core.ui.Astronaut
import uk.co.zac_h.spacex.core.ui.ComponentPreviews
import uk.co.zac_h.spacex.core.ui.DevicePreviews
import uk.co.zac_h.spacex.core.ui.SpaceXTheme
import uk.co.zac_h.spacex.feature.launch.CrewItem
import uk.co.zac_h.spacex.feature.launch.LaunchItem
import uk.co.zac_h.spacex.feature.launch.preview.LaunchPreviewParameterProvider

@Composable
fun LaunchCrewScreen(
    modifier: Modifier = Modifier,
    crew: List<CrewItem>,
    expandedPosition: Int,
    setExpandedPosition: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(top = 8.dp)
    ) {
        itemsIndexed(
            crew,
            key = { _, crew -> crew.id }
        ) { index, it ->
            Astronaut(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                image = it.image,
                role = it.role,
                title = it.name,
                agency = it.agency,
                status = it.status.status,
                firstFlight = it.firstFlight,
                description = it.bio,
                expanded = expandedPosition == index
            ) {
                setExpandedPosition(
                    if (expandedPosition != index) index else -1
                )
            }
        }
    }
}

@DevicePreviews
@ComponentPreviews
@Composable
fun LaunchCrewScreenPreview(
    @PreviewParameter(LaunchPreviewParameterProvider::class) launches: LaunchItem
) {
    SpaceXTheme {
        LaunchCrewScreen(
            crew = launches.crew!!,
            expandedPosition = 0,
            setExpandedPosition = {}
        )
    }
}