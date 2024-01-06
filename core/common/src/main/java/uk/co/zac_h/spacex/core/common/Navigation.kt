package uk.co.zac_h.spacex.core.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

@Composable
fun PermanentNavigationDrawerContent(
    selectedDestination: String,
    navigateToTopLevelDestination: (TopLevelNavigation) -> Unit
) {
    PermanentDrawerSheet(
        modifier = Modifier.sizeIn(minWidth = 200.dp, maxWidth = 300.dp),
        drawerContainerColor = MaterialTheme.colorScheme.inverseOnSurface
    ) {
        Column {
            nav.forEach {
                NavigationDrawerItem(
                    label = { Text(text = it.label) },
                    selected = selectedDestination == it.route,
                    onClick = { navigateToTopLevelDestination(it) })
            }
        }
    }
}

@Composable
fun SpaceXBottomNavigationBar(
    selectedDestination: String,
    navigateToTopLevelDestination: (TopLevelNavigation) -> Unit
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        nav.forEach {
            NavigationBarItem(
                selected = selectedDestination == it.route,
                onClick = {
                    navigateToTopLevelDestination(it)
                },
                label = {
                    Text(text = it.label)
                },
                icon = {
                    Icon(painter = painterResource(id = it.icon), contentDescription = "")
                }
            )
        }
    }
}

val nav = listOf(
    TopLevelNavigation.Launches,
    TopLevelNavigation.News,
    TopLevelNavigation.Assets,
    TopLevelNavigation.Company,
    TopLevelNavigation.Settings,
)

class NavigationActions(private val navController: NavController) {

    fun navigateTo(destination: TopLevelNavigation) {
        navController.navigate(destination.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}

sealed class TopLevelNavigation(val route: String, @DrawableRes val icon: Int, val label: String) {
    data object Launches : TopLevelNavigation(
        route = "launches",
        icon = R.drawable.ic_calendar_plus,
        label = "Launches"
    )

    data object News : TopLevelNavigation(
        route = "news",
        icon = R.drawable.ic_newspaper,
        label = "News"
    )

    data object Assets : TopLevelNavigation(
        route = "assets",
        icon = R.drawable.ic_rocket,
        label = "Assets"
    )

    data object Company : TopLevelNavigation(
        route = "company",
        icon = R.drawable.ic_baseline_domain_24,
        label = "Company"
    )

    data object Settings : TopLevelNavigation(
        route = "settings",
        icon = R.drawable.ic_baseline_brightness_medium_24,
        label = "Settings"
    )
}