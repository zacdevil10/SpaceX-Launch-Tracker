package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

data class ShipsDocsModel(
    @field:Json(name = "docs") val docs: List<ShipQueriedResponse>
)

data class ShipResponse(
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "legacy_id") val legacyId: String?,
    @field:Json(name = "model") val model: String?,
    @field:Json(name = "type") val type: String?,
    @field:Json(name = "roles") val roles: List<String>?,
    @field:Json(name = "active") val active: Boolean?,
    @field:Json(name = "imo") val imo: Int?,
    @field:Json(name = "mmsi") val mmsi: Int?,
    @field:Json(name = "abs") val abs: Int?,
    @field:Json(name = "class") val shipClass: Int?,
    @field:Json(name = "mass_kg") val massKg: Int?,
    @field:Json(name = "mass_lbs") val massLbs: Int?,
    @field:Json(name = "year_built") val yearBuilt: Int?,
    @field:Json(name = "home_port") val homePort: String?,
    @field:Json(name = "status") val status: String?,
    @field:Json(name = "speed_kn") val speed: Int?,
    @field:Json(name = "course_deg") val course: Int?,
    @field:Json(name = "latitude") val lat: Float?,
    @field:Json(name = "longitude") val lng: Float?,
    @field:Json(name = "last_ais_update") val lastUpdate: String?,
    @field:Json(name = "link") val link: String?,
    @field:Json(name = "image") val image: String?,
    @field:Json(name = "launches") val launches: List<String>?,
    @field:Json(name = "id") val id: String
)

data class ShipQueriedResponse(
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "legacy_id") val legacyId: String?,
    @field:Json(name = "model") val model: String?,
    @field:Json(name = "type") val type: String?,
    @field:Json(name = "roles") val roles: List<String>?,
    @field:Json(name = "active") val active: Boolean?,
    @field:Json(name = "imo") val imo: Int?,
    @field:Json(name = "mmsi") val mmsi: Int?,
    @field:Json(name = "abs") val abs: Int?,
    @field:Json(name = "class") val shipClass: Int?,
    @field:Json(name = "mass_kg") val massKg: Int?,
    @field:Json(name = "mass_lbs") val massLbs: Int?,
    @field:Json(name = "year_built") val yearBuilt: Int?,
    @field:Json(name = "home_port") val homePort: String?,
    @field:Json(name = "status") val status: String?,
    @field:Json(name = "speed_kn") val speed: Int?,
    @field:Json(name = "course_deg") val course: Int?,
    @field:Json(name = "latitude") val lat: Float?,
    @field:Json(name = "longitude") val lng: Float?,
    @field:Json(name = "last_ais_update") val lastUpdate: String?,
    @field:Json(name = "link") val link: String?,
    @field:Json(name = "image") val image: String?,
    @field:Json(name = "launches") val launches: List<LaunchResponse>?,
    @field:Json(name = "id") val id: String
)

@Parcelize
data class Ship(
    val name: String?,
    val legacyId: String?,
    val model: String?,
    val type: String?,
    val roles: List<String>?,
    val active: Boolean?,
    val imo: Int?,
    val mmsi: Int?,
    val abs: Int?,
    val shipClass: Int?,
    val massKg: Int?,
    val massLbs: Int?,
    val yearBuilt: Int?,
    val homePort: String?,
    val status: String?,
    val speed: Int?,
    val course: Int?,
    val lat: Float?,
    val lng: Float?,
    val lastUpdate: String?,
    val link: String?,
    val image: String?,
    val launchIds: List<String>? = null,
    val launches: List<Launch>? = null,
    val id: String
) : Parcelable {

    constructor(
        response: ShipResponse
    ) : this(
        name = response.name,
        legacyId = response.legacyId,
        model = response.model,
        type = response.type,
        roles = response.roles,
        active = response.active,
        imo = response.imo,
        mmsi = response.mmsi,
        abs = response.abs,
        shipClass = response.shipClass,
        massKg = response.massKg,
        massLbs = response.massLbs,
        yearBuilt = response.yearBuilt,
        homePort = response.homePort,
        status = response.status,
        speed = response.speed,
        course = response.course,
        lat = response.lat,
        lng = response.lng,
        lastUpdate = response.lastUpdate,
        link = response.link,
        image = response.image,
        launchIds = response.launches,
        id = response.id
    )

    constructor(
        response: ShipQueriedResponse
    ) : this(
        name = response.name,
        legacyId = response.legacyId,
        model = response.model,
        type = response.type,
        roles = response.roles,
        active = response.active,
        imo = response.imo,
        mmsi = response.mmsi,
        abs = response.abs,
        shipClass = response.shipClass,
        massKg = response.massKg,
        massLbs = response.massLbs,
        yearBuilt = response.yearBuilt,
        homePort = response.homePort,
        status = response.status,
        speed = response.speed,
        course = response.course,
        lat = response.lat,
        lng = response.lng,
        lastUpdate = response.lastUpdate,
        link = response.link,
        image = response.image,
        launches = response.launches?.map { Launch(it) },
        id = response.id
    )

}