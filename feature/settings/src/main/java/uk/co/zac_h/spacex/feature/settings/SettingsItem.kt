package uk.co.zac_h.spacex.feature.settings

import androidx.navigation.NavDirections
import uk.co.zac_h.spacex.core.common.recyclerview.RecyclerViewItem

data class SettingsItem(
    val icon: Int,
    val label: String,
    val direction: NavDirections
) : RecyclerViewItem

data class SettingsHeader(
    val label: String
) : RecyclerViewItem