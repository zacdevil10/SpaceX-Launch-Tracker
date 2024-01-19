package uk.co.zac_h.spacex.network.dto.spacex

import com.squareup.moshi.Json

data class LauncherResponse(
    val id: Int,
    @field:Json(name = "serial_number") val serialNumber: String?,
    val status: String?,
    val details: String?,
    @field:Json(name = "launcher_config") val launcherConfig: LauncherConfig?,
    @field:Json(name = "image_url") val imageUrl: String?,
    val flights: Int?,
    @field:Json(name = "last_launch_date") val lastLaunchDate: String?,
    @field:Json(name = "first_launch_date") val firstLaunchDate: String?
) {

    data class LauncherConfig(
        val id: Int,
        val name: String?,
        val family: String?,
        @field:Json(name = "full_name") val fullName: String?,
        val variant: String?
    )
}
