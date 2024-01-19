package uk.co.zac_h.spacex.network.dto.spacex

import com.squareup.moshi.Json

data class SpacecraftResponse(
    val id: Int,
    val name: String?,
    @field:Json(name = "serial_number") val serialNumber: String?,
    val status: Status?,
    val description: String?,
    @field:Json(name = "spacecraft_config") val spacecraftConfig: SpacecraftConfig?
) {

    data class Status(
        val id: Int,
        val name: String?,
    )

    data class SpacecraftConfig(
        val id: Int,
        val name: String?,
        @field:Json(name = "image_url") val imageUrl: String?
    )
}
