package uk.co.zac_h.spacex.core.ui

import android.content.res.Configuration
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Vehicle(
    modifier: Modifier = Modifier,
    image: String? = null,
    title: String? = null,
    status: String? = null,
    buttonText: String? = null,
    navigate: () -> Unit,
    content: @Composable (() -> Unit)? = null
) {
    ElevatedCard(
        modifier = modifier,
        shape = CardDefaults.elevatedShape,
        onClick = { navigate() }
    ) {
        Column {
            image?.let {
                AsyncImage(
                    model = it,
                    contentDescription = "",
                    error = painterResource(id = R.drawable.ic_baseline_error_outline_24),
                    modifier = Modifier
                        .height(256.dp)
                        .fillMaxWidth()
                        .clip(CardDefaults.elevatedShape),
                    contentScale = ContentScale.Crop
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

@ComponentPreviews
@Composable
fun VehiclePreview(
    @PreviewParameter(LoremIpsum::class) text: String
) {
    SpaceXTheme {
        Vehicle(
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