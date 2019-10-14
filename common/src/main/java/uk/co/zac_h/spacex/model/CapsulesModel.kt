package uk.co.zac_h.spacex.model

import com.squareup.moshi.Json

data class CapsulesModel(
    @field:Json(name = "capsule_serial") var serial: String,
    @field:Json(name = "capsule_id") var id: String,
    @field:Json(name = "status") var status: String,
    @field:Json(name = "original_launch") var originalLaunch: String?,
    @field:Json(name = "original_launch_unix") var originalLaunchUnix: Long?,
    @field:Json(name = "missions") var missions: List<MissionsModel>,
    @field:Json(name = "landings") var landings: Int,
    @field:Json(name = "type") var type: String,
    @field:Json(name = "details") var details: String,
    @field:Json(name = "reuse_count") var reuseCount: Int
)