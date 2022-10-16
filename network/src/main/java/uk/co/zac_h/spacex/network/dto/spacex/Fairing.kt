package uk.co.zac_h.spacex.network.dto.spacex

import com.squareup.moshi.Json
import uk.co.zac_h.spacex.network.*

data class FairingResponse(
    @field:Json(name = SPACEX_FIELD_FAIRING_SERIAL) val serial: String,
    @field:Json(name = SPACEX_FIELD_FAIRING_VERSION) val version: String?,
    @field:Json(name = SPACEX_FIELD_FAIRING_STATUS) val status: String?,
    @field:Json(name = SPACEX_FIELD_FAIRING_REUSE_COUNT) val reuseCount: Int?,
    @field:Json(name = SPACEX_FIELD_FAIRING_NET_LANDING_ATTEMPTS) val netLandingAttempts: Int?,
    @field:Json(name = SPACEX_FIELD_FAIRING_NET_LANDING) val netLanding: Int?,
    @field:Json(name = SPACEX_FIELD_FAIRING_WATER_LANDING_ATTEMPTS) val waterLandingAttempts: Int?,
    @field:Json(name = SPACEX_FIELD_FAIRING_WATER_LANDING) val waterLanding: Int?,
    @field:Json(name = SPACEX_FIELD_FAIRING_LAST_UPDATE) val lastUpdate: String?,
    @field:Json(name = SPACEX_FIELD_FAIRING_LAUNCHES) val launches: List<String>?
)

data class FairingQueriedResponse(
    @field:Json(name = SPACEX_FIELD_FAIRING_SERIAL) val serial: String,
    @field:Json(name = SPACEX_FIELD_FAIRING_VERSION) val version: String?,
    @field:Json(name = SPACEX_FIELD_FAIRING_STATUS) val status: String?,
    @field:Json(name = SPACEX_FIELD_FAIRING_REUSE_COUNT) val reuseCount: Int?,
    @field:Json(name = SPACEX_FIELD_FAIRING_NET_LANDING_ATTEMPTS) val netLandingAttempts: Int?,
    @field:Json(name = SPACEX_FIELD_FAIRING_NET_LANDING) val netLanding: Int?,
    @field:Json(name = SPACEX_FIELD_FAIRING_WATER_LANDING_ATTEMPTS) val waterLandingAttempts: Int?,
    @field:Json(name = SPACEX_FIELD_FAIRING_WATER_LANDING) val waterLanding: Int?,
    @field:Json(name = SPACEX_FIELD_FAIRING_LAST_UPDATE) val lastUpdate: String?,
    @field:Json(name = SPACEX_FIELD_FAIRING_LAUNCHES) val launches: List<LegacyLaunchResponse>?
)

/*data class Fairing(
    val serial: String,
    val version: FairingVersion?,
    val status: FairingStatus?,
    val reuseCount: Int?,
    val netLandingAttempts: Int?,
    val netLanding: Int?,
    val waterLandingAttempts: Int?,
    val waterLanding: Int?,
    val lastUpdate: String?,
    val launchIds: List<String>? = null,
    val launches: List<Launch>? = null
) {

    constructor(
        response: FairingResponse
    ) : this(
        serial = response.serial,
        version = response.version.toFairingVersion(),
        status = response.status.toFairingStatus(),
        reuseCount = response.reuseCount,
        netLandingAttempts = response.netLandingAttempts,
        netLanding = response.netLanding,
        waterLandingAttempts = response.waterLandingAttempts,
        waterLanding = response.waterLanding,
        lastUpdate = response.lastUpdate,
        launchIds = response.launches
    )

    constructor(
        response: FairingQueriedResponse
    ) : this(
        serial = response.serial,
        version = response.version.toFairingVersion(),
        status = response.status.toFairingStatus(),
        reuseCount = response.reuseCount,
        netLandingAttempts = response.netLandingAttempts,
        netLanding = response.netLanding,
        waterLandingAttempts = response.waterLandingAttempts,
        waterLanding = response.waterLanding,
        lastUpdate = response.lastUpdate,
        launches = response.launches?.map { Launch(it) }
    )

    companion object {
        private fun String?.toFairingStatus() = when (this) {
            SPACEX_ACTIVE -> FairingStatus.ACTIVE
            SPACEX_INACTIVE -> FairingStatus.INACTIVE
            SPACEX_EXPENDED -> FairingStatus.EXPENDED
            SPACEX_LOST -> FairingStatus.LOST
            SPACEX_RETIRED -> FairingStatus.RETIRED
            else -> FairingStatus.UNKNOWN
        }

        private fun String?.toFairingVersion() = when (this) {
            SPACEX_FAIRING_VERSION_1 -> FairingVersion.FAIRING_1
            SPACEX_FAIRING_VERSION_2 -> FairingVersion.FAIRING_2
            SPACEX_FAIRING_VERSION_2_1 -> FairingVersion.FAIRING_2_1
            else -> FairingVersion.UNKNOWN
        }
    }
}*/

enum class FairingStatus(val status: String) {
    ACTIVE(SPACEX_ACTIVE),
    INACTIVE(SPACEX_INACTIVE),
    UNKNOWN(SPACEX_UNKNOWN),
    EXPENDED(SPACEX_EXPENDED),
    LOST(SPACEX_LOST),
    RETIRED(SPACEX_RETIRED)
}

enum class FairingVersion(val version: String) {
    FAIRING_1(SPACEX_FAIRING_VERSION_1),
    FAIRING_2(SPACEX_FAIRING_VERSION_2),
    FAIRING_2_1(SPACEX_FAIRING_VERSION_2_1),
    UNKNOWN(SPACEX_UNKNOWN)
}
