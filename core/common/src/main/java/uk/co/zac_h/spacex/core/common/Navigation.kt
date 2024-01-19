package uk.co.zac_h.spacex.core.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailDefaults
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import uk.co.zac_h.spacex.core.ui.ComponentPreviews
import uk.co.zac_h.spacex.core.ui.SpaceXTheme

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
fun ModalNavigationDrawerContent(
    selectedDestination: String,
    navigateToTopLevelDestination: (TopLevelNavigation) -> Unit,
    onDrawerClicked: () -> Unit = {}
) {
    ModalDrawerSheet {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SpaceX - Launch Tracker".uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = onDrawerClicked) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = ""
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                nav.forEach { destination ->
                    NavigationDrawerItem(
                        selected = selectedDestination == destination.route,
                        label = {
                            Text(
                                text = destination.label,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        },
                        icon = {
                            Icon(
                                painter = painterResource(destination.icon),
                                contentDescription = ""
                            )
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedContainerColor = Color.Transparent
                        ),
                        onClick = {
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
            nav.forEach { destination ->
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
fun SpaceXNavigationRailPreview() {
    SpaceXTheme {
        SpaceXNavigationRail(
            selectedDestination = "",
            navigateToTopLevelDestination = {},
            contentType = ContentType.SINGLE_PANE
        )
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
        icon = R.drawable.ic_baseline_settings_24,
        label = "Settings"
    )
}