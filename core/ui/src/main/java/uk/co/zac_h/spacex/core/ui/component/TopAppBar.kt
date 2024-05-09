package uk.co.zac_h.spacex.core.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import uk.co.zac_h.spacex.core.ui.ComponentPreviews
import uk.co.zac_h.spacex.core.ui.DynamicThemePreviews
import uk.co.zac_h.spacex.core.ui.SpaceXTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SpaceXAppBar(
    modifier: Modifier = Modifier,
    label: String,
    isFullscreen: Boolean = true,
    navigationIcon: ImageVector,
    navigationIconAction: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    val heightOffsetLimit = with(LocalDensity.current) { -64.dp.toPx() }
    SideEffect {
        if (scrollBehavior?.state?.heightOffsetLimit != heightOffsetLimit) {
            scrollBehavior?.state?.heightOffsetLimit = heightOffsetLimit
        }
    }

    val appBarContainerColor by animateColorAsState(
        targetValue = scrollBehavior.containerColor(),
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = ""
    )

    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                modifier = Modifier.basicMarquee(),
                text = label,
                maxLines = 1
            )
        },
        navigationIcon = {
            if (isFullscreen) IconButton(onClick = { navigationIconAction() }) {
                Icon(
                    imageVector = navigationIcon,
                    contentDescription = ""
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = appBarContainerColor
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@DynamicThemePreviews
@ComponentPreviews
@Composable
fun SpaceXAppBarPreview() {
    SpaceXTheme {
        SpaceXAppBar(
            label = "App Bar",
            navigationIcon = Icons.Filled.Menu,
            navigationIconAction = {}
        )
    }
}
