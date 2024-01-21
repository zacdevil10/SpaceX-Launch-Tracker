package uk.co.zac_h.feature.news

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import uk.co.zac_h.feature.news.articles.ArticlesScreen
import uk.co.zac_h.feature.news.reddit.RedditFeedScreen
import uk.co.zac_h.spacex.core.common.ContentType
import uk.co.zac_h.spacex.core.ui.component.PagerItem
import uk.co.zac_h.spacex.core.ui.component.SpaceXTabLayout
import uk.co.zac_h.spacex.core.ui.component.TabRowDefaults

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    contentType: ContentType
) {
    val pagerState = rememberPagerState(pageCount = { 2 })

    val scrollBehavior = TabRowDefaults.pinnedScrollBehavior()

    val screens = listOf(
        PagerItem(label = "Articles", icon = R.drawable.ic_newspaper) {
            ArticlesScreen(
                contentType = contentType
            )
        },
        PagerItem(label = "Reddit", icon = R.drawable.reddit) {
            RedditFeedScreen(
                contentType = contentType
            )
        }
    )

    when (contentType) {
        ContentType.SINGLE_PANE -> NewsSinglePaneContent(
            pagerState = pagerState,
            screens = screens,
            scrollBehavior = scrollBehavior
        )
        ContentType.DUAL_PANE -> NewsTwoPaneContent(
            pagerState = pagerState,
            screens = screens,
            scrollBehavior = scrollBehavior
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NewsTwoPaneContent(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    screens: List<PagerItem>,
    scrollBehavior: TopAppBarScrollBehavior
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.inverseOnSurface)
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            NewsSinglePaneContent(
                pagerState = pagerState,
                screens = screens,
                scrollBehavior = scrollBehavior
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NewsSinglePaneContent(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    screens: List<PagerItem>,
    scrollBehavior: TopAppBarScrollBehavior
) {
    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SpaceXTabLayout(
                pagerState = pagerState,
                screens = screens,
                scrollBehavior = scrollBehavior
            )
        }
    ) { padding ->
        HorizontalPager(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            state = pagerState,
            beyondBoundsPageCount = 1,
            verticalAlignment = Alignment.Top,
            key = { screens[it].label }
        ) { page ->
            screens[page].screen(page)
        }
    }
}
