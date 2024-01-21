package uk.co.zac_h.spacex.core.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.core.ui.ComponentPreviews
import uk.co.zac_h.spacex.core.ui.DynamicThemePreviews
import uk.co.zac_h.spacex.core.ui.R
import uk.co.zac_h.spacex.core.ui.SpaceXTheme

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SpaceXTabLayout(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    screens: List<PagerItem>,
    scrollable: Boolean = false,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    val coroutineScope = rememberCoroutineScope()

    val heightOffsetLimit = with(LocalDensity.current) { -48.dp.toPx() }
    SideEffect {
        if (scrollBehavior?.state?.heightOffsetLimit != heightOffsetLimit) {
            scrollBehavior?.state?.heightOffsetLimit = heightOffsetLimit
        }
    }

    val tabRowContainerColor by animateColorAsState(
        targetValue = scrollBehavior.containerColor(),
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = ""
    )

    val tabs: @Composable () -> Unit = {
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

    if (scrollable) {
        SpaceXScrollableTabLayout(
            modifier = modifier,
            pagerState = pagerState,
            tabs = tabs,
            color = tabRowContainerColor
        )
    } else {
        SpaceXTabLayout(
            modifier = modifier,
            pagerState = pagerState,
            tabs = tabs,
            color = tabRowContainerColor
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SpaceXScrollableTabLayout(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    tabs: @Composable () -> Unit,
    color: Color
) {
    ScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        modifier = modifier,
        containerColor = color,
        contentColor = MaterialTheme.colorScheme.onSurface,
        edgePadding = 0.dp,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                height = 2.dp
            )
        },
        divider = {},
        tabs = tabs
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SpaceXTabLayout(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    tabs: @Composable () -> Unit,
    color: Color
) {
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        modifier = modifier,
        containerColor = color,
        contentColor = MaterialTheme.colorScheme.onSurface,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                height = 2.dp
            )
        },
        divider = {},
        tabs = tabs
    )
}

data class PagerItem(
    val label: String,
    @DrawableRes val icon: Int? = null,
    val screen: @Composable (Int) -> Unit
)

object TabRowDefaults {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun pinnedScrollBehavior(
        state: TopAppBarState = rememberTopAppBarState(),
        canScroll: () -> Boolean = { true }
    ): TopAppBarScrollBehavior = PinnedScrollBehavior(state = state, canScroll = canScroll)
}

@OptIn(ExperimentalMaterial3Api::class)
private class PinnedScrollBehavior(
    override val state: TopAppBarState,
    val canScroll: () -> Boolean = { true }
) : TopAppBarScrollBehavior {
    override val isPinned: Boolean = true
    override val snapAnimationSpec: AnimationSpec<Float>? = null
    override val flingAnimationSpec: DecayAnimationSpec<Float>? = null
    override var nestedScrollConnection = object : NestedScrollConnection {
        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource
        ): Offset {
            if (!canScroll()) return Offset.Zero
            if (consumed.y == 0f && available.y > 0f) {
                state.contentOffset = 0f
            } else {
                state.contentOffset += consumed.y
            }
            return Offset.Zero
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@DynamicThemePreviews
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
            ),
            scrollable = false
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@DynamicThemePreviews
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
            ),
            scrollable = false
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@DynamicThemePreviews
@ComponentPreviews
@Composable
fun SpaceXScrollableTabLayoutPreview() {
    SpaceXTheme {
        SpaceXTabLayout(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            pagerState = rememberPagerState(pageCount = { 2 }),
            screens = listOf(
                PagerItem(label = "Page 1") {},
                PagerItem(label = "Page 2") {}
            ),
            scrollable = true
        )
    }
}
