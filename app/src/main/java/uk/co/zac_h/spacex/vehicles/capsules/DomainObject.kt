package uk.co.zac_h.spacex.vehicles.capsules

import uk.co.zac_h.spacex.launches.Launch
import uk.co.zac_h.spacex.network.*
import uk.co.zac_h.spacex.network.dto.spacex.CapsuleQueriedResponse
import uk.co.zac_h.spacex.network.dto.spacex.CapsuleResponse
import uk.co.zac_h.spacex.network.dto.spacex.CapsuleStatus
import uk.co.zac_h.spacex.network.dto.spacex.CapsuleType

data class Capsule(
    val serial: String?,
    val status: CapsuleStatus?,
    val type: CapsuleType?,
    val dragon: String?,
    val reuseCount: Int?,
    val waterLandings: Int?,
    val landLandings: Int?,
    val lastUpdate: String?,
    val launchIds: List<String>? = null,
    val launches: List<Launch>? = null,
    val id: String
) {

    constructor(
        response: CapsuleResponse
    ) : this(
        serial = response.serial,
        status = response.status.toCapsuleStatus(),
        type = response.type.toCapsuleType(),
        dragon = response.dragon,
        reuseCount = response.reuseCount,
        waterLandings = response.waterLandings,
        landLandings = response.landLandings,
        lastUpdate = response.lastUpdate,
        launchIds = response.launches,
        id = response.id
    )

    constructor(
        response: CapsuleQueriedResponse
    ) : this(
        serial = response.serial,
        status = response.status.toCapsuleStatus(),
        type = response.type.toCapsuleType(),
        dragon = response.dragon,
        reuseCount = response.reuseCount,
        waterLandings = response.waterLandings,
        landLandings = response.landLandings,
        lastUpdate = response.lastUpdate,
        launches = response.launches?.map { Launch(it) },
        id = response.id
    )

    companion object {
        private fun String?.toCapsuleStatus() = when (this) {
            SPACEX_CAPSULE_STATUS_ACTIVE -> CapsuleStatus.ACTIVE
            SPACEX_CAPSULE_STATUS_RETIRED -> CapsuleStatus.RETIRED
            SPACEX_CAPSULE_STATUS_DESTROYED -> CapsuleStatus.DESTROYED
            else -> CapsuleStatus.UNKNOWN
        }

        private fun String?.toCapsuleType() = when (this) {
            SPACEX_CAPSULE_TYPE_DRAGON_1 -> CapsuleType.DRAGON_1
            SPACEX_CAPSULE_TYPE_DRAGON_1_1 -> CapsuleType.DRAGON_1_1
            SPACEX_CAPSULE_TYPE_DRAGON_2 -> CapsuleType.DRAGON_2
            else -> CapsuleType.UNKNOWN
        }
    }
}
