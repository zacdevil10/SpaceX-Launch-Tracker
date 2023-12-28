package uk.co.zac_h.spacex.feature.launch.details.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import uk.co.zac_h.spacex.core.ui.ComponentPreviews
import uk.co.zac_h.spacex.core.ui.SpaceXTheme
import uk.co.zac_h.spacex.feature.launch.R
import uk.co.zac_h.spacex.feature.launch.VideoItem

@Composable
fun LaunchDetailsVideo(
    modifier: Modifier = Modifier,
    videoItem: VideoItem
) {
    val uriHandler = LocalUriHandler.current

    OutlinedCard(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                modifier = Modifier.aspectRatio(16f / 9f),
                model = videoItem.imageUrl, contentDescription = "",
                contentScale = ContentScale.Crop
            )
            videoItem.title?.let { title ->
                Text(
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                    text = title,
                    maxLines = 4,
                    minLines = 4
                )
            }
            Button(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 8.dp, end = 16.dp, bottom = 8.dp),
                onClick = {
                    uriHandler.openUri(videoItem.url)
                }) {
                Text(
                    text = videoItem.source?.let {
                        stringResource(
                            id = R.string.watch_on_domain_label,
                            it
                        )
                    } ?: stringResource(id = R.string.watch_label)
                )
            }
        }
    }
}

@ComponentPreviews
@Composable
fun LaunchDetailsVideoPreview() {
    SpaceXTheme {
        LaunchDetailsVideo(
            videoItem = VideoItem(
                title = "Watch live: SpaceX Falcon Heavy launches secretive X-37B military spaceplane",
                description = "Watch live coverage as SpaceX launches a Falcon Heavy rocket carrying the U.S. military's secretive X-37B spaceplane. Liftoff from historic launch pad 39A at...",
                imageUrl = null,
                url = "https://www.youtube.com/watch?v=wnfddhDuWDE",
                source = "Youtube"
            )
        )
    }
}