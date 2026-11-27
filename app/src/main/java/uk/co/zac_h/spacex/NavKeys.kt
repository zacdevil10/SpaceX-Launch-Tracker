package uk.co.zac_h.spacex

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import uk.co.zac_h.spacex.feature.assets.vehicles.VehicleType

/**
 * Navigation 3 keys for the app's destinations. Each key is serializable so the back stacks
 * that hold it survive configuration changes and process death.
 */
@Serializable
data object LaunchesKey : NavKey

/** A launch's details, shown beside the launch list on wide screens. */
@Serializable
data class LaunchDetailKey(val launchId: String) : NavKey

@Serializable
data object NewsKey : NavKey

@Serializable
data object AssetsKey : NavKey

/** An asset's details, shown beside the asset lists on wide screens. */
@Serializable
data class VehicleDetailKey(val type: VehicleType, val vehicleId: String) : NavKey

@Serializable
data object CompanyKey : NavKey

@Serializable
data object SettingsKey : NavKey

@Serializable
data object StarlinkKey : NavKey
