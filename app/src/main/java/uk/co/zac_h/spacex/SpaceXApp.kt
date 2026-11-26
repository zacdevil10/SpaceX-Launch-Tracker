package uk.co.zac_h.spacex

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import kotlinx.coroutines.launch
import uk.co.zac_h.feature.news.NewsScreen
import uk.co.zac_h.spacex.core.common.ContentType
import uk.co.zac_h.spacex.core.common.NavigationType
import uk.co.zac_h.spacex.feature.assets.AssetsScreen
import uk.co.zac_h.spacex.feature.launch.LaunchesScreen
import uk.co.zac_h.spacex.feature.settings.SettingsDialog
import uk.co.zac_h.spacex.feature.settings.company.CompanyScreen
import uk.co.zac_h.spacex.feature.starlink.StarlinkScreen

@Composable
fun SpaceXApp(
    widthSize: WindowWidthSizeClass
) {
    val contentType: ContentType
    val navigationType: NavigationType

    when (widthSize) {
        WindowWidthSizeClass.Compact -> {
            contentType = ContentType.SINGLE_PANE
            navigationType = NavigationType.BOTTOM_NAVIGATION
        }

        WindowWidthSizeClass.Medium -> {
            contentType = ContentType.SINGLE_PANE
            navigationType = NavigationType.NAVIGATION_RAIL
        }

        WindowWidthSizeClass.Expanded -> {
            contentType = ContentType.DUAL_PANE
            navigationType = NavigationType.NAVIGATION_RAIL
        }

        else -> {
            contentType = ContentType.SINGLE_PANE
            navigationType = NavigationType.BOTTOM_NAVIGATION
        }
    }

    SpaceXNavigationWrapper(
        navigationType = navigationType,
        contentType = contentType
    )
}

@Composable
fun SpaceXNavigationWrapper(
    navigationType: NavigationType,
    contentType: ContentType
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val topLevelBackStack = rememberTopLevelBackStack()

    // Settings is a dialog over the current destination, but it's highlighted while it's open,
    // as it was when it had its own route.
    val selectedDestination =
        if (topLevelBackStack.backStack.lastOrNull() == SettingsKey) {
            TopLevelNavigation.Settings
        } else {
            topLevelBackStack.topLevelRoute
        }

    val navigateToTopLevelDestination: (TopLevelNavigation) -> Unit = { destination ->
        if (destination.isDialog) {
            topLevelBackStack.add(destination.key)
        } else {
            topLevelBackStack.selectTopLevel(destination)
        }
    }

    if (navigationType == NavigationType.PERMANENT_NAVIGATION_DRAWER) {
        PermanentNavigationDrawer(
            drawerContent = {
                SpaceXPermanentNavigationDrawerContent(
                    selectedDestination = selectedDestination,
                    navigateToTopLevelDestination = navigateToTopLevelDestination
                )
            }
        ) {
            SpaceXAppContent(
                navigationType = navigationType,
                contentType = contentType,
                topLevelBackStack = topLevelBackStack,
                selectedDestination = selectedDestination,
                navigateToTopLevelDestination = navigateToTopLevelDestination
            )
        }
    } else {
        ModalNavigationDrawer(
            drawerContent = {
                SpaceXModalNavigationDrawerContent(
                    selectedDestination = selectedDestination,
                    navigateToTopLevelDestination = navigateToTopLevelDestination,
                    onDrawerClicked = {
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
            },
            drawerState = drawerState,
            // Horizontal drags on the Starlink globe turn it, so the edge swipe mustn't open the
            // drawer there. The drawer can still be closed by swiping while it's open.
            gesturesEnabled = drawerState.isOpen || selectedDestination != TopLevelNavigation.Starlink
        ) {
            SpaceXAppContent(
                navigationType = navigationType,
                contentType = contentType,
                topLevelBackStack = topLevelBackStack,
                selectedDestination = selectedDestination,
                navigateToTopLevelDestination = navigateToTopLevelDestination,
                onDrawerClicked = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            )
        }
    }
}

@Composable
fun SpaceXAppContent(
    modifier: Modifier = Modifier,
    navigationType: NavigationType,
    contentType: ContentType,
    topLevelBackStack: TopLevelBackStack<TopLevelNavigation, NavKey>,
    selectedDestination: TopLevelNavigation,
    navigateToTopLevelDestination: (TopLevelNavigation) -> Unit,
    onDrawerClicked: () -> Unit = {}
) {
    val showNavigationRail = navigationType == NavigationType.NAVIGATION_RAIL

    Row(modifier = modifier.fillMaxSize()) {
        AnimatedVisibility(visible = showNavigationRail) {
            Row {
                SpaceXNavigationRail(
                    selectedDestination = selectedDestination,
                    navigateToTopLevelDestination = navigateToTopLevelDestination,
                    onDrawerClicked = onDrawerClicked,
                    contentType = contentType
                )
                if (contentType == ContentType.SINGLE_PANE) VerticalDivider()
            }
        }
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            bottomBar = {
                // Hidden while the keyboard is up, so a search field has room for its results.
                @OptIn(ExperimentalLayoutApi::class)
                val imeVisible = WindowInsets.isImeVisible
                AnimatedVisibility(visible = navigationType == NavigationType.BOTTOM_NAVIGATION && !imeVisible) {
                    SpaceXBottomNavigationBar(
                        selectedDestination = selectedDestination,
                        navigateToTopLevelDestination = navigateToTopLevelDestination
                    )
                }
            },
            // The app draws edge-to-edge (enforced from targetSdk 35). The navigation rail pads the
            // start edge itself, so the content only needs the remaining sides. The insets are
            // consumed here so screens and their top app bars don't apply them a second time.
            contentWindowInsets = WindowInsets.safeDrawing.only(
                if (showNavigationRail) {
                    WindowInsetsSides.Top + WindowInsetsSides.End + WindowInsetsSides.Bottom
                } else {
                    WindowInsetsSides.Top + WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
                }
            )
        ) {
            SpaceXNavHost(
                modifier = Modifier
                    .padding(it)
                    .consumeWindowInsets(it),
                topLevelBackStack = topLevelBackStack,
                contentType = contentType
            )
        }
    }
}

/**
 * Remembers one Navigation 3 back stack for each top-level destination except Settings, which
 * opens as a dialog over whichever destination is showing. The back stacks and the selected
 * destination are saved, so they survive configuration changes and process death.
 */
@Composable
fun rememberTopLevelBackStack(): TopLevelBackStack<TopLevelNavigation, NavKey> {
    val launches = rememberNavBackStack(TopLevelNavigation.Launches.key)
    val news = rememberNavBackStack(TopLevelNavigation.News.key)
    val assets = rememberNavBackStack(TopLevelNavigation.Assets.key)
    val starlink = rememberNavBackStack(TopLevelNavigation.Starlink.key)
    val company = rememberNavBackStack(TopLevelNavigation.Company.key)
    val currentRoute = rememberSaveable { mutableStateOf(TopLevelNavigation.Launches) }

    return remember(launches, news, assets, starlink, company, currentRoute) {
        TopLevelBackStack(
            startRoute = TopLevelNavigation.Launches,
            stacks = mapOf(
                TopLevelNavigation.Launches to launches,
                TopLevelNavigation.News to news,
                TopLevelNavigation.Assets to assets,
                TopLevelNavigation.Starlink to starlink,
                TopLevelNavigation.Company to company
            ),
            currentRoute = currentRoute
        )
    }
}

@Composable
fun SpaceXNavHost(
    modifier: Modifier = Modifier,
    topLevelBackStack: TopLevelBackStack<TopLevelNavigation, NavKey>,
    contentType: ContentType
) {
    NavDisplay(
        modifier = modifier,
        backStack = topLevelBackStack.backStack,
        onBack = { topLevelBackStack.removeLast() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator<NavKey>(),
            rememberViewModelStoreNavEntryDecorator<NavKey>()
        ),
        sceneStrategies = listOf(remember { DialogSceneStrategy<NavKey>() }),
        entryProvider = entryProvider {
            entry<LaunchesKey> {
                LaunchesScreen(
                    contentType = contentType
                )
            }
            entry<NewsKey> {
                NewsScreen(
                    contentType = contentType
                )
            }
            entry<AssetsKey> {
                AssetsScreen(
                    contentType = contentType
                )
            }
            entry<StarlinkKey> {
                StarlinkScreen()
            }
            entry<CompanyKey> {
                CompanyScreen(
                    contentType = contentType
                )
            }
            entry<SettingsKey>(metadata = DialogSceneStrategy.dialog()) {
                SettingsDialog(
                    onDismiss = { topLevelBackStack.removeLast() }
                )
            }
        }
    )
}
