package uk.co.zac_h.spacex.feature.settings

import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDirections
import uk.co.zac_h.spacex.core.common.recyclerview.RecyclerViewItem

data class SettingsLocalItem(
    override val icon: Int,
    override val label: String,
    val direction: NavDirections
) : SettingsItem

data class SettingsDeepLinkItem(
    override val icon: Int,
    override val label: String,
    val deepLink: NavDeepLinkRequest
) : SettingsItem

interface SettingsItem : RecyclerViewItem {
    val icon: Int
    val label: String
}

data class SettingsHeader(
    val label: String
) : RecyclerViewItem