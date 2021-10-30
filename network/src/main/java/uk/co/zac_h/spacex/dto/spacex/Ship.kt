package uk.co.zac_h.spacex.dto.spacex

import com.squareup.moshi.Json
import uk.co.zac_h.spacex.*

data class ShipsDocsModel(
    @field:Json(name = "docs") val docs: List<ShipQueriedResponse>
)

data class ShipResponse(
    @field:Json(name = SPACEX_FIELD_SHIP_NAME) val name: String?,
    @field:Json(name = SPACEX_FIELD_SHIP_LEGACY_ID) val legacyId: String?,
    @field:Json(name = SPACEX_FIELD_SHIP_MODEL) val model: String?,
    @field:Json(name = SPACEX_FIELD_SHIP_TYPE) val type: String?,
    @field:Json(name = SPACEX_FIELD_SHIP_ROLES) val roles: List<String>?,
    @field:Json(name = SPACEX_FIELD_SHIP_ACTIVE) val active: Boolean?,
    @field:Json(name = SPACEX_FIELD_SHIP_IMO) val imo: Int?,
    @field:Json(name = SPACEX_FIELD_SHIP_MMSI) val mmsi: Int?,
    @field:Json(name = SPACEX_FIELD_SHIP_ABS) val abs: Int?,
    @field:Json(name = SPACEX_FIELD_SHIP_CLASS) val shipClass: Int?,
    @field:Json(name = SPACEX_FIELD_SHIP_MASS_KG) val massKg: Float?,
    @field:Json(name = SPACEX_FIELD_SHIP_MASS_LBS) val massLbs: Float?,
    @field:Json(name = SPACEX_FIELD_SHIP_YEAR_BUILT) val yearBuilt: Int?,
    @field:Json(name = SPACEX_FIELD_SHIP_HOME_PORT) val homePort: String?,
    @field:Json(name = SPACEX_FIELD_SHIP_STATUS) val status: String?,
    @field:Json(name = SPACEX_FIELD_SHIP_SPEED_KN) val speed: Int?,
    @field:Json(name = SPACEX_FIELD_SHIP_COURSE_DEG) val course: Int?,
    @field:Json(name = SPACEX_FIELD_SHIP_LATITUDE) val lat: Float?,
    @field:Json(name = SPACEX_FIELD_SHIP_LONGITUDE) val lng: Float?,
    @field:Json(name = SPACEX_FIELD_SHIP_LAST_AIS_UPDATE) val lastUpdate: String?,
    @field:Json(name = SPACEX_FIELD_SHIP_LINK) val link: String?,
    @field:Json(name = SPACEX_FIELD_SHIP_IMAGE) val image: String?,
    @field:Json(name = SPACEX_FIELD_SHIP_LAUNCHES) val launches: List<String>?,
    @field:Json(name = SPACEX_FIELD_ID) val id: String
)

data class ShipQueriedResponse(
    @field:Json(name = SPACEX_FIELD_SHIP_NAME) val name: String?,
    @field:Json(name = SPACEX_FIELD_SHIP_LEGACY_ID) val legacyId: String?,
    @field:Json(name = SPACEX_FIELD_SHIP_MODEL) val model: String?,
    @field:Json(name = SPACEX_FIELD_SHIP_TYPE) val type: String?,
    @field:Json(name = SPACEX_FIELD_SHIP_ROLES) val roles: List<String>?,
    @field:Json(name = SPACEX_FIELD_SHIP_ACTIVE) val active: Boolean?,
    @field:Json(name = SPACEX_FIELD_SHIP_IMO) val imo: Int?,
    @field:Json(name = SPACEX_FIELD_SHIP_MMSI) val mmsi: Int?,
    @field:Json(name = SPACEX_FIELD_SHIP_ABS) val abs: Int?,
    @field:Json(name = SPACEX_FIELD_SHIP_CLASS) val shipClass: Int?,
    @field:Json(name = SPACEX_FIELD_SHIP_MASS_KG) val massKg: Float?,
    @field:Json(name = SPACEX_FIELD_SHIP_MASS_LBS) val massLbs: Float?,
    @field:Json(name = SPACEX_FIELD_SHIP_YEAR_BUILT) val yearBuilt: Int?,
    @field:Json(name = SPACEX_FIELD_SHIP_HOME_PORT) val homePort: String?,
    @field:Json(name = SPACEX_FIELD_SHIP_STATUS) val status: String?,
    @field:Json(name = SPACEX_FIELD_SHIP_SPEED_KN) val speed: Int?,
    @field:Json(name = SPACEX_FIELD_SHIP_COURSE_DEG) val course: Int?,
    @field:Json(name = SPACEX_FIELD_SHIP_LATITUDE) val lat: Float?,
    @field:Json(name = SPACEX_FIELD_SHIP_LONGITUDE) val lng: Float?,
    @field:Json(name = SPACEX_FIELD_SHIP_LAST_AIS_UPDATE) val lastUpdate: String?,
    @field:Json(name = SPACEX_FIELD_SHIP_LINK) val link: String?,
    @field:Json(name = SPACEX_FIELD_SHIP_IMAGE) val image: String?,
    @field:Json(name = SPACEX_FIELD_SHIP_LAUNCHES) val launches: List<LaunchResponse>?,
    @field:Json(name = SPACEX_FIELD_ID) val id: String
)