package uk.co.zac_h.spacex.core.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import uk.co.zac_h.spacex.core.ui.component.DynamicAsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Vehicle(
    modifier: Modifier = Modifier,
    image: String? = null,
    title: String? = null,
    status: String? = null,
    buttonText: String? = null,
    isSelected: Boolean = false,
    navigate: () -> Unit,
    content: @Composable (() -> Unit)? = null
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

    ElevatedCard(
        modifier = modifier,
        shape = CardDefaults.elevatedShape,
        colors = CardDefaults.cardColors(
            containerColor = animatedCardBackgroundColor.value
        ),
        onClick = { navigate() }
    ) {
        Column {
            image?.let {
                DynamicAsyncImage(
                    modifier = Modifier
                        .height(256.dp)
                        .fillMaxWidth()
                        .clip(CardDefaults.elevatedShape),
                    model = it,
                    contentDescription = "",
                    placeholder = painterResource(id = R.drawable.ic_rocket),
                    contentScale = ContentScale.Crop,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            ) {
                Text(text = title.orEmpty(), style = MaterialTheme.typography.headlineSmall)
                Text(text = status.orEmpty(), modifier = Modifier.align(Alignment.CenterVertically))
            }
            content?.invoke()
            Button(
                onClick = {
                    navigate()
                },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 8.dp, end = 16.dp, bottom = 8.dp)
            ) {
                Text(text = buttonText.orEmpty())
            }
        }
    }
}

@DynamicThemePreviews
@ComponentPreviews
@Composable
fun VehiclePreview(
    @PreviewParameter(LoremIpsum::class) text: String
) {
    SpaceXTheme {
        Vehicle(
            modifier = Modifier.padding(16.dp),
            image = "",
            title = "Falcon 1",
            status = "Status",
            buttonText = "Specs",
            navigate = {}
        ) {
            Text(
                text = text,
                maxLines = 4,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 24.dp)
            )
        }
    }
}