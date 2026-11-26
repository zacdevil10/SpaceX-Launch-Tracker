package uk.co.zac_h.spacex

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import uk.co.zac_h.spacex.core.ui.ComponentPreviews
import uk.co.zac_h.spacex.core.ui.SpaceXTheme

/**
 * The More tab: the destinations that don't fit in the bottom bar, such as Company and Settings.
 *
 * @param selectedDestination highlighted when it's one of [destinations], which on wide screens
 * means it's open beside this list.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreScreen(
    destinations: List<TopLevelNavigation>,
    selectedDestination: TopLevelNavigation,
    onDestinationClick: (TopLevelNavigation) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text(text = TopLevelNavigation.More.label) }) },
        contentWindowInsets = WindowInsets(0)
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(destinations, key = { it.name }) { destination ->
                val selected = destination == selectedDestination
                ListItem(
                    modifier = Modifier.clickable { onDestinationClick(destination) },
                    headlineContent = { Text(text = destination.label) },
                    leadingContent = {
                        Icon(painter = painterResource(destination.icon), contentDescription = null)
                    },
                    colors = if (selected) {
                        ListItemDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    } else {
                        ListItemDefaults.colors()
                    }
                )
            }
        }
    }
}

@ComponentPreviews
@Composable
private fun MoreScreenPreview() {
    SpaceXTheme {
        MoreScreen(
            destinations = TopLevelNavigation.moreDestinations,
            selectedDestination = TopLevelNavigation.Company,
            onDestinationClick = {}
        )
    }
}
