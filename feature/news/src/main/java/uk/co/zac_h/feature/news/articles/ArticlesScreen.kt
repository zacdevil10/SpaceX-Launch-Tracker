package uk.co.zac_h.feature.news.articles

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import uk.co.zac_h.spacex.core.common.NetworkContent
import uk.co.zac_h.spacex.core.common.pagingFooter
import uk.co.zac_h.spacex.core.common.utils.convertDate
import uk.co.zac_h.spacex.core.common.utils.toMillis
import uk.co.zac_h.spacex.core.ui.Article

private val ArticleMinWidth = 360.dp

@Composable
fun ArticlesScreen(
    viewModel: ArticlesViewModel = hiltViewModel()
) {
    val articles = viewModel.articlesLiveData.collectAsLazyPagingItems()

    val articlesLazyListState = rememberLazyStaggeredGridState()

    NetworkContent(
        modifier = Modifier.fillMaxSize(),
        result = articles
    ) {
        LazyVerticalStaggeredGrid(
            // One column on phones, more as the window widens.
            columns = StaggeredGridCells.Adaptive(minSize = ArticleMinWidth),
            state = articlesLazyListState
        ) {
            items(
                count = articles.itemCount,
                key = articles.itemKey { it.id }
            ) { index ->
                val article = articles[index]

                article?.let {
                    Article(
                        title = article.title,
                        url = article.url,
                        image = article.image,
                        site = article.site,
                        published = article.published.toMillis()?.convertDate()
                    )
                }
            }

            pagingFooter(
                loadState = articles.loadState.append,
                retry = articles::retry
            )
        }
    }
}
