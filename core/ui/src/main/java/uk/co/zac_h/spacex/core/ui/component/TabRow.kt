package uk.co.zac_h.spacex.core.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.core.ui.ComponentPreviews
import uk.co.zac_h.spacex.core.ui.R
import uk.co.zac_h.spacex.core.ui.SpaceXTheme

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SpaceXTabLayout(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    tabs: List<Tab>,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    var scrollableTabs by remember { mutableStateOf(false) }

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

    SpaceXAutoAdjustTabLayout(
        modifier = modifier,
        scrollableTabs = scrollableTabs,
        selectedTabIndex = pagerState.currentPage,
        containerColor = tabRowContainerColor,
        tabs = {
            tabs.mapIndexed { index, tab ->
                SpaceXAutoAdjustTab(
                    pagerState = pagerState,
                    tabPosition = index,
                    tab = tab,
                    setScrollableTab = { scrollableTabs = true }
                )
            }
        },
    )
}

@Composable
private fun SpaceXAutoAdjustTabLayout(
    modifier: Modifier = Modifier,
    scrollableTabs: Boolean = false,
    selectedTabIndex: Int,
    containerColor: Color,
    tabs: @Composable () -> Unit,
) {
    if (scrollableTabs) {
        PrimaryScrollableTabRow(
            modifier = modifier,
            selectedTabIndex = selectedTabIndex,
            containerColor = containerColor,
            edgePadding = 0.dp,
            tabs = tabs
        )
    } else {
        PrimaryTabRow(
            modifier = modifier,
            selectedTabIndex = selectedTabIndex,
            containerColor = containerColor,
            tabs = tabs
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SpaceXAutoAdjustTab(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    tabPosition: Int,
    tab: Tab,
    setScrollableTab: () -> Unit
) {
    tab.icon?.let {
        SpaceXLeadingIconTab(
            modifier = modifier,
            pagerState = pagerState,
            tabPosition = tabPosition,
            tab = tab
        )
    } ?: SpaceXTab(
        modifier = modifier,
        pagerState = pagerState,
        tabPosition = tabPosition,
        tab = tab,
        setScrollableTab = setScrollableTab
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SpaceXTab(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    tabPosition: Int,
    tab: Tab,
    setScrollableTab: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    Tab(
        modifier = modifier,
        selected = pagerState.currentPage == tabPosition,
        onClick = {
            coroutineScope.launch {
                pagerState.animateScrollToPage(tabPosition)
            }
        },
        selectedContentColor = MaterialTheme.colorScheme.primary,
        unselectedContentColor = MaterialTheme.colorScheme.inverseSurface,
        text = {
            Text(
                text = tab.label,
                onTextLayout = { textLayoutResult ->
                    if (textLayoutResult.hasVisualOverflow) setScrollableTab()
                },
                maxLines = 1
            )
        },
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SpaceXLeadingIconTab(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    tabPosition: Int,
    tab: Tab
) {
    val coroutineScope = rememberCoroutineScope()

    LeadingIconTab(
        modifier = modifier,
        selected = pagerState.currentPage == tabPosition,
        onClick = {
            coroutineScope.launch {
                pagerState.animateScrollToPage(tabPosition)
            }
        },
        selectedContentColor = MaterialTheme.colorScheme.primary,
        unselectedContentColor = MaterialTheme.colorScheme.inverseSurface,
        icon = {
            tab.icon?.let {
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = ""
                )
            }
        },
        text = {
            Text(text = tab.label)
        },
    )
}

data class PagerItem(
    val label: String,
    @DrawableRes val icon: Int? = null,
    val screen: @Composable (Int) -> Unit
)

fun List<PagerItem>.toTabs() = map { Tab(label = it.label, icon = it.icon) }

data class Tab(
    val label: String,
    @DrawableRes val icon: Int? = null
)

object SpaceXTabRowDefaults {

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
@ComponentPreviews
@Composable
private fun SpaceXTabLayoutPreview() {
    SpaceXTheme {
        SpaceXTabLayout(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            pagerState = rememberPagerState(pageCount = { 2 }),
            tabs = listOf(
                Tab(label = "Page 1"),
                Tab(label = "Page 2")
            )
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@ComponentPreviews
@Composable
private fun SpaceXTabLayoutWithIconPreview() {
    SpaceXTheme {
        SpaceXTabLayout(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            pagerState = rememberPagerState(pageCount = { 2 }),
            tabs = listOf(
                Tab(label = "Page 1", icon = R.drawable.ic_history_black_24dp),
                Tab(label = "Page 2", icon = R.drawable.ic_history_black_24dp)
            )
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@ComponentPreviews
@Composable
private fun SpaceXScrollableTabLayoutPreview() {
    SpaceXTheme {
        SpaceXTabLayout(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            pagerState = rememberPagerState(pageCount = { 6 }),
            tabs = listOf(
                Tab(label = "Page 1"),
                Tab(label = "Page 2"),
                Tab(label = "Page 3"),
                Tab(label = "Page 4"),
                Tab(label = "Page 5"),
                Tab(label = "Page 6")
            )
        )
    }
}
