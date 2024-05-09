package uk.co.zac_h.spacex.network.dto.spacex

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpacecraftResponse(
    val id: Int,
    val name: String?,
    @SerialName("serial_number") val serialNumber: String?,
    val status: Status?,
    val description: String?,
    @SerialName("spacecraft_config") val spacecraftConfig: SpacecraftConfig?
) {

    @Serializable
    data class Status(
        val id: Int,
        val name: String?,
    )

    @Serializable
    data class SpacecraftConfig(
        val id: Int,
        val name: String?,
        @SerialName("image_url") val imageUrl: String?
    )
}
