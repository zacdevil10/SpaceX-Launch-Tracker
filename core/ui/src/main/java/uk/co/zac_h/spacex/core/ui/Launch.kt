package uk.co.zac_h.spacex.core.ui

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Launch(
    modifier: Modifier = Modifier,
    patch: Any? = null,
    missionName: String? = null,
    date: String? = null,
    vehicle: String? = null,
    reused: Boolean = false,
    landingPad: String? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        AsyncImage(
            modifier = Modifier
                .defaultMinSize(64.dp, 64.dp)
                .fillMaxHeight()
                .width(64.dp),
            model = patch,
            contentDescription = "",
            placeholder = painterResource(id = R.drawable.ic_mission_patch),
            error = painterResource(id = R.drawable.ic_mission_patch),
            fallback = painterResource(id = R.drawable.ic_mission_patch),
            alignment = Alignment.Center
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            missionName?.let {
                Text(
                    modifier = Modifier.basicMarquee(),
                    text = it,
                    maxLines = 1,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            date?.let {
                Text(
                    text = it,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Column(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.End
        ) {
            vehicle?.let {
                Text(
                    text = it,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            if (reused) Text(
                text = stringResource(id = R.string.reused),
                maxLines = 1,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary
            )
            landingPad?.let {
                Text(
                    text = it,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandedLaunch(
    modifier: Modifier = Modifier,
    patch: Any? = null,
    missionName: String? = null,
    date: String? = null,
    vehicle: String? = null,
    reused: Boolean = false,
    landingPad: String? = null,
    launchSite: String? = null,
    description: String? = null,
    dateUnix: Long?,
    onClick: () -> Unit
) {
    OutlinedCard(modifier = modifier, onClick = { onClick() }) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Countdown(
                modifier = Modifier.padding(bottom = 8.dp),
                dateUnix,
                "Watch Live"
            )
            Launch(
                modifier = Modifier.padding(bottom = 8.dp),
                missionName = missionName,
                patch = patch,
                date = date,
                vehicle = vehicle,
                reused = reused,
                landingPad = landingPad
            )
            launchSite?.let {
                LabelValue(
                    modifier = Modifier.padding(bottom = 8.dp),
                    label = stringResource(id = R.string.launch_site_label),
                    value = it
                )
            }
            if (description != null) {
                Text(text = description)
            }
        }
    }
}

@Preview(name = "Light Mode")
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LaunchPreview() {
    SpaceXTheme {
        Launch(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            missionName = "Nusantara Satu (PSN-6) / GTO-1 / Beresheet",
            date = "14 Oct 2019 - 12:35",
            vehicle = "Falcon 9",
            reused = true,
            landingPad = "JRTI"
        )
    }
}

@Preview(name = "Light Mode")
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ExpandedLaunchPreview(
    @PreviewParameter(LoremIpsum::class) text: String
) {
    SpaceXTheme {
        ExpandedLaunch(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            missionName = "Nusantara Satu (PSN-6) / GTO-1 / Beresheet",
            date = "14 Oct 2019 - 12:35",
            vehicle = "Falcon 9",
            reused = true,
            landingPad = "JRTI",
            launchSite = "CCAFS SLC 40",
            description = text.split("\n\n").first(),
            dateUnix = 0
        ) {}
    }
}

@Preview(name = "Light Mode")
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ExpandedLaunchCountdownPreview(
    @PreviewParameter(LoremIpsum::class) text: String
) {
    SpaceXTheme {
        ExpandedLaunch(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            missionName = "Nusantara Satu (PSN-6) / GTO-1 / Beresheet",
            date = "14 Oct 2019 - 12:35",
            vehicle = "Falcon 9",
            reused = true,
            landingPad = "JRTI",
            launchSite = "CCAFS SLC 40",
            description = text.split("\n\n").first(),
            dateUnix = 0
        ) {}
    }
}
