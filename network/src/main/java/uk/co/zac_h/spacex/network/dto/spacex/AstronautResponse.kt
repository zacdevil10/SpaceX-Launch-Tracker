package uk.co.zac_h.spacex.network.dto.spacex

import com.squareup.moshi.Json

data class AstronautResponse(
    val id: Int,
    val name: String,
    val status: Status?,
    val agency: Agency?,
    val nationality: String?,
    val bio: String?,
    @field:Json(name = "profile_image") val image: String?,
    @field:Json(name = "first_flight") val firstFlight: String?
) {

    data class Status(
        val id: Int,
        val name: String?,
    )

    data class Agency(
        val id: Int,
        val name: String?
    )
}
