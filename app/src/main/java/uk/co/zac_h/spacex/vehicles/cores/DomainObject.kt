package uk.co.zac_h.spacex.vehicles.cores

import uk.co.zac_h.spacex.*
import uk.co.zac_h.spacex.dto.spacex.CoreQueriedResponse
import uk.co.zac_h.spacex.dto.spacex.CoreResponse
import uk.co.zac_h.spacex.dto.spacex.CoreStatus
import uk.co.zac_h.spacex.dto.spacex.LaunchResponse
import uk.co.zac_h.spacex.launches.Launch

data class Core(
    val serial: String?,
    val block: String?,
    val status: CoreStatus?,
    val reuseCount: Int?,
    val attemptsRtls: Int?,
    val landingsRtls: Int?,
    val attemptsAsds: Int?,
    val landingsAsds: Int?,
    val lastUpdate: String?,
    val launchIds: List<String>? = null,
    val launches: List<Launch>? = null,
    val id: String
) {

    constructor(
        response: LaunchResponse.Rocket.LauncherStage.Launcher
    ) : this(
        serial = response.serialNumber,
        block = null,
        status = response.status?.toCoreStatus(),
        reuseCount = response.flights,
        attemptsRtls = null,
        landingsRtls = null,
        attemptsAsds = null,
        landingsAsds = null,
        lastUpdate = null,
        id = response.id.toString()
    )

    constructor(
        response: CoreResponse
    ) : this(
        serial = response.serial,
        block = response.block,
        status = response.status.toCoreStatus(),
        reuseCount = response.reuseCount,
        attemptsRtls = response.attemptsRtls,
        landingsRtls = response.landingsRtls,
        attemptsAsds = response.attemptsAsds,
        landingsAsds = response.landingsAsds,
        lastUpdate = response.lastUpdate,
        launchIds = response.launches,
        id = response.id
    )

    constructor(
        response: CoreQueriedResponse
    ) : this(
        serial = response.serial,
        block = response.block,
        status = response.status.toCoreStatus(),
        reuseCount = response.reuseCount ?: 0,
        attemptsRtls = response.attemptsRtls ?: 0,
        landingsRtls = response.landingsRtls ?: 0,
        attemptsAsds = response.attemptsAsds ?: 0,
        landingsAsds = response.landingsAsds ?: 0,
        lastUpdate = response.lastUpdate,
        launches = response.launches?.map { Launch(it) },
        id = response.id
    )

    companion object {
        private fun String?.toCoreStatus() = when (this) {
            SPACEX_CORE_STATUS_ACTIVE -> CoreStatus.ACTIVE
            SPACEX_CORE_STATUS_INACTIVE -> CoreStatus.INACTIVE
            SPACEX_CORE_STATUS_EXPENDED -> CoreStatus.EXPENDED
            SPACEX_CORE_STATUS_LOST -> CoreStatus.LOST
            SPACEX_CORE_STATUS_RETIRED -> CoreStatus.RETIRED
            else -> CoreStatus.UNKNOWN
        }
    }
}
