package uk.co.zac_h.spacex.feature.launch.container.cores

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import uk.co.zac_h.spacex.core.common.Header
import uk.co.zac_h.spacex.core.common.recyclerview.RecyclerViewItem
import uk.co.zac_h.spacex.core.ui.DevicePreviews
import uk.co.zac_h.spacex.core.ui.LabelValue
import uk.co.zac_h.spacex.core.ui.SpaceXTheme
import uk.co.zac_h.spacex.feature.launch.FirstStageItem
import uk.co.zac_h.spacex.feature.launch.LaunchItem
import uk.co.zac_h.spacex.feature.launch.preview.LaunchPreviewParameterProvider

@Composable
fun LaunchCoresScreen(
    modifier: Modifier = Modifier,
    cores: List<RecyclerViewItem>
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(
            cores,
            key = {
                when (it) {
                    is Header -> it.title
                    is FirstStageItem -> it.id
                    else -> error("Invalid type: $it")
                }
            }
        ) {
            when (it) {
                is Header -> {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        text = it.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                is FirstStageItem -> {
                    OutlinedCard(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                        ) {
                            Text(
                                text = it.serial,
                                style = MaterialTheme.typography.headlineMedium
                            )
                            Divider(
                                modifier = Modifier
                                    .padding(top = 8.dp)
                            )
                            it.landingDescription?.let { description ->
                                Text(
                                    modifier = Modifier
                                        .padding(top = 8.dp),
                                    text = description
                                )
                            }
                            if (it.landingAttempt) {
                                it.landingLocationFull?.let { location ->
                                    LabelValue(
                                        modifier = Modifier
                                            .padding(top = 8.dp),
                                        label = "Landing Location",
                                        value = location
                                    )
                                }
                                if (it.landingType == "ASDS" || it.landingType == "RTLS") {
                                    LabelValue(
                                        modifier = Modifier
                                            .padding(top = 8.dp),
                                        label = "Landing Type",
                                        value = it.landingType
                                    )
                                }
                            }
                            it.previousFlight?.let { previousFlight ->
                                LabelValue(
                                    modifier = Modifier
                                        .padding(top = 8.dp),
                                    label = "Preview Flight",
                                    value = previousFlight.replace(" | ", "\n")
                                )
                            }
                            it.turnAroundTimeDays?.let { days ->
                                LabelValue(
                                    modifier = Modifier
                                        .padding(top = 8.dp),
                                    label = "Turn Around Time",
                                    value = "$days days"
                                )
                            }
                            it.totalFlights?.let { flights ->
                                LabelValue(
                                    modifier = Modifier
                                        .padding(top = 8.dp),
                                    label = "Total Flights",
                                    value = flights.toString()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@DevicePreviews
@Composable
fun LaunchCoresScreenPreview(
    @PreviewParameter(LaunchPreviewParameterProvider::class) launch: LaunchItem
) {
    SpaceXTheme {
        LaunchCoresScreen(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            cores = launch.firstStage?.let { firstStageList ->
                if (firstStageList.size > 1) {
                    firstStageList.groupBy { firstStageItem ->
                        firstStageItem.type
                    }.flatMap { firstStageItem ->
                        listOf(Header(firstStageItem.key.type)) + firstStageItem.value
                    }
                } else {
                    firstStageList
                }
            } ?: emptyList()
        )
    }
}
