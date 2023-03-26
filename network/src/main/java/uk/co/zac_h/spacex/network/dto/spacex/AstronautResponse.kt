package uk.co.zac_h.spacex.network.dto.spacex

import com.squareup.moshi.Json

data class AstronautResponse(
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "status") val status: Status?,
    @field:Json(name = "agency") val agency: Agency?,
    @field:Json(name = "nationality") val nationality: String?,
    @field:Json(name = "bio") val bio: String?,
    @field:Json(name = "profile_image") val image: String?,
    @field:Json(name = "first_flight") val firstFlight: String?
) {

    data class Status(
        @field:Json(name = "id") val id: Int,
        @field:Json(name = "name") val name: String?,
    )

    data class Agency(
        @field:Json(name = "id") val id: Int,
        @field:Json(name = "name") val name: String?
    )
}
