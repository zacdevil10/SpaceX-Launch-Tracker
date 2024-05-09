package uk.co.zac_h.spacex.network.dto.spacex

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AstronautResponse(
    val id: Int,
    val name: String,
    val status: Status?,
    val agency: Agency?,
    val nationality: String?,
    val bio: String?,
    @SerialName("profile_image") val image: String?,
    @SerialName("first_flight") val firstFlight: String?
) {

    @Serializable
    data class Status(
        val id: Int,
        val name: String?,
    )

    @Serializable
    data class Agency(
        val id: Int,
        val name: String?
    )
}
