package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

data class CapsulesDocsModel(
    @field:Json(name = "docs") val docs: List<CapsulesModel>
)

@Parcelize
data class CapsulesModel(
    @field:Json(name = "reuse_count") val reuseCount: Int?,
    @field:Json(name = "water_landings") val waterLandings: Int?,
    @field:Json(name = "land_landings") val landLandings: Int?,
    @field:Json(name = "last_update") val lastUpdate: String?,
    @field:Json(name = "launches") val launches: List<CapsuleLaunchesModel>,
    @field:Json(name = "serial") val serial: String?,
    @field:Json(name = "status") val status: String?,
    @field:Json(name = "id") val id: String
) : Parcelable

@Parcelize
data class CapsuleLaunchesModel(
    @field:Json(name = "flight_number") val flightNumber: Int?,
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "id") val id: String
) : Parcelable