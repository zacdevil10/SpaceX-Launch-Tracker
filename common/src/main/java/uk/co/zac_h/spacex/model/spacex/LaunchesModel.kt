package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

// v4
data class LaunchesDocsModel(
    @field:Json(name = "docs") val docs: List<LaunchesModel>
)

@Parcelize
data class LaunchesModel(
    @field:Json(name = "flight_number") val flightNumber: Int,
    @field:Json(name = "name") val missionName: String,
    @field:Json(name = "date_utc") val launchDateUtc: String,
    @field:Json(name = "date_unix") val launchDateUnix: Long,
    @field:Json(name = "date_local") val launchDateLocal: String,
    @field:Json(name = "date_precision") val datePrecision: String,
    @field:Json(name = "static_fire_date_utc") val staticFireDateUtc: String?,
    @field:Json(name = "static_fire_date_unix") val staticFireDateUnix: Long?,
    @field:Json(name = "tbd") val tbd: Boolean,
    @field:Json(name = "net") val net: Boolean,
    @field:Json(name = "window") val window: Int?,
    @field:Json(name = "rocket") val rocket: String?,
    @field:Json(name = "success") val success: Boolean?,
    @field:Json(name = "failures") val failures: List<String>,
    @field:Json(name = "upcoming") val upcoming: Boolean,
    @field:Json(name = "details") val details: String?,
    @field:Json(name = "fairings") val fairings: FairingsModel?,
    @field:Json(name = "crew") val crew: List<String>,
    @field:Json(name = "ships") val ships: List<String>,
    @field:Json(name = "capsules") val capsules: List<String>,
    @field:Json(name = "payloads") val payloads: List<PayloadModel>,
    @field:Json(name = "launchpad") val launchpad: String?,
    @field:Json(name = "cores") val cores: List<LaunchCoreModel>,
    @field:Json(name = "links") val links: LaunchLinksModel?,
    @field:Json(name = "auto_update") val autoUpdate: Boolean,
    @field:Json(name = "id") val id: String
) : Parcelable