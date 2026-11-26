package uk.co.zac_h.spacex

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
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
                SpaceXNavDrawerItems(
                    selectedDestination = selectedDestination,
                    navigateToTopLevelDestination = navigateToTopLevelDestination
                )
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
                SpaceXNavDrawerItems(
                    selectedDestination = selectedDestination,
                    navigateToTopLevelDestination = {
                        navigateToTopLevelDestination(it)
                        onDrawerClicked()
                    }
                )
            }
        }
    }
}

/**
 * The drawer's items: the primary destinations, then everything in More under its own heading.
 * The drawer has room for all of them, so it leaves out the More tab itself.
 */
@Composable
private fun ColumnScope.SpaceXNavDrawerItems(
    selectedDestination: TopLevelNavigation,
    navigateToTopLevelDestination: (TopLevelNavigation) -> Unit
) {
    TopLevelNavigation.primaryDestinations.forEach { destination ->
        SpaceXNavDrawerItem(
            selected = selectedDestination == destination,
            label = destination.label,
            icon = destination.icon,
            navigateToTopLevelDestination = { navigateToTopLevelDestination(destination) }
        )
    }
    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
    Text(
        modifier = Modifier
            .align(Alignment.Start)
            .padding(horizontal = 16.dp),
        text = TopLevelNavigation.More.label,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    TopLevelNavigation.moreDestinations.forEach { destination ->
        SpaceXNavDrawerItem(
            selected = selectedDestination == destination,
            label = destination.label,
            icon = destination.icon,
            navigateToTopLevelDestination = { navigateToTopLevelDestination(destination) }
        )
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
        TopLevelNavigation.barDestinations.forEach {
            NavigationBarItem(
                selected = selectedDestination.barDestination == it,
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
            TopLevelNavigation.barDestinations.forEach { destination ->
                NavigationRailItem(
                    selected = selectedDestination.barDestination == destination,
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
 * The app's destinations, as shown in the bottom bar, navigation rail, drawers and the More sheet.
 * [key] is the Navigation 3 key each one opens, and [placement] says where it's listed and how it
 * opens.
 */
enum class TopLevelNavigation(
    val key: NavKey?,
    @DrawableRes val icon: Int,
    val label: String,
    val placement: Placement
) {
    Launches(
        key = LaunchesKey,
        icon = R.drawable.ic_calendar_plus,
        label = "Launches",
        placement = Placement.PRIMARY
    ),
    News(
        key = NewsKey,
        icon = R.drawable.ic_newspaper,
        label = "News",
        placement = Placement.PRIMARY
    ),
    Assets(
        key = AssetsKey,
        icon = R.drawable.ic_rocket,
        label = "Assets",
        placement = Placement.PRIMARY
    ),
    More(
        key = null,
        icon = R.drawable.ic_more_horiz,
        label = "More",
        placement = Placement.MORE
    ),
    Company(
        key = CompanyKey,
        icon = R.drawable.ic_baseline_domain_24,
        label = "Company",
        placement = Placement.SECONDARY
    ),
    Settings(
        key = SettingsKey,
        icon = R.drawable.ic_baseline_settings_24,
        label = "Settings",
        placement = Placement.DIALOG
    );

    /** Where a destination is listed and how it opens. */
    enum class Placement {
        /** In the bar and rail, with its own back stack. */
        PRIMARY,

        /** The More item in the bar and rail. It opens a sheet listing the rest, not a screen. */
        MORE,

        /** Listed under More, with its own back stack. More is highlighted while it's showing. */
        SECONDARY,

        /** Listed under More, and opened as a dialog over whichever destination is showing. */
        DIALOG
    }

    /** True for the destinations that have their own back stack. */
    val hasBackStack: Boolean
        get() = placement == Placement.PRIMARY || placement == Placement.SECONDARY

    /** The bar and rail item to highlight while this destination's back stack is showing. */
    val barDestination: TopLevelNavigation
        get() = if (placement == Placement.SECONDARY) More else this

    companion object {
        /** The bottom bar and navigation rail items: the primary destinations, then More. */
        val barDestinations: List<TopLevelNavigation> =
            entries.filter { it.placement == Placement.PRIMARY || it.placement == Placement.MORE }

        /** The destinations listed in the drawer above the More group. */
        val primaryDestinations: List<TopLevelNavigation> =
            entries.filter { it.placement == Placement.PRIMARY }

        /** The destinations listed in the More sheet, and in the drawer's More group. */
        val moreDestinations: List<TopLevelNavigation> =
            entries.filter { it.placement == Placement.SECONDARY || it.placement == Placement.DIALOG }

        /** The destinations with their own back stack. */
        val backStackDestinations: List<TopLevelNavigation> = entries.filter { it.hasBackStack }

        /**
         * The destination that's showing, for highlighting in the drawer and More sheet: a dialog
         * when one is open on top of [backStackTop], otherwise [route].
         */
        fun selected(route: TopLevelNavigation, backStackTop: NavKey?): TopLevelNavigation =
            entries.firstOrNull { it.placement == Placement.DIALOG && it.key == backStackTop }
                ?: route
    }
}

/**
 * The More sheet: the destinations that don't fit in the bottom bar or rail, such as Company and
 * Settings. Picking one closes the sheet.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpaceXMoreSheet(
    selectedDestination: TopLevelNavigation,
    navigateToTopLevelDestination: (TopLevelNavigation) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        SpaceXMoreSheetContent(
            selectedDestination = selectedDestination,
            onDestinationClick = {
                onDismiss()
                navigateToTopLevelDestination(it)
            }
        )
    }
}

@Composable
private fun SpaceXMoreSheetContent(
    selectedDestination: TopLevelNavigation,
    onDestinationClick: (TopLevelNavigation) -> Unit
) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        TopLevelNavigation.moreDestinations.forEach { destination ->
            ListItem(
                modifier = Modifier.clickable { onDestinationClick(destination) },
                headlineContent = { Text(text = destination.label) },
                leadingContent = {
                    Icon(painter = painterResource(destination.icon), contentDescription = null)
                },
                colors = if (destination == selectedDestination) {
                    ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                } else {
                    ListItemDefaults.colors(containerColor = Color.Transparent)
                }
            )
        }
    }
}

@ComponentPreviews
@Composable
private fun SpaceXMoreSheetContentPreview() {
    SpaceXTheme {
        SpaceXMoreSheetContent(
            selectedDestination = TopLevelNavigation.Company,
            onDestinationClick = {}
        )
    }
}
