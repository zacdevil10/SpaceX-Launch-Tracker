package uk.co.zac_h.spacex.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RedditPost(
    modifier: Modifier = Modifier,
    author: String,
    created: String,
    pinned: Boolean,
    showThumbnail: Boolean,
    thumbnail: String,
    title: String,
    description: String? = null,
    showPreview: Boolean,
    previewImage: String? = null,
    score: Int,
    comments: Int,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .padding(end = 4.dp),
                    text = author
                )
                Divider(
                    modifier = Modifier
                        .size(1.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    modifier = Modifier
                        .padding(start = 4.dp),
                    text = created
                )
                Spacer(modifier = Modifier.weight(1f))
                if (pinned) Icon(
                    painter = painterResource(id = R.drawable.ic_pin),
                    contentDescription = ""
                )
            }
            Row(
                modifier = Modifier
                    .padding(start = 16.dp, top = 8.dp, end = 16.dp)
                    .fillMaxWidth()
            ) {
                if (showThumbnail && thumbnail.isNotEmpty()) {
                    AsyncImage(
                        model = thumbnail,
                        contentDescription = "",
                        error = painterResource(id = R.drawable.ic_baseline_error_outline_24),
                        modifier = Modifier
                            .width(128.dp)
                            .padding(end = 8.dp)
                            .aspectRatio(16f / 9f)
                            .clip(CardDefaults.elevatedShape),
                        contentScale = ContentScale.Crop
                    )
                }
                Text(
                    text = title,
                    maxLines = 2,
                    style = MaterialTheme.typography.headlineSmall,
                    overflow = TextOverflow.Ellipsis
                )
            }
            description?.let {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                    text = HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_COMPACT).toString(),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
            previewImage?.let {
                if (showPreview) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .padding(top = 8.dp),
                        model = it,
                        contentDescription = "",
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_up_bold),
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = score.toString())
                Spacer(modifier = Modifier.width(16.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_comment_black_24dp),
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = comments.toString())
            }
        }
    }
}

@ComponentPreviews
@Composable
fun RedditPostPreview() {
    SpaceXTheme {
        RedditPost(
            author = "Author",
            created = "4d",
            pinned = true,
            showThumbnail = true,
            thumbnail = "",
            title = "Title",
            showPreview = false,
            score = 231,
            comments = 123,
            onClick = {}
        )
    }
}