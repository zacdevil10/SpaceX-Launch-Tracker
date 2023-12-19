package uk.co.zac_h.spacex.core.ui

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AstronautView(
    modifier: Modifier = Modifier,
    image: Any? = null,
    role: String? = null,
    title: String? = null,
    agency: String? = null,
    status: String? = null,
    firstFlight: String? = null,
    description: String? = null,
    expanded: Boolean,
    onClick: () -> Unit
) {
    val rotationState by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "rotation"
    )

    ElevatedCard(
        modifier = modifier,
        shape = CardDefaults.elevatedShape,
        onClick = onClick
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
            ) {
                AsyncImage(
                    model = image,
                    contentDescription = "",
                    error = painterResource(id = R.drawable.ic_baseline_error_outline_24),
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .clip(CardDefaults.elevatedShape),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .fillMaxWidth()
                ) {
                    role?.let {
                        Text(
                            modifier = Modifier.padding(top = 16.dp, end = 16.dp),
                            text = it,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    title?.let {
                        Text(
                            modifier = Modifier.padding(top = 8.dp, end = 16.dp),
                            text = it,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                    agency?.let {
                        Text(
                            modifier = Modifier.padding(top = 8.dp, end = 16.dp),
                            text = it,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    Image(
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(top = 8.dp, end = 16.dp, bottom = 8.dp)
                            .rotate(rotationState),
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = ""
                    )
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column {
                    status?.let {
                        LabelValueView(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(top = 8.dp),
                            label = "Status",
                            value = it
                        )
                    }
                    firstFlight?.let {
                        LabelValueView(
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

@Preview(name = "Light Mode - Collapsed")
@Preview(name = "Dark Mode - Collapsed", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AstronautViewCollapsedPreview(
    @PreviewParameter(LoremIpsum::class) text: String
) {
    SpaceXTheme {
        AstronautView(
            modifier = Modifier.padding(16.dp),
            role = "Earthling",
            title = "Little Earth",
            agency = "NASA",
            status = "Active",
            firstFlight = "02 Mar 19",
            description = text,
            expanded = false
        ) {}
    }
}

@Preview(name = "Light Mode - Expanded")
@Preview(name = "Dark Mode - Expanded", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AstronautViewExpandedPreview(
    @PreviewParameter(LoremIpsum::class) text: String
) {
    SpaceXTheme {
        AstronautView(
            modifier = Modifier.padding(16.dp),
            role = "Earthling",
            title = "Little Earth",
            agency = "NASA",
            status = "Active",
            firstFlight = "02 Mar 19",
            description = text,
            expanded = true
        ) {}
    }
}