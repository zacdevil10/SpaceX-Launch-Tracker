package uk.co.zac_h.spacex.network.dto.spacex

import com.squareup.moshi.Json

data class LauncherResponse(
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "serial_number") val serialNumber: String?,
    @field:Json(name = "status") val status: String?,
    @field:Json(name = "details") val details: String?,
    @field:Json(name = "launcher_config") val launcherConfig: LauncherConfig?,
    @field:Json(name = "image_url") val imageUrl: String?,
    @field:Json(name = "flights") val flights: Int?,
    @field:Json(name = "last_launch_date") val lastLaunchDate: String?,
    @field:Json(name = "first_launch_date") val firstLaunchDate: String?
) {

    data class LauncherConfig(
        @field:Json(name = "id") val id: Int,
        @field:Json(name = "name") val name: String?,
        @field:Json(name = "family") val family: String?,
        @field:Json(name = "full_name") val fullName: String?,
        @field:Json(name = "variant") val variant: String?
    )
}