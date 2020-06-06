package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LaunchesModel(
    @field:Json(name = "fairings") val fairings: FairingsModel?,
    @field:Json(name = "links") val links: LaunchLinksModel?,
    @field:Json(name = "static_fire_date_utc") val staticFireDateUtc: String?,
    @field:Json(name = "static_fire_date_unix") val staticFireDateUnix: Long?,
    @field:Json(name = "tbd") val tbd: Boolean,
    @field:Json(name = "net") val net: Boolean,
    @field:Json(name = "window") val window: Int?,
    @field:Json(name = "rocket") val rocket: String,
    @field:Json(name = "success") val success: Boolean?,
    @field:Json(name = "failures") val failures: List<String>,
    @field:Json(name = "details") val details: String?,
    @field:Json(name = "crew") val crew: List<String>,
    @field:Json(name = "ships") val ships: List<String>,
    @field:Json(name = "capsules") val capsules: List<String>,
    @field:Json(name = "payloads") val payloads: List<String>,
    @field:Json(name = "launchpad") val launchpad: String,
    @field:Json(name = "auto_update") val autoUpdate: Boolean,
    @field:Json(name = "flight_number") val flightNumber: Int,
    @field:Json(name = "name") val missionName: String,
    @field:Json(name = "date_utc") val launchDateUtc: String,
    @field:Json(name = "date_unix") val launchDateUnix: Long,
    @field:Json(name = "date_local") val launchDateLocal: String,
    @field:Json(name = "date_precision") val datePrecision: String,
    @field:Json(name = "upcoming") val upcoming: Boolean,
    @field:Json(name = "cores") val cores: List<LaunchCoreModel>,
    @field:Json(name = "id") val id: String

    /*
    *    v3
    */
    /*
    @field:Json(name = "flight_number") var flightNumber: Int,
    @field:Json(name = "mission_name") var missionName: String,
    @field:Json(name = "mission_id") var missionId: List<String>,
    @field:Json(name = "launch_date_unix") var launchDateUnix: Long,
    @field:Json(name = "is_tentative") var tentative: Boolean?,
    @field:Json(name = "tentative_max_precision") var tentativeMaxPrecision: String,
    @field:Json(name = "tbd") var tbd: Boolean?,
    @field:Json(name = "launch_window") var launchWindow: Int?,
    @field:Json(name = "rocket") var rocket: LaunchConfigModel,
    @field:Json(name = "ships") var ships: List<String>,
    //@field:Json(name = "telemetry") var telemetry: JSONObject,
    @field:Json(name = "launch_site") var launchSite: LaunchSiteModel,
    @field:Json(name = "launch_success") var success: Boolean?,
    @field:Json(name = "links") var links: LaunchLinksModel,
    @field:Json(name = "details") var details: String?,
    @field:Json(name = "upcoming") var upcoming: Boolean,
    @field:Json(name = "static_fire_date_utc") var staticFireDateUTC: String?,
    @field:Json(name = "static_fire_date_unix") var staticFireDateUnix: Long?
    //@field:Json(name = "timeline") var timeline: LaunchTimelineModel
    */
) : Parcelable