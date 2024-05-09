package uk.co.zac_h.spacex

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailDefaults
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import uk.co.zac_h.spacex.core.common.ContentType
import uk.co.zac_h.spacex.core.common.R
import uk.co.zac_h.spacex.core.ui.ComponentPreviews
import uk.co.zac_h.spacex.core.ui.SpaceXTheme
import uk.co.zac_h.spacex.core.ui.component.SpaceXNavDrawerHeader
import uk.co.zac_h.spacex.core.ui.component.SpaceXNavDrawerItem

@Composable
fun SpaceXPermanentNavigationDrawerContent(
    modifier: Modifier = Modifier,
    selectedDestination: String,
    navigateToTopLevelDestination: (TopLevelNavigation) -> Unit
) {
    PermanentDrawerSheet(
        modifier = modifier,
        drawerContainerColor = MaterialTheme.colorScheme.inverseOnSurface
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            SpaceXNavDrawerHeader()

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TopLevelNavigation.entries.forEach { destination ->
                    SpaceXNavDrawerItem(
                        selected = selectedDestination == destination.route,
                        label = destination.label,
                        icon = destination.icon,
                        navigateToTopLevelDestination = {
                            navigateToTopLevelDestination(destination)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SpaceXModalNavigationDrawerContent(
    modifier: Modifier = Modifier,
    selectedDestination: String,
    navigateToTopLevelDestination: (TopLevelNavigation) -> Unit,
    onDrawerClicked: () -> Unit = {}
) {
    ModalDrawerSheet(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            SpaceXNavDrawerHeader(
                onDrawerClicked = onDrawerClicked
            )

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TopLevelNavigation.entries.forEach { destination ->
                    SpaceXNavDrawerItem(
                        selected = selectedDestination == destination.route,
                        label = destination.label,
                        icon = destination.icon,
                        navigateToTopLevelDestination = {
                            navigateToTopLevelDestination(destination)
                            onDrawerClicked()
                        }
                    )
                }
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
        TopLevelNavigation.entries.forEach {
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

@Composable
fun SpaceXNavigationRail(
    selectedDestination: String,
    navigateToTopLevelDestination: (TopLevelNavigation) -> Unit,
    onDrawerClicked: () -> Unit = {},
    contentType: ContentType
) {
    NavigationRail(
        modifier = Modifier
            .fillMaxHeight(),
        containerColor = when (contentType) {
            ContentType.SINGLE_PANE -> NavigationRailDefaults.ContainerColor
            ContentType.DUAL_PANE -> MaterialTheme.colorScheme.inverseOnSurface
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            NavigationRailItem(
                selected = false,
                onClick = onDrawerClicked,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = ""
                    )
                }
            )
        }
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            TopLevelNavigation.entries.forEach { destination ->
                NavigationRailItem(
                    selected = selectedDestination == destination.route,
                    onClick = { navigateToTopLevelDestination(destination) },
                    icon = {
                        Icon(
                            painter = painterResource(destination.icon),
                            contentDescription = ""
                        )
                    }
                )
            }
        }
    }
}

@ComponentPreviews
@Composable
private fun SpaceXBottomNavigationBarPreview() {
    SpaceXTheme {
        SpaceXBottomNavigationBar(
            selectedDestination = TopLevelNavigation.Launches.route,
            navigateToTopLevelDestination = {}
        )
    }
}

@ComponentPreviews
@Composable
fun SpaceXNavigationRailPreview() {
    SpaceXTheme {
        SpaceXNavigationRail(
            selectedDestination = TopLevelNavigation.Launches.route,
            navigateToTopLevelDestination = {},
            contentType = ContentType.SINGLE_PANE
        )
    }
}

@ComponentPreviews
@Composable
private fun SpaceXModalNavigationDrawerContentPreview() {
    SpaceXTheme {
        SpaceXModalNavigationDrawerContent(
            selectedDestination = TopLevelNavigation.Launches.route,
            navigateToTopLevelDestination = {}
        )
    }
}

@ComponentPreviews
@Composable
private fun PermanentNavigationDrawerContentPreview() {
    SpaceXTheme {
        SpaceXPermanentNavigationDrawerContent(
            selectedDestination = TopLevelNavigation.Launches.route,
            navigateToTopLevelDestination = {}
        )
    }
}

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

enum class TopLevelNavigation(val route: String, @DrawableRes val icon: Int, val label: String) {
    Launches(
        route = "launches",
        icon = R.drawable.ic_calendar_plus,
        label = "Launches"
    ),
    News(
        route = "news",
        icon = R.drawable.ic_newspaper,
        label = "News"
    ),
    Assets(
        route = "assets",
        icon = R.drawable.ic_rocket,
        label = "Assets"
    ),
    Company(
        route = "company",
        icon = R.drawable.ic_baseline_domain_24,
        label = "Company"
    ),
    Settings(
        route = "settings",
        icon = R.drawable.ic_baseline_settings_24,
        label = "Settings"
    )
}