package uk.co.zac_h.feature.news.reddit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import uk.co.zac_h.spacex.core.common.ContentType
import uk.co.zac_h.spacex.core.common.NetworkContent
import uk.co.zac_h.spacex.core.common.pagingFooter
import uk.co.zac_h.spacex.core.common.utils.convertDate
import uk.co.zac_h.spacex.core.ui.RedditPost

@Composable
fun RedditFeedScreen(
    contentType: ContentType,
    viewModel: RedditFeedViewModel = hiltViewModel()
) {
    val posts = viewModel.redditFeed.collectAsLazyPagingItems()

    val postsLazyListState = rememberLazyStaggeredGridState()

    val uriHandler = LocalUriHandler.current

    NetworkContent(
        modifier = Modifier.fillMaxSize(),
        result = posts
    ) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(
                when (contentType) {
                    ContentType.SINGLE_PANE -> 1
                    ContentType.DUAL_PANE -> 2
                }
            ),
            state = postsLazyListState,
            verticalItemSpacing = 16.dp,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            items(
                count = posts.itemCount,
                key = posts.itemKey { it.id }
            ) { index ->
                val post = posts[index]

                post?.let {
                    RedditPost(
                        author = post.author,
                        created = post.created.toLong().convertDate(),
                        pinned = post.stickied,
                        showThumbnail = !(post.redditDomain || post.isSelf),
                        thumbnail = post.thumbnail,
                        title = post.title,
                        description = post.description,
                        showPreview = post.redditDomain && post.preview != null && post.description.isNullOrEmpty(),
                        previewImage = post.preview?.let {
                            it.images.first().resolutions[it.images.first().resolutions.size - 1].url
                        },
                        score = post.score,
                        comments = post.commentsCount,
                        onClick = { uriHandler.openUri("https://www.reddit.com${post.permalink}") }
                    )
                }
            }

            pagingFooter(
                loadState = posts.loadState.append,
                retry = posts::retry
            )
        }
    }
}