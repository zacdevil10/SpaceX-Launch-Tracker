package uk.co.zac_h.spacex.utils.data

import com.squareup.moshi.Json

data class CoreModel(
    @field:Json(name = "core_serial") val serial: String,
    @field:Json(name = "block") val block: String,
    @field:Json(name = "status") val status: String,
    @field:Json(name = "original_launch") val originalLaunchDate: String,
    @field:Json(name = "original_launch_unix") val originalLaunchDateUnix: Long,
    @field:Json(name = "missions") val missions: List<CoreMissionsModel>,
    @field:Json(name = "reuse_count") val reuseCount: Int,
    @field:Json(name = "rtls_attempts") val attemptsRtls: Int,
    @field:Json(name = "rtls_landings") val landingsRtls: Int,
    @field:Json(name = "asds_attempts") val attemptsAsds: Int,
    @field:Json(name = "asds_landings") val landingsAsds: Int,
    @field:Json(name = "water_landing") val landingWater: Boolean,
    @field:Json(name = "details") val details: String
)

data class CoreMissionsModel(
    @field:Json(name = "name") val name: String,
    @field:Json(name = "flight") val flightNumber: Int
)