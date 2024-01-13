package uk.co.zac_h.spacex.core.ui

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers

@Preview(name = "phone", device = Devices.PHONE, showBackground = true)
@Preview(
    name = "phone",
    device = Devices.PHONE,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(name = "foldable", device = Devices.FOLDABLE, showBackground = true)
@Preview(
    name = "foldable",
    device = Devices.FOLDABLE,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(name = "tablet", device = Devices.TABLET, showBackground = true)
@Preview(
    name = "tablet",
    device = Devices.TABLET,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
annotation class DevicePreviews

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
annotation class ComponentPreviews

@Preview(
    name = "Light Mode - Red Dominated",
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE,
    showBackground = true
)
@Preview(
    name = "Light Mode - Green Dominated",
    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE,
    showBackground = true
)
@Preview(
    name = "Light Mode - Blue Dominated",
    wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
    showBackground = true
)
@Preview(
    name = "Light Mode - Yellow Dominated",
    wallpaper = Wallpapers.YELLOW_DOMINATED_EXAMPLE,
    showBackground = true
)
@Preview(
    name = "Dark Mode - Red Dominated",
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Preview(
    name = "Dark Mode - Green Dominated",
    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Preview(
    name = "Dark Mode - Blue Dominated",
    wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Preview(
    name = "Dark Mode - Yellow Dominated",
    wallpaper = Wallpapers.YELLOW_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
annotation class DynamicThemePreviews