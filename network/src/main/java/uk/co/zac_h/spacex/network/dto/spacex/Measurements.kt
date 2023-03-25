package uk.co.zac_h.spacex.network.dto.spacex

import com.squareup.moshi.Json

data class Dimens(
    @field:Json(name = "meters") val meters: Double?,
    @field:Json(name = "feet") val feet: Double?
)

data class Mass(
    @field:Json(name = "kg") var kg: Float?,
    @field:Json(name = "lb") var lb: Float?
)

data class Thrust(
    @field:Json(name = "kN") val kN: Float?,
    @field:Json(name = "lbf") val lbf: Float?
)

data class SpecificImpulse(
    @field:Json(name = "sea_level") val seaLevel: Int?,
    @field:Json(name = "vacuum") val vacuum: Int?
)
