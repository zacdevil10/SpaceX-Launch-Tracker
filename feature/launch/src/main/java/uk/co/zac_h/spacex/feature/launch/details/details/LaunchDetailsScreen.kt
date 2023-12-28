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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import uk.co.zac_h.spacex.core.ui.ComponentPreviews
import uk.co.zac_h.spacex.core.ui.Countdown
import uk.co.zac_h.spacex.core.ui.DevicePreviews
import uk.co.zac_h.spacex.core.ui.LabelValue
import uk.co.zac_h.spacex.core.ui.Launch
import uk.co.zac_h.spacex.core.ui.SpaceXTheme
import uk.co.zac_h.spacex.feature.launch.LaunchItem
import uk.co.zac_h.spacex.feature.launch.R
import uk.co.zac_h.spacex.feature.launch.VideoItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaunchDetailsScreen(
    modifier: Modifier = Modifier,
    launch: LaunchItem
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        Countdown(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp, bottom = 8.dp),
            future = launch.launchDateUnix,
            onFinishLabel = "Watch Live"
        )
        Launch(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
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
                    .padding(bottom = 16.dp),
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
                    .padding(bottom = 16.dp),
                description = launch.description,
                type = launch.type,
                orbit = launch.orbit
            )
        }

        if (!launch.webcast.isNullOrEmpty()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentPadding = PaddingValues(end = 16.dp),
            ) {
                items(launch.webcast) {
                    LaunchDetailsVideo(
                        modifier = Modifier
                            .fillParentMaxWidth(0.9f)
                            .padding(start = 16.dp),
                        videoItem = it
                    )
                }
            }
        }

        if (launch.upcoming && !launch.webcastLive) {
            OutlinedButton(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
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
                    .padding(bottom = 16.dp)
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
                    .padding(bottom = 16.dp)
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
@ComponentPreviews
@Composable
fun LaunchDetailsScreenPreview() {
    SpaceXTheme {
        LaunchDetailsScreen(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            launch = LaunchItem(
                id = "0098c032-73de-4c6f-8d73-5d68b9a12fdf",
                upcoming = true,
                missionPatch = null,
                missionName = "OTV-7 (X-37B) (USSF-52)",
                rocket = "Falcon Heavy",
                launchDate = "29 Dec 23 - 01:07",
                launchDateUnix = System.currentTimeMillis() + 10000L,
                launchLocation = "Launch Location",
                launchLocationMap = null,
                launchLocationMapUrl = null,
                status = LaunchItem.Status(
                    type = 1,
                    name = "Go for Launch",
                    description = "Current T-0 confirmed by official or reliable sources."
                ),
                holdReason = "Hold Reason",
                failReason = "Fail Reason",
                description = "It is the seventh flight of the X-37B program. United States Air Force Orbital Test Vehicle is built by Boeing. It's an uncrewed 5000 kg, 8.8 m-long reusable mini-spaceplane capable of autonomous re-entry and landing.",
                type = "Government/Top Secret",
                orbit = "Elliptical Orbit",
                webcast = listOf(
                    VideoItem(
                        title = "Watch live: SpaceX Falcon Heavy launches secretive X-37B military spaceplane",
                        description = "Watch live coverage as SpaceX launches a Falcon Heavy rocket carrying the U.S. military's secretive X-37B spaceplane. Liftoff from historic launch pad 39A at...",
                        imageUrl = null,
                        url = "https://www.youtube.com/watch?v=wnfddhDuWDE",
                        source = "Youtube"
                    ),
                    VideoItem(
                        title = "Livestream on X",
                        description = "This browser is no longer supported.\\n\\nPlease switch to a supported browser to continue using twitter.com. You can see a list of supported browsers in our Help Center.",
                        imageUrl = null,
                        url = "https://twitter.com/i/broadcasts/1ynKOyeDmrwJR",
                        source = "Twitter"
                    )
                ),
                webcastLive = true,
                firstStage = emptyList(),
                crew = emptyList()
            )
        )
    }
}