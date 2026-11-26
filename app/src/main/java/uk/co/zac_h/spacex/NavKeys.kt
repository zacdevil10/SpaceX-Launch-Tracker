package uk.co.zac_h.spacex

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Navigation 3 keys for the app's destinations. Each key is serializable so the back stacks
 * that hold it survive configuration changes and process death.
 */
@Serializable
data object LaunchesKey : NavKey

@Serializable
data object NewsKey : NavKey

@Serializable
data object AssetsKey : NavKey

@Serializable
data object CompanyKey : NavKey

@Serializable
data object SettingsKey : NavKey

/** The debug tools menu. Only debug builds have an entry for it; see DebugTools. */
@Serializable
data object DebugToolsKey : NavKey
