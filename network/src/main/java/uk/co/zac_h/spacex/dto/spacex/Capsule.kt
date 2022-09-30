package uk.co.zac_h.spacex.dto.spacex

import com.squareup.moshi.Json
import uk.co.zac_h.spacex.*

data class CapsuleQueriedResponse(
    @field:Json(name = SPACEX_FIELD_CAPSULE_SERIAL) val serial: String?,
    @field:Json(name = SPACEX_FIELD_CAPSULE_STATUS) val status: String?,
    @field:Json(name = SPACEX_FIELD_CAPSULE_TYPE) val type: String?,
    @field:Json(name = SPACEX_FIELD_CAPSULE_DRAGON) val dragon: String?,
    @field:Json(name = SPACEX_FIELD_CAPSULE_REUSE_COUNT) val reuseCount: Int?,
    @field:Json(name = SPACEX_FIELD_CAPSULE_WATER_LANDINGS) val waterLandings: Int?,
    @field:Json(name = SPACEX_FIELD_CAPSULE_LAND_LANDINGS) val landLandings: Int?,
    @field:Json(name = SPACEX_FIELD_CAPSULE_LAST_UPDATE) val lastUpdate: String?,
    @field:Json(name = SPACEX_FIELD_CAPSULE_LAUNCHES) val launches: List<LaunchResponse>?,
    @field:Json(name = SPACEX_FIELD_ID) val id: String
)

data class CapsuleResponse(
    @field:Json(name = SPACEX_FIELD_CAPSULE_SERIAL) val serial: String?,
    @field:Json(name = SPACEX_FIELD_CAPSULE_STATUS) val status: String?,
    @field:Json(name = SPACEX_FIELD_CAPSULE_TYPE) val type: String?,
    @field:Json(name = SPACEX_FIELD_CAPSULE_DRAGON) val dragon: String?,
    @field:Json(name = SPACEX_FIELD_CAPSULE_REUSE_COUNT) val reuseCount: Int?,
    @field:Json(name = SPACEX_FIELD_CAPSULE_WATER_LANDINGS) val waterLandings: Int?,
    @field:Json(name = SPACEX_FIELD_CAPSULE_LAND_LANDINGS) val landLandings: Int?,
    @field:Json(name = SPACEX_FIELD_CAPSULE_LAST_UPDATE) val lastUpdate: String?,
    @field:Json(name = SPACEX_FIELD_CAPSULE_LAUNCHES) val launches: List<String>?,
    @field:Json(name = SPACEX_FIELD_ID) val id: String
)

enum class CapsuleStatus(val status: String) {
    UNKNOWN(SPACEX_UNKNOWN),
    ACTIVE(SPACEX_ACTIVE),
    RETIRED(SPACEX_RETIRED),
    DESTROYED(SPACEX_DESTROYED)
}

enum class CapsuleType(val type: String) {
    DRAGON_1(SPACEX_CAPSULE_TYPE_DRAGON_1),
    DRAGON_1_1(SPACEX_CAPSULE_TYPE_DRAGON_1_1),
    DRAGON_2(SPACEX_CAPSULE_TYPE_DRAGON_2),
    UNKNOWN(SPACEX_UNKNOWN)
}
