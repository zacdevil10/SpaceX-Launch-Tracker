package uk.co.zac_h.spacex.utils.data

import com.squareup.moshi.Json

data class SiteLocationModel(
    @field:Json(name = "name") var name: String,
    @field:Json(name = "region") var region: String,
    @field:Json(name = "latitude") var lat: Float,
    @field:Json(name = "longitude") var lng: Float
)