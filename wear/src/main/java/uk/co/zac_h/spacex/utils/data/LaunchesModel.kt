package uk.co.zac_h.spacex.utils.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LaunchesModel(
    @field:Json(name = "flight_number") var flightNumber: Int,
    @field:Json(name = "mission_name") var missionName: String,
    @field:Json(name = "mission_id") var missionId: List<String>,
    @field:Json(name = "launch_year") var launchYear: Int,
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
    @field:Json(name = "details") var details: String,
    @field:Json(name = "upcoming") var upcoming: Boolean?,
    @field:Json(name = "static_fire_date_utc") var staticFireDateUTC: String?,
    @field:Json(name = "static_fire_date_unix") var staticFireDateUnix: Long?
    //@field:Json(name = "timeline") var timeline: LaunchTimelineModel
) : Parcelable