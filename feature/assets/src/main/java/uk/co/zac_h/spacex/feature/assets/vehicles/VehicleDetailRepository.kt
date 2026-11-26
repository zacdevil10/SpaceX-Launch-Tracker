package uk.co.zac_h.spacex.feature.assets.vehicles

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uk.co.zac_h.spacex.feature.assets.astronauts.AstronautItem
import uk.co.zac_h.spacex.feature.assets.vehicles.dragon.SecondStageItem
import uk.co.zac_h.spacex.feature.assets.vehicles.launcher.CoreItem
import uk.co.zac_h.spacex.feature.assets.vehicles.rockets.RocketItem
import uk.co.zac_h.spacex.feature.assets.vehicles.spacecraft.SpacecraftItem
import uk.co.zac_h.spacex.network.store.AssetStore
import javax.inject.Inject

class VehicleDetailRepository @Inject constructor(
    private val assetStore: AssetStore
) {

    /**
     * One stored asset by its type and id, or null while it isn't stored. Every asset the lists
     * show is stored, so one opened from them is always found.
     */
    fun vehicle(type: VehicleType, id: String): Flow<VehicleItem?> = when (type) {
        VehicleType.ROCKET -> assetStore.agency.map { agency ->
            agency?.launcherList?.firstOrNull { it.id.toString() == id }?.let(::RocketItem)
        }

        VehicleType.SECOND_STAGE -> assetStore.agency.map { agency ->
            agency?.spacecraftList?.firstOrNull { it.id.toString() == id }?.let(::SecondStageItem)
        }

        VehicleType.CORE -> assetStore.launcher(id).map { it?.let(::CoreItem) }
        VehicleType.CAPSULE -> assetStore.spacecraft(id).map { it?.let(::SpacecraftItem) }
        VehicleType.ASTRONAUT -> assetStore.astronaut(id).map { it?.let(::AstronautItem) }
    }
}
