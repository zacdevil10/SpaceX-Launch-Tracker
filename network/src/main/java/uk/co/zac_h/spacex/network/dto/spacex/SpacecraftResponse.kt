package uk.co.zac_h.spacex.network.dto.spacex

import com.squareup.moshi.Json

data class SpacecraftResponse(
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "serial_number") val serialNumber: String?,
    @field:Json(name = "status") val status: Status?,
    @field:Json(name = "description") val description: String?,
    @field:Json(name = "spacecraft_config") val spacecraftConfig: SpacecraftConfig?
) {

    data class Status(
        @field:Json(name = "id") val id: Int,
        @field:Json(name = "name") val name: String?,
    )

    data class SpacecraftConfig(
        @field:Json(name = "id") val id: Int,
        @field:Json(name = "name") val name: String?,
        @field:Json(name = "image_url") val imageUrl: String?
    )
}
