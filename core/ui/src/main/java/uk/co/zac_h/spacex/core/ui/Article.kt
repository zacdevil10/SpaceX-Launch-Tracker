package uk.co.zac_h.spacex.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun Article(
    modifier: Modifier = Modifier,
    title: String,
    url: String,
    image: String,
    site: String,
    published: String?
) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier
            .clickable { uriHandler.openUri(url) }
            .padding(bottom = 8.dp)
    ) {
        AsyncImage(
            modifier = Modifier
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .aspectRatio(16f / 9f)
                .clip(CardDefaults.elevatedShape),
            model = image,
            contentDescription = "",
            error = painterResource(id = R.drawable.ic_baseline_error_outline_24),
            contentScale = ContentScale.Crop
        )
        Text(
            modifier = Modifier
                .padding(top = 8.dp, start = 16.dp, end = 16.dp),
            text = site,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            modifier = Modifier
                .padding(top = 8.dp, start = 16.dp, end = 16.dp),
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        published?.let {
            Text(
                modifier = Modifier
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                text = it,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@DynamicThemePreviews
@ComponentPreviews
@Composable
fun ArticlePreview() {
    SpaceXTheme {
        Article(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),
            title = "Title",
            url = "",
            image = "",
            site = "Site",
            published = "Published"
        )
    }
}