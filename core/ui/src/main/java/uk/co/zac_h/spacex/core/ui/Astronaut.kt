package uk.co.zac_h.spacex.core.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import uk.co.zac_h.spacex.core.ui.component.DynamicAsyncImage

/** The height of the card's photo and name row; the card is taller only when expanded. */
private val AstronautRowHeight = 136.dp

/**
 * An astronaut's card: photo, role, name and agency.
 *
 * @param expandable true to show a chevron and reveal [status], [firstFlight] and [description]
 * below the row when [expanded]. Cards that open details elsewhere instead pass false.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun Astronaut(
    modifier: Modifier = Modifier,
    image: Any? = null,
    role: String? = null,
    title: String? = null,
    agency: String? = null,
    status: String? = null,
    firstFlight: String? = null,
    description: String? = null,
    expanded: Boolean = false,
    expandable: Boolean = true,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    val animatedCardBackgroundColor = animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
        animationSpec = tween(200, 0, LinearEasing),
        label = ""
    )

    val rotationState by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "rotation"
    )

    ElevatedCard(
        modifier = modifier,
        shape = CardDefaults.elevatedShape,
        colors = CardDefaults.cardColors(
            containerColor = animatedCardBackgroundColor.value
        ),
        onClick = onClick
    ) {
        Column {
            // A fixed height, so the card never takes its size from the photo, which can be cached
            // larger after its details screen has shown it.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AstronautRowHeight)
            ) {
                DynamicAsyncImage(
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .fillMaxHeight()
                        .clip(CardDefaults.elevatedShape),
                    model = image,
                    contentDescription = "",
                    placeholder = Icons.Outlined.Person,
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .fillMaxSize()
                ) {
                    role?.let {
                        Text(
                            modifier = Modifier.padding(top = 16.dp, end = 16.dp),
                            text = it,
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    title?.let {
                        Text(
                            modifier = Modifier
                                .padding(top = 8.dp, end = 16.dp)
                                .basicMarquee(),
                            text = it,
                            style = MaterialTheme.typography.headlineSmall,
                            maxLines = 1
                        )
                    }
                    agency?.let {
                        Text(
                            modifier = Modifier
                                .padding(top = 8.dp, end = 16.dp)
                                .basicMarquee(),
                            text = it,
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1
                        )
                    }
                    if (expandable) {
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(end = 16.dp, bottom = 8.dp)
                                .rotate(rotationState),
                            imageVector = Icons.Filled.KeyboardArrowDown,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column {
                    status?.let {
                        LabelValue(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(top = 8.dp),
                            label = "Status",
                            value = it
                        )
                    }
                    firstFlight?.let {
                        LabelValue(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(top = 8.dp),
                            label = "First flight",
                            value = it
                        )
                    }
                    description?.let {
                        Text(
                            modifier = Modifier.padding(all = 16.dp),
                            text = it
                        )
                    }
                }
            }
        }
    }
}

@DynamicThemePreviews
@ComponentPreviews
@Composable
fun AstronautCollapsedPreview(
    @PreviewParameter(LoremIpsum::class) text: String
) {
    SpaceXTheme {
        Astronaut(
            modifier = Modifier.padding(16.dp),
            role = "Earthling",
            title = "Little Earth",
            agency = "National Aeronautics and Space Administration",
            status = "Active",
            firstFlight = "02 Mar 19",
            description = text,
            expanded = false,
            expandable = true
        ) {}
    }
}

@DynamicThemePreviews
@ComponentPreviews
@Composable
fun AstronautDualPanePreview(
    @PreviewParameter(LoremIpsum::class) text: String
) {
    SpaceXTheme {
        Astronaut(
            modifier = Modifier.padding(16.dp),
            role = "Earthling",
            title = "Little Earth",
            agency = "National Aeronautics and Space Administration",
            status = "Active",
            firstFlight = "02 Mar 19",
            description = text,
            expanded = false,
            expandable = false
        ) {}
    }
}

@DynamicThemePreviews
@ComponentPreviews
@Composable
fun AstronautExpandedPreview(
    @PreviewParameter(LoremIpsum::class) text: String
) {
    SpaceXTheme {
        Astronaut(
            modifier = Modifier.padding(16.dp),
            role = "Earthling",
            title = "Little Earth",
            agency = "National Aeronautics and Space Administration",
            status = "Active",
            firstFlight = "02 Mar 19",
            description = text,
            expanded = true,
            expandable = true
        ) {}
    }
}