package uk.co.zac_h.spacex.core.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import uk.co.zac_h.spacex.core.ui.component.DynamicAsyncImage

@Composable
fun LaunchContainer(
    modifier: Modifier = Modifier,
    patch: Any? = null,
    missionName: String? = null,
    date: String? = null,
    vehicle: String? = null,
    reused: Boolean = false,
    landingPad: String? = null,
    launchSite: String? = null,
    description: String? = null,
    dateUnix: Long? = null,
    state: LaunchState = LaunchState.COMPACT,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val animatedCardBackgroundColor = animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            MaterialTheme.colorScheme.background
        },
        animationSpec = tween(200, 0, LinearEasing),
        label = ""
    )

    when (state) {
        LaunchState.COMPACT -> Launch(
            modifier = modifier
                .clickable(enabled = onClick != null) { onClick?.invoke() }
                .background(
                    color = animatedCardBackgroundColor.value,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(16.dp),
            patch = patch,
            missionName = missionName,
            date = date,
            vehicle = vehicle,
            reused = reused,
            landingPad = landingPad
        )

        else -> ExpandedLaunch(
            modifier = modifier
                .padding(16.dp),
            patch = patch,
            missionName = missionName,
            date = date,
            vehicle = vehicle,
            reused = reused,
            landingPad = landingPad,
            launchSite = launchSite,
            description = description,
            dateUnix = dateUnix,
            state = state,
            backgroundColor = animatedCardBackgroundColor,
            onClick = onClick
        )
    }
}

enum class LaunchState {
    COMPACT, EXPANDED, FULL
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun Launch(
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
            .height(IntrinsicSize.Min),
    ) {
        DynamicAsyncImage(
            modifier = Modifier
                .defaultMinSize(64.dp, 64.dp)
                .fillMaxHeight()
                .width(64.dp),
            model = patch,
            contentDescription = "",
            placeholder = painterResource(id = R.drawable.ic_mission_patch),
            tint = MaterialTheme.colorScheme.primary
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
                    style = MaterialTheme.typography.labelLarge,
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
            ) else Spacer(modifier = Modifier)
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
internal fun ExpandedLaunch(
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
    state: LaunchState,
    backgroundColor: State<Color>,
    onClick: (() -> Unit)?
) {
    OutlinedCard(
        modifier = modifier,
        colors = CardDefaults.outlinedCardColors(
            containerColor = backgroundColor.value
        ),
        onClick = { onClick?.invoke() }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Countdown(
                future = dateUnix,
                onFinishLabel = "Watch Live"
            )
            Launch(
                modifier = Modifier
                    .padding(top = 8.dp),
                missionName = missionName,
                patch = patch,
                date = date,
                vehicle = vehicle,
                reused = reused,
                landingPad = landingPad
            )
            if (state == LaunchState.FULL) launchSite?.let {
                LabelValue(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    label = stringResource(id = R.string.launch_site_label),
                    value = it
                )
            }
            if (description != null && state == LaunchState.FULL) {
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    text = description
                )
            }
        }
    }
}

@DynamicThemePreviews
@ComponentPreviews
@Composable
fun LaunchPreview() {
    SpaceXTheme {
        LaunchContainer(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            missionName = "Nusantara Satu (PSN-6) / GTO-1 / Beresheet",
            date = "14 Oct 2019 - 12:35",
            vehicle = "Falcon 9",
            reused = true,
            landingPad = "JRTI"
        ) {}
    }
}

@DynamicThemePreviews
@ComponentPreviews
@Composable
fun ExpandedLaunchPreview(
    @PreviewParameter(LoremIpsum::class) text: String
) {
    SpaceXTheme {
        LaunchContainer(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            missionName = "Nusantara Satu (PSN-6) / GTO-1 / Beresheet",
            date = "14 Oct 2019 - 12:35",
            vehicle = "Falcon 9",
            reused = true,
            landingPad = "JRTI",
            launchSite = "CCAFS SLC 40",
            description = text.split("\n\n").first(),
            dateUnix = 0,
            state = LaunchState.EXPANDED
        ) {}
    }
}

@DynamicThemePreviews
@ComponentPreviews
@Composable
fun FullLaunchPreview(
    @PreviewParameter(LoremIpsum::class) text: String
) {
    SpaceXTheme {
        LaunchContainer(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            missionName = "Nusantara Satu (PSN-6) / GTO-1 / Beresheet",
            date = "14 Oct 2019 - 12:35",
            vehicle = "Falcon 9",
            reused = true,
            landingPad = "JRTI",
            launchSite = "CCAFS SLC 40",
            description = text.split("\n\n").first(),
            dateUnix = 0,
            state = LaunchState.FULL
        ) {}
    }
}
