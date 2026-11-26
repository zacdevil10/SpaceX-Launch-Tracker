package uk.co.zac_h.spacex

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
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
import androidx.navigation3.runtime.NavKey
import uk.co.zac_h.spacex.core.common.ContentType
import uk.co.zac_h.spacex.core.common.R
import uk.co.zac_h.spacex.core.ui.ComponentPreviews
import uk.co.zac_h.spacex.core.ui.SpaceXTheme
import uk.co.zac_h.spacex.core.ui.component.SpaceXNavDrawerHeader
import uk.co.zac_h.spacex.core.ui.component.SpaceXNavDrawerItem

@Composable
fun SpaceXPermanentNavigationDrawerContent(
    modifier: Modifier = Modifier,
    selectedDestination: TopLevelNavigation,
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
                        selected = selectedDestination == destination,
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
    selectedDestination: TopLevelNavigation,
    navigateToTopLevelDestination: (TopLevelNavigation) -> Unit,
    onDrawerClicked: () -> Unit = {}
) {
    ModalDrawerSheet(
        modifier = modifier,
        windowInsets = WindowInsets.safeDrawing.only(
            WindowInsetsSides.Vertical + WindowInsetsSides.Start
        )
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
                        selected = selectedDestination == destination,
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
    selectedDestination: TopLevelNavigation,
    navigateToTopLevelDestination: (TopLevelNavigation) -> Unit
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth(),
        windowInsets = WindowInsets.safeDrawing.only(
            WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
        )
    ) {
        TopLevelNavigation.entries.forEach {
            NavigationBarItem(
                selected = selectedDestination == it,
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
    selectedDestination: TopLevelNavigation,
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
        },
        // Include the display cutout, which landscape phones put on the rail's side.
        windowInsets = WindowInsets.safeDrawing.only(
            WindowInsetsSides.Vertical + WindowInsetsSides.Start
        )
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
                    selected = selectedDestination == destination,
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
            selectedDestination = TopLevelNavigation.Launches,
            navigateToTopLevelDestination = {}
        )
    }
}

@ComponentPreviews
@Composable
fun SpaceXNavigationRailPreview() {
    SpaceXTheme {
        SpaceXNavigationRail(
            selectedDestination = TopLevelNavigation.Launches,
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
            selectedDestination = TopLevelNavigation.Launches,
            navigateToTopLevelDestination = {}
        )
    }
}

@ComponentPreviews
@Composable
private fun PermanentNavigationDrawerContentPreview() {
    SpaceXTheme {
        SpaceXPermanentNavigationDrawerContent(
            selectedDestination = TopLevelNavigation.Launches,
            navigateToTopLevelDestination = {}
        )
    }
}

/**
 * The destinations shown in the bottom bar, navigation rail and drawers. [key] is the Navigation 3
 * key each one opens. Settings opens as a dialog over the current destination instead of switching
 * to its own back stack; see [isDialog].
 */
enum class TopLevelNavigation(
    val key: NavKey,
    @DrawableRes val icon: Int,
    val label: String,
    val isDialog: Boolean = false
) {
    Launches(
        key = LaunchesKey,
        icon = R.drawable.ic_calendar_plus,
        label = "Launches"
    ),
    News(
        key = NewsKey,
        icon = R.drawable.ic_newspaper,
        label = "News"
    ),
    Assets(
        key = AssetsKey,
        icon = R.drawable.ic_rocket,
        label = "Assets"
    ),
    Company(
        key = CompanyKey,
        icon = R.drawable.ic_baseline_domain_24,
        label = "Company"
    ),
    Settings(
        key = SettingsKey,
        icon = R.drawable.ic_baseline_settings_24,
        label = "Settings",
        isDialog = true
    )
}