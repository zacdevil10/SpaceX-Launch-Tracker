package uk.co.zac_h.feature.news.articles

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import uk.co.zac_h.spacex.core.common.ContentType
import uk.co.zac_h.spacex.core.common.utils.convertDate
import uk.co.zac_h.spacex.core.common.utils.toMillis
import uk.co.zac_h.spacex.core.ui.Article

@Composable
fun ArticlesScreen(
    contentType: ContentType,
    viewModel: ArticlesViewModel = hiltViewModel()
) {
    val articles = viewModel.articlesLiveData.collectAsLazyPagingItems()

    val articlesLazyListState = rememberLazyStaggeredGridState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (articles.loadState.refresh is LoadState.Loading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
            )
        } else {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(
                    when (contentType) {
                        ContentType.SINGLE_PANE -> 1
                        ContentType.DUAL_PANE -> 2
                    }
                ),
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

                item {
                    when (articles.loadState.append) {
                        is LoadState.Loading -> LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                        )

                        is LoadState.Error -> Box(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Button(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(16.dp),
                                onClick = { articles.retry() }
                            ) {
                                Text(text = "Retry")
                            }
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}