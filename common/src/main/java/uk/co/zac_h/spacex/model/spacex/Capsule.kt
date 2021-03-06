package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import uk.co.zac_h.spacex.utils.*

data class CapsulesDocsModel(
    @field:Json(name = "docs") val docs: List<CapsuleQueriedResponse>
)

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

@Parcelize
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
) : Parcelable {

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