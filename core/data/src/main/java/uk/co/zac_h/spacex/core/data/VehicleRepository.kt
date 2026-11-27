package uk.co.zac_h.spacex.core.data

import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uk.co.zac_h.spacex.core.data.mapper.toLauncher
import uk.co.zac_h.spacex.core.data.mapper.toRocket
import uk.co.zac_h.spacex.core.data.mapper.toSpacecraft
import uk.co.zac_h.spacex.core.data.mapper.toSpacecraftConfiguration
import uk.co.zac_h.spacex.core.model.Launcher
import uk.co.zac_h.spacex.core.model.Rocket
import uk.co.zac_h.spacex.core.model.Spacecraft
import uk.co.zac_h.spacex.core.model.SpacecraftConfiguration
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.store.AssetStore
import javax.inject.Inject

/**
 * SpaceX rockets, boosters, spacecraft and spacecraft designs, from the Room cache that
 * [AssetStore] keeps current. Rockets and spacecraft designs both come from the stored SpaceX
 * agency, so [refreshAgency] updates both.
 */
class VehicleRepository @Inject constructor(
    private val assetStore: AssetStore
) {

    /** Rocket configurations, or null until the agency has been fetched once. */
    val rockets: Flow<List<Rocket>?> = assetStore.agency.map { agency ->
        agency?.let { it.launcherList.orEmpty().map { rocket -> rocket.toRocket() } }
    }

    /** Spacecraft designs such as Crew Dragon, or null until the agency has been fetched once. */
    val spacecraftConfigurations: Flow<List<SpacecraftConfiguration>?> =
        assetStore.agency.map { agency ->
            agency?.let { it.spacecraftList.orEmpty().map { config ->
                config.toSpacecraftConfiguration()
            } }
        }

    /** Fetches the agency if it is out of date, or always when [force]. */
    suspend fun refreshAgency(force: Boolean = false): ApiResult<Unit> =
        assetStore.refreshAgency(force)

    /** Stored boosters, fetching each page once and the list again only once it is out of date. */
    fun launchers(): Flow<PagingData<Launcher>> =
        assetStore.launchers().map { page -> page.map { it.toLauncher() } }

    /** Stored spacecraft, fetching each page once and the list again only once it is out of date. */
    fun spacecraft(): Flow<PagingData<Spacecraft>> =
        assetStore.spacecraft().map { page -> page.map { it.toSpacecraft() } }

    /** One stored rocket configuration, or null while it isn't stored. */
    fun rocket(id: String): Flow<Rocket?> =
        rockets.map { rockets -> rockets?.firstOrNull { it.id.toString() == id } }

    /** One stored spacecraft design, or null while it isn't stored. */
    fun spacecraftConfiguration(id: String): Flow<SpacecraftConfiguration?> =
        spacecraftConfigurations.map { configs -> configs?.firstOrNull { it.id.toString() == id } }

    /** One stored booster, or null while it isn't stored. */
    fun launcher(id: String): Flow<Launcher?> = assetStore.launcher(id).map { it?.toLauncher() }

    /** One stored spacecraft, or null while it isn't stored. */
    fun spacecraft(id: String): Flow<Spacecraft?> =
        assetStore.spacecraft(id).map { it?.toSpacecraft() }
}
