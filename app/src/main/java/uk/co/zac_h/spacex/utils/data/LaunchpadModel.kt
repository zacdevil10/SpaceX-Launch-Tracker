package uk.co.zac_h.spacex.utils.data

import com.squareup.moshi.Json

data class LaunchpadModel(
    @field:Json(name = "id") var id: Int,
    @field:Json(name = "status") var status: String,
    @field:Json(name = "location") var location: LaunchpadLocationModel,
    @field:Json(name = "vehicles_launched") var vehicles: List<String>,
    @field:Json(name = "attempted_launches") var launchAttempts: Int,
    @field:Json(name = "successful_launches") var launchSuccesses: Int,
    @field:Json(name = "wikipedia") var wikiLink: String,
    @field:Json(name = "details") var details: String,
    @field:Json(name = "site_id") var siteId: String,
    @field:Json(name = "site_name_long") var nameLong: String
)

data class LaunchpadLocationModel(
    @field:Json(name = "name") var name: String,
    @field:Json(name = "region") var region: String,
    @field:Json(name = "latitude") var lat: Float,
    @field:Json(name = "longitude") var lng: Float
)