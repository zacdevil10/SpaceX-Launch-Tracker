package uk.co.zac_h.spacex.feature.launch.container.news

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import uk.co.zac_h.spacex.core.common.NetworkVerticalListContent
import uk.co.zac_h.spacex.core.common.utils.convertDate
import uk.co.zac_h.spacex.core.common.utils.toMillis
import uk.co.zac_h.spacex.core.ui.Article
import uk.co.zac_h.spacex.network.dto.news.ArticleResponse

@Composable
fun LaunchNewsScreen(
    modifier: Modifier = Modifier,
    articles: LazyPagingItems<ArticleResponse>
) {
    NetworkVerticalListContent(
        modifier = modifier,
        result = articles,
        state = rememberLazyListState()
    ) {
        if (articles.itemCount == 0) item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = "No articles found for this launch.",
                textAlign = TextAlign.Center
            )
        }
        items(
            count = articles.itemCount,
            key = articles.itemKey { it.id }
        ) { index ->
            val article = articles[index]

            article?.let {
                Article(
                    title = article.title,
                    url = article.url,
                    image = article.imageUrl,
                    site = article.newsSite,
                    published = article.publishedAt.toMillis()?.convertDate()
                )
            }
        }
    }
}