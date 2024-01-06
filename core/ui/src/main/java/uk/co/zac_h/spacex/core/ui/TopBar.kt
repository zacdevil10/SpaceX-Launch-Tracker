package uk.co.zac_h.spacex.core.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SpaceXAppBar(
    modifier: Modifier = Modifier,
    label: String,
    isFullscreen: Boolean = true,
    navigationIcon: ImageVector,
    navigationIconAction: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                modifier = Modifier.basicMarquee(),
                text = label,
                maxLines = 1
            )
        },
        navigationIcon = {
            if (isFullscreen) IconButton(onClick = { navigationIconAction() }) {
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
        },
        divider = {}
    ) {
        for (i in 0..<pagerState.pageCount) {
            LeadingIconTab(
                selected = pagerState.currentPage == i,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(i)
                    }
                },
                modifier = modifier,
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.inverseSurface,
                icon = {
                    screens[i].icon?.let {
                        Icon(
                            painter = painterResource(id = it),
                            contentDescription = ""
                        )
                    }
                },
                text = {
                    val style =
                        MaterialTheme.typography.labelLarge.copy(textAlign = TextAlign.Center)
                    ProvideTextStyle(
                        value = style,
                        content = {
                            Box(modifier = Modifier) {
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
    @DrawableRes val icon: Int? = null,
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
            navigationIconAction = {}
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
                PagerItem(label = "Page 1") {},
                PagerItem(label = "Page 2") {}
            )
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@ComponentPreviews
@Composable
fun SpaceXTabLayoutWithIconPreview() {
    SpaceXTheme {
        SpaceXTabLayout(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            pagerState = rememberPagerState(pageCount = { 2 }),
            screens = listOf(
                PagerItem(label = "Page 1", icon = R.drawable.ic_history_black_24dp) {},
                PagerItem(label = "Page 2", icon = R.drawable.ic_history_black_24dp) {}
            )
        )
    }
}