package uk.co.zac_h.spacex.dto.spacex

import com.squareup.moshi.Json
import uk.co.zac_h.spacex.*

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
    @field:Json(name = SPACEX_FIELD_CORE_LAUNCHES) val launches: List<LegacyLaunchResponse>?,
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
    @field:Json(name = SPACEX_FIELD_CORE_LAUNCHES) val launches: List<String>?,
    @field:Json(name = SPACEX_FIELD_ID) val id: String
)

enum class CoreStatus(val status: String) {
    ACTIVE(SPACEX_ACTIVE),
    INACTIVE(SPACEX_INACTIVE),
    UNKNOWN(SPACEX_UNKNOWN),
    EXPENDED(SPACEX_EXPENDED),
    LOST(SPACEX_LOST),
    RETIRED(SPACEX_RETIRED)
}
