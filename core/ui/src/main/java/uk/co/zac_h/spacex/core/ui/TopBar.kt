package uk.co.zac_h.spacex.core.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpaceXAppBar(
    modifier: Modifier = Modifier,
    label: String,
    navigationIcon: ImageVector,
    navigationIconAction: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(text = label) },
        navigationIcon = {
            IconButton(onClick = { navigationIconAction() }) {
                Icon(
                    imageVector = navigationIcon,
                    contentDescription = ""
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SpaceXTabLayout(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    screens: List<PagerItem>
) {
    val coroutineScope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        modifier = modifier,
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onSurface,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                height = 2.dp
            )
        }
    ) {
        for (i in 0..<pagerState.pageCount) {
            Tab(
                selected = pagerState.currentPage == i,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(i)
                    }
                },
                modifier = modifier,
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.inverseSurface,
                text = {
                    val style =
                        MaterialTheme.typography.labelLarge.copy(textAlign = TextAlign.Center)
                    ProvideTextStyle(
                        value = style,
                        content = {
                            Box(modifier = Modifier.padding(top = 8.dp)) {
                                Text(text = screens[i].label)
                            }
                        },
                    )
                },
            )
        }
    }
}

data class PagerItem(
    val label: String,
    val screen: @Composable () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@ComponentPreviews
@Composable
fun SpaceXAppBarPreview() {
    SpaceXTheme {
        SpaceXAppBar(
            label = "App Bar",
            navigationIcon = Icons.Filled.Menu,
            navigationIconAction = {},
            scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@ComponentPreviews
@Composable
fun SpaceXTabLayoutPreview() {
    SpaceXTheme {
        SpaceXTabLayout(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            pagerState = rememberPagerState(pageCount = { 2 }),
            screens = listOf(
                PagerItem("Page 1") {},
                PagerItem("Page 2") {}
            )
        )
    }
}