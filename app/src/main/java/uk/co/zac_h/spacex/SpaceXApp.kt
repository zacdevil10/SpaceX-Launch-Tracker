package uk.co.zac_h.spacex

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import uk.co.zac_h.spacex.core.common.ContentType
import uk.co.zac_h.spacex.core.common.NavigationActions
import uk.co.zac_h.spacex.core.common.NavigationType
import uk.co.zac_h.spacex.core.common.PermanentNavigationDrawerContent
import uk.co.zac_h.spacex.core.common.SpaceXBottomNavigationBar
import uk.co.zac_h.spacex.core.common.TopLevelNavigation
import uk.co.zac_h.spacex.feature.launch.LaunchListScreen
import uk.co.zac_h.spacex.feature.settings.SettingsDialog

@Composable
fun SpaceXApp(
    windowSize: WindowSizeClass
) {
    val contentType: ContentType
    val navigationType: NavigationType

    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            contentType = ContentType.SINGLE_PANE
            navigationType = NavigationType.BOTTOM_NAVIGATION
        }

        WindowWidthSizeClass.Medium -> {
            contentType = ContentType.DUAL_PANE
            navigationType = NavigationType.BOTTOM_NAVIGATION
        }

        WindowWidthSizeClass.Expanded -> {
            contentType = ContentType.DUAL_PANE
            navigationType = NavigationType.PERMANENT_NAVIGATION_DRAWER
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
        SpaceXAppContent(
            navigationType = navigationType,
            contentType = contentType,
            navController = navController,
            selectedDestination = selectedDestination,
            navigateToTopLevelDestination = navigationActions::navigateTo
        )
    }
}

@Composable
fun SpaceXAppContent(
    modifier: Modifier = Modifier,
    navigationType: NavigationType,
    contentType: ContentType,
    navController: NavHostController,
    selectedDestination: String,
    navigateToTopLevelDestination: (TopLevelNavigation) -> Unit
) {
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
                modifier = Modifier,
                contentType = contentType
            )
        }
        composable(TopLevelNavigation.News.route) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(text = "News", modifier = Modifier.align(Alignment.Center))
            }
        }
        composable(TopLevelNavigation.Assets.route) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(text = "Assets", modifier = Modifier.align(Alignment.Center))
            }
        }
        composable(TopLevelNavigation.Company.route) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(text = "Company", modifier = Modifier.align(Alignment.Center))
            }
        }
        dialog(TopLevelNavigation.Settings.route) {
            SettingsDialog(onDismiss = { navController.popBackStack() })
        }
    }
}