package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShipModel(
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "legacy_id") val legacyId: String?,
    @field:Json(name = "model") val model: String?,
    @field:Json(name = "type") val type: String?,
    @field:Json(name = "role") val roles: List<String>?,
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
) : Parcelable