package uk.co.zac_h.spacex

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import uk.co.zac_h.feature.news.NewsScreen
import uk.co.zac_h.spacex.core.common.ContentType
import uk.co.zac_h.spacex.core.common.ModalNavigationDrawerContent
import uk.co.zac_h.spacex.core.common.NavigationActions
import uk.co.zac_h.spacex.core.common.NavigationType
import uk.co.zac_h.spacex.core.common.PermanentNavigationDrawerContent
import uk.co.zac_h.spacex.core.common.SpaceXBottomNavigationBar
import uk.co.zac_h.spacex.core.common.SpaceXNavigationRail
import uk.co.zac_h.spacex.core.common.TopLevelNavigation
import uk.co.zac_h.spacex.feature.launch.LaunchListScreen
import uk.co.zac_h.spacex.feature.settings.SettingsDialog
import uk.co.zac_h.spacex.feature.settings.company.CompanyScreen

@Composable
fun SpaceXApp(
    windowSize: WindowSizeClass
) {
    val contentType: ContentType
    val navigationType: NavigationType

    WindowHeightSizeClass.Expanded

    when (windowSize.widthSizeClass) {
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

    val navController = rememberNavController()
    val navigationActions = remember(navController) {
        NavigationActions(navController)
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination =
        navBackStackEntry?.destination?.route ?: TopLevelNavigation.Launches.route

    if (navigationType == NavigationType.PERMANENT_NAVIGATION_DRAWER) {
        PermanentNavigationDrawer(
            drawerContent = {
                PermanentNavigationDrawerContent(
                    selectedDestination = selectedDestination,
                    navigateToTopLevelDestination = navigationActions::navigateTo
                )
            }
        ) {
            SpaceXAppContent(
                navigationType = navigationType,
                contentType = contentType,
                navController = navController,
                selectedDestination = selectedDestination,
                navigateToTopLevelDestination = navigationActions::navigateTo
            )
        }
    } else {
        ModalNavigationDrawer(
            drawerContent = {
                ModalNavigationDrawerContent(
                    selectedDestination = selectedDestination,
                    navigateToTopLevelDestination = navigationActions::navigateTo,
                    onDrawerClicked = {
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
            },
            drawerState = drawerState
        ) {
            SpaceXAppContent(
                navigationType = navigationType,
                contentType = contentType,
                navController = navController,
                selectedDestination = selectedDestination,
                navigateToTopLevelDestination = navigationActions::navigateTo,
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
    navController: NavHostController,
    selectedDestination: String,
    navigateToTopLevelDestination: (TopLevelNavigation) -> Unit,
    onDrawerClicked: () -> Unit = {}
) {
    Row(modifier = modifier.fillMaxSize()) {
        AnimatedVisibility(visible = navigationType == NavigationType.NAVIGATION_RAIL) {
            Row {
                SpaceXNavigationRail(
                    selectedDestination = selectedDestination,
                    navigateToTopLevelDestination = navigateToTopLevelDestination,
                    onDrawerClicked = onDrawerClicked,
                    contentType = contentType
                )
                if (contentType == ContentType.SINGLE_PANE) Divider(
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                )
            }
        }
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            bottomBar = {
                AnimatedVisibility(visible = navigationType == NavigationType.BOTTOM_NAVIGATION) {
                    SpaceXBottomNavigationBar(
                        selectedDestination = selectedDestination,
                        navigateToTopLevelDestination = navigateToTopLevelDestination
                    )
                }
            }
        ) {
            SpaceXNavHost(
                modifier = modifier.padding(it),
                navController = navController,
                contentType = contentType
            )
        }
    }
}

@Composable
fun SpaceXNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    contentType: ContentType
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = TopLevelNavigation.Launches.route
    ) {
        composable(TopLevelNavigation.Launches.route) {
            LaunchListScreen(
                contentType = contentType
            )
        }
        composable(TopLevelNavigation.News.route) {
            NewsScreen(
                contentType = contentType
            )
        }
        composable(TopLevelNavigation.Assets.route) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(text = "Assets", modifier = Modifier.align(Alignment.Center))
            }
        }
        composable(TopLevelNavigation.Company.route) {
            CompanyScreen(
                contentType = contentType
            )
        }
        dialog(TopLevelNavigation.Settings.route) {
            SettingsDialog(onDismiss = { navController.popBackStack() })
        }
    }
}