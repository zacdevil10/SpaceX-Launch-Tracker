package uk.co.zac_h.spacex.network.dto.spacex

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LauncherResponse(
    val id: Int,
    @SerialName("serial_number") val serialNumber: String?,
    val status: String?,
    val details: String?,
    @SerialName("launcher_config") val launcherConfig: LauncherConfig?,
    @SerialName("image_url") val imageUrl: String?,
    val flights: Int?,
    @SerialName("last_launch_date") val lastLaunchDate: String?,
    @SerialName("first_launch_date") val firstLaunchDate: String?
) {

    @Serializable
    data class LauncherConfig(
        val id: Int,
        val name: String?,
        val family: String?,
        @SerialName("full_name") val fullName: String?,
        val variant: String?
    )
}
