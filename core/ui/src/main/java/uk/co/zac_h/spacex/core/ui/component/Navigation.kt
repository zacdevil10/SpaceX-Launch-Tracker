package uk.co.zac_h.spacex.core.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import uk.co.zac_h.spacex.core.ui.ComponentPreviews
import uk.co.zac_h.spacex.core.ui.R
import uk.co.zac_h.spacex.core.ui.SpaceXTheme

@Composable
fun SpaceXNavDrawerHeader(
    modifier: Modifier = Modifier,
    onDrawerClicked: (() -> Unit)? = null
) {
    Column(
        modifier = modifier,
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
                modifier = Modifier
                    .padding(vertical = 8.dp),
                text = "SpaceX - Launch Tracker",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            onDrawerClicked?.let {
                IconButton(onClick = it) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = ""
                    )
                }
            }
        }
    }
}

@Composable
fun SpaceXNavDrawerItem(
    selected: Boolean,
    label: String,
    @DrawableRes icon: Int,
    navigateToTopLevelDestination: () -> Unit
) {
    NavigationDrawerItem(
        selected = selected,
        label = {
            Text(
                text = label,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        },
        icon = {
            Icon(
                painter = painterResource(icon),
                contentDescription = ""
            )
        },
        colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = Color.Transparent
        ),
        onClick = navigateToTopLevelDestination
    )
}

@ComponentPreviews
@Composable
private fun SpaceXNavDrawerHeaderPreview() {
    SpaceXTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
        ) {
            SpaceXNavDrawerHeader()
        }
    }
}

@ComponentPreviews
@Composable
private fun SpaceXNavDrawerItemPreview() {
    SpaceXTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SpaceXNavDrawerItem(
                selected = true,
                label = "Label",
                icon = R.drawable.ic_rocket,
                navigateToTopLevelDestination = {}
            )
            SpaceXNavDrawerItem(
                selected = false,
                label = "Label",
                icon = R.drawable.ic_rocket,
                navigateToTopLevelDestination = {}
            )
        }
    }
}
