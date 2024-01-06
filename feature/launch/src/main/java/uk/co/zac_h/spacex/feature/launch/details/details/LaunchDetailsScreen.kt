package uk.co.zac_h.spacex.feature.launch.details.details

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import uk.co.zac_h.spacex.core.ui.Countdown
import uk.co.zac_h.spacex.core.ui.DevicePreviews
import uk.co.zac_h.spacex.core.ui.LabelValue
import uk.co.zac_h.spacex.core.ui.LaunchContainer
import uk.co.zac_h.spacex.core.ui.SpaceXTheme
import uk.co.zac_h.spacex.feature.launch.LaunchItem
import uk.co.zac_h.spacex.feature.launch.LaunchPreviewParameterProvider
import uk.co.zac_h.spacex.feature.launch.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaunchDetailsScreen(
    modifier: Modifier = Modifier,
    launch: LaunchItem,
    isFullscreen: Boolean = true,
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(bottom = 16.dp)
    ) {
        if (isFullscreen && (launch.webcastLive
                    || (launch.launchDateUnix?.minus(System.currentTimeMillis()) ?: 0) > 0)
        ) {
            Countdown(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp),
                future = launch.launchDateUnix,
                onFinishLabel = "Watch Live"
            )
        }
        if (isFullscreen) LaunchContainer(
            modifier = Modifier,
            patch = launch.missionPatch,
            missionName = launch.missionName,
            date = launch.launchDate,
            vehicle = launch.rocket,
            reused = launch.rocket == "Falcon 9" && launch.firstStage?.firstOrNull()?.reused ?: false,
            landingPad = launch.firstStage?.firstOrNull()?.landingLocation?.let {
                if (launch.rocket != "Falcon 9" && it == "N/A") null else it
            }
        )
        if (!launch.holdReason.isNullOrEmpty()) {
            LaunchDetailsHold(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp),
                description = launch.holdReason
            )
        }
        if (!launch.description.isNullOrEmpty()
            || !launch.type.isNullOrEmpty()
            || !launch.orbit.isNullOrEmpty()
        ) {
            LaunchDetailsMission(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp),
                description = launch.description,
                type = launch.type,
                orbit = launch.orbit
            )
        }

        if (!launch.webcast.isNullOrEmpty()) {
            if (launch.webcast.size > 1) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    contentPadding = PaddingValues(end = 16.dp),
                ) {
                    items(
                        launch.webcast,
                        key = { it.url }
                    ) {
                        LaunchDetailsVideo(
                            modifier = Modifier
                                .fillParentMaxWidth(
                                    0.9f
                                )
                                .padding(start = 16.dp),
                            videoItem = it
                        )
                    }
                }
            } else {
                LaunchDetailsVideo(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp),
                    videoItem = launch.webcast.first()
                )
            }
        }

        if (launch.upcoming && !launch.webcastLive) {
            OutlinedButton(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                onClick = {
                    createEvent(context, launch)
                }
            ) {
                Image(
                    modifier = Modifier.padding(end = 4.dp),
                    painter = painterResource(id = R.drawable.ic_calendar_plus),
                    contentDescription = ""
                )
                Text(
                    text = stringResource(id = R.string.launches_event_button_label)
                )
            }
        }

        launch.status.type?.let {
            OutlinedCard(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    launch.status.name?.let { name ->
                        Text(
                            modifier = Modifier.padding(bottom = 8.dp),
                            text = name,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    (if (launch.status.type == 4) launch.failReason else launch.status.description)?.let { description ->
                        Text(
                            modifier = Modifier.padding(bottom = 8.dp),
                            text = description
                        )
                    }
                }
            }
        }

        launch.launchLocation?.let {
            OutlinedCard(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                onClick = {
                    launch.launchLocationMapUrl?.let { it1 -> uriHandler.openUri(it1) }
                }
            ) {
                Column {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CardDefaults.outlinedShape),
                        model = launch.launchLocationMap,
                        contentDescription = "",
                        contentScale = ContentScale.Crop
                    )
                    LabelValue(
                        modifier = Modifier.padding(16.dp),
                        label = stringResource(id = R.string.launches_launch_location_label),
                        value = it
                    )
                }
            }
        }
    }
}

internal fun createEvent(context: Context, launch: LaunchItem) {
    val calendarIntent = Intent(Intent.ACTION_INSERT).apply {
        data = CalendarContract.Events.CONTENT_URI
        putExtra(
            CalendarContract.EXTRA_EVENT_BEGIN_TIME,
            launch.launchDateUnix
        )
        putExtra(
            CalendarContract.EXTRA_EVENT_END_TIME,
            launch.launchDateUnix
        )
        putExtra(
            CalendarContract.Events.TITLE,
            "${launch.missionName} - SpaceX"
        )
    }
    try {
        context.startActivity(calendarIntent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "No supported calendar apps found.", Toast.LENGTH_SHORT).show()
    }
}

@DevicePreviews
@Composable
fun LaunchDetailsScreenPreview(
    @PreviewParameter(LaunchPreviewParameterProvider::class) launches: LaunchItem
) {
    SpaceXTheme {
        LaunchDetailsScreen(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            launch = launches
        )
    }
}