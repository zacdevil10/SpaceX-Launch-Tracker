package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import uk.co.zac_h.spacex.utils.*

//v4
data class CoreDocsModel(
    @field:Json(name = "docs") val docs: List<CoreQueriedResponse>
)

data class CoreQueriedResponse(
    @field:Json(name = SPACEX_FIELD_CORE_SERIAL) val serial: String?,
    @field:Json(name = SPACEX_FIELD_CORE_BLOCK) val block: String?,
    @field:Json(name = SPACEX_FIELD_CORE_STATUS) val status: String?,
    @field:Json(name = SPACEX_FIELD_CORE_REUSE_COUNT) val reuseCount: Int?,
    @field:Json(name = SPACEX_FIELD_CORE_RTLS_ATTEMPTS) val attemptsRtls: Int?,
    @field:Json(name = SPACEX_FIELD_CORE_RTLS_LANDING) val landingsRtls: Int?,
    @field:Json(name = SPACEX_FIELD_CORE_ASDS_ATTEMPTS) val attemptsAsds: Int?,
    @field:Json(name = SPACEX_FIELD_CORE_ASDS_LANDINGS) val landingsAsds: Int?,
    @field:Json(name = SPACEX_FIELD_CORE_LAST_UPDATE) val lastUpdate: String?,
    @field:Json(name = SPACEX_FIELD_CORE_LAUNCHES) val missions: List<Launch>?,
    @field:Json(name = SPACEX_FIELD_ID) val id: String
)

data class CoreResponse(
    @field:Json(name = SPACEX_FIELD_CORE_SERIAL) val serial: String?,
    @field:Json(name = SPACEX_FIELD_CORE_BLOCK) val block: String?,
    @field:Json(name = SPACEX_FIELD_CORE_STATUS) val status: String?,
    @field:Json(name = SPACEX_FIELD_CORE_REUSE_COUNT) val reuseCount: Int?,
    @field:Json(name = SPACEX_FIELD_CORE_RTLS_ATTEMPTS) val attemptsRtls: Int?,
    @field:Json(name = SPACEX_FIELD_CORE_RTLS_LANDING) val landingsRtls: Int?,
    @field:Json(name = SPACEX_FIELD_CORE_ASDS_ATTEMPTS) val attemptsAsds: Int?,
    @field:Json(name = SPACEX_FIELD_CORE_ASDS_LANDINGS) val landingsAsds: Int?,
    @field:Json(name = SPACEX_FIELD_CORE_LAST_UPDATE) val lastUpdate: String?,
    @field:Json(name = SPACEX_FIELD_CORE_LAUNCHES) val missions: List<String>?,
    @field:Json(name = SPACEX_FIELD_ID) val id: String
)

@Parcelize
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
    val missionIds: List<String>?,
    val missions: List<Launch>?,
    val id: String
) : Parcelable {

    constructor(
        response: CoreResponse
    ) : this(
        serial = response.serial,
        block = response.block,
        status = response.status?.toCoreStatus(),
        reuseCount = response.reuseCount,
        attemptsRtls = response.attemptsRtls,
        landingsRtls = response.landingsRtls,
        attemptsAsds = response.attemptsAsds,
        landingsAsds = response.landingsAsds,
        lastUpdate = response.lastUpdate,
        missionIds = response.missions,
        missions = null,
        id = response.id
    )

    constructor(
        response: CoreQueriedResponse
    ) : this(
        serial = response.serial,
        block = response.block,
        status = response.status?.toCoreStatus(),
        reuseCount = response.reuseCount,
        attemptsRtls = response.attemptsRtls,
        landingsRtls = response.landingsRtls,
        attemptsAsds = response.attemptsAsds,
        landingsAsds = response.landingsAsds,
        lastUpdate = response.lastUpdate,
        missionIds = null,
        missions = response.missions,
        id = response.id
    )

    companion object {
        private fun String.toCoreStatus() = when (this) {
            SPACEX_CORE_STATUS_ACTIVE -> CoreStatus.ACTIVE
            SPACEX_CORE_STATUS_INACTIVE -> CoreStatus.INACTIVE
            SPACEX_CORE_STATUS_UNKNOWN -> CoreStatus.UNKNOWN
            SPACEX_CORE_STATUS_EXPENDED -> CoreStatus.EXPENDED
            SPACEX_CORE_STATUS_LOST -> CoreStatus.LOST
            SPACEX_CORE_STATUS_RETIRED -> CoreStatus.RETIRED
            else -> throw IllegalStateException()
        }
    }
}

enum class CoreStatus {
    ACTIVE,
    INACTIVE,
    UNKNOWN,
    EXPENDED,
    LOST,
    RETIRED
}