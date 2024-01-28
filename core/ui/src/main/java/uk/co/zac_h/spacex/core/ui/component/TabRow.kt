package uk.co.zac_h.spacex.core.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
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
    scrollBehavior: TopAppBarScrollBehavior? = null
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

    val tabTextWidths = remember {
        mutableStateListOf(*tabs.map { 0.dp }.toTypedArray())
    }
    val tabIconWidths = remember {
        mutableStateListOf(*tabs.map { 0.dp }.toTypedArray())
    }

    SpaceXAutoAdjustTabLayout(
        modifier = modifier,
        scrollableTabs = scrollableTabs,
        selectedTabIndex = pagerState.currentPage,
        containerColor = tabRowContainerColor,
        indicator = { tabPositions ->
            RoundedSpringTabIndicator(
                currentPage = pagerState.currentPage,
                tabPositions = tabPositions,
                currentTabWidth = tabTextWidths[pagerState.currentPage] + tabIconWidths[pagerState.currentPage]
            )
        },
        tabs = {
            tabs.mapIndexed { index, tab ->
                SpaceXAutoAdjustTab(
                    pagerState = pagerState,
                    tabPosition = index,
                    tab = tab,
                    setTabIconWidth = {
                        tabIconWidths[index] = it
                    },
                    setTabTextWidth = {
                        tabTextWidths[index] = it
                    },
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
    indicator: @Composable (tabPositions: List<TabPosition>) -> Unit,
    tabs: @Composable () -> Unit,
) {
    if (scrollableTabs) {
        SpaceXScrollableTabLayout(
            modifier = modifier,
            selectedTabIndex = selectedTabIndex,
            containerColor = containerColor,
            indicator = indicator,
            tabs = tabs,
        )
    } else {
        SpaceXTabLayout(
            modifier = modifier,
            selectedTabIndex = selectedTabIndex,
            containerColor = containerColor,
            indicator = indicator,
            tabs = tabs,
        )
    }
}

@Composable
private fun SpaceXScrollableTabLayout(
    modifier: Modifier = Modifier,
    selectedTabIndex: Int,
    containerColor: Color,
    indicator: @Composable (tabPositions: List<TabPosition>) -> Unit,
    tabs: @Composable () -> Unit,
) {
    ScrollableTabRow(
        modifier = modifier,
        selectedTabIndex = selectedTabIndex,
        containerColor = containerColor,
        edgePadding = 0.dp,
        indicator = indicator,
        tabs = tabs
    )
}

@Composable
private fun SpaceXTabLayout(
    modifier: Modifier = Modifier,
    selectedTabIndex: Int,
    containerColor: Color,
    indicator: @Composable (tabPositions: List<TabPosition>) -> Unit,
    tabs: @Composable () -> Unit,
) {
    TabRow(
        modifier = modifier,
        selectedTabIndex = selectedTabIndex,
        containerColor = containerColor,
        indicator = indicator,
        tabs = tabs
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SpaceXAutoAdjustTab(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    tabPosition: Int,
    tab: Tab,
    setTabIconWidth: (Dp) -> Unit,
    setTabTextWidth: (Dp) -> Unit,
    setScrollableTab: () -> Unit
) {
    tab.icon?.let {
        SpaceXLeadingIconTab(
            modifier = modifier,
            pagerState = pagerState,
            tabPosition = tabPosition,
            tab = tab,
            setTabIconWidth = setTabIconWidth,
            setTabTextWidth = setTabTextWidth
        )
    } ?: SpaceXTab(
        modifier = modifier,
        pagerState = pagerState,
        tabPosition = tabPosition,
        tab = tab,
        setTabTextWidth = setTabTextWidth,
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
    setTabTextWidth: (Dp) -> Unit,
    setScrollableTab: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

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
                    setTabTextWidth(
                        with(density) {
                            textLayoutResult.size.width.toDp()
                        }
                    )
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
    tab: Tab,
    setTabIconWidth: (Dp) -> Unit,
    setTabTextWidth: (Dp) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

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
                    modifier = Modifier.onGloballyPositioned {
                        setTabIconWidth(
                            with(density) {
                                it.size.width.toDp()
                            }
                        )
                    },
                    painter = painterResource(id = it),
                    contentDescription = ""
                )
            }
        },
        text = {
            Text(
                text = tab.label,
                onTextLayout = { textLayoutResult ->
                    setTabTextWidth(
                        with(density) {
                            textLayoutResult.size.width.toDp() + 8.dp
                        }
                    )
                }
            )
        },
    )
}

@Composable
private fun RoundedSpringTabIndicator(
    modifier: Modifier = Modifier,
    currentPage: Int,
    tabPositions: List<TabPosition>,
    currentTabWidth: Dp
) {
    RoundedTabIndicator(
        modifier = modifier
            .tabIndicatorSpringOffset(
                currentPage = currentPage,
                tabPositions = tabPositions,
                currentTabWidth = currentTabWidth
            )
    )
}

@Composable
private fun RoundedTabIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier
            .height(3.dp)
            .background(
                MaterialTheme.colorScheme.primary,
                RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp),
            ),
    )
}

private fun Modifier.tabIndicatorSpringOffset(
    currentPage: Int,
    tabPositions: List<TabPosition>,
    currentTabWidth: Dp
): Modifier = composed {
    val transition = updateTransition(currentPage, label = "")
    val indicatorStart by transition.animateDp(
        transitionSpec = {
            if (initialState < targetState) {
                spring(dampingRatio = 1f, stiffness = 50f)
            } else {
                spring(dampingRatio = 1f, stiffness = 1000f)
            }
        },
        label = "tab_indicator_start_position"
    ) {
        (tabPositions[it].left + tabPositions[it].right - currentTabWidth) / 2
    }

    val indicatorEnd by transition.animateDp(
        transitionSpec = {
            if (initialState < targetState) {
                spring(dampingRatio = 1f, stiffness = 1000f)
            } else {
                spring(dampingRatio = 1f, stiffness = 50f)
            }
        },
        label = "tab_indicator_end_position"
    ) {
        (tabPositions[it].left + tabPositions[it].right + currentTabWidth) / 2
    }

    wrapContentSize(align = Alignment.BottomStart)
        .offset(x = indicatorStart)
        .width(indicatorEnd - indicatorStart)
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
