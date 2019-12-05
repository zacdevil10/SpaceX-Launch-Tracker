package uk.co.zac_h.spacex.model.spacex

import com.squareup.moshi.Json

data class CompanyModel(
    @field:Json(name = "name") val name: String,
    @field:Json(name = "founder") val founder: String,
    @field:Json(name = "founded") val founded: Int,
    @field:Json(name = "employees") val employees: Int,
    @field:Json(name = "vehicles") val vehicles: Int,
    @field:Json(name = "launch_sites") val launchSites: Int,
    @field:Json(name = "test_sites") val testSites: Int,
    @field:Json(name = "ceo") val ceo: String,
    @field:Json(name = "cto") val cto: String,
    @field:Json(name = "coo") val coo: String,
    @field:Json(name = "cto_propulsion") val ctoPropulsion: String,
    @field:Json(name = "valuation") val valuation: Long,
    @field:Json(name = "headquarters") val headquarters: CompanyAddress,
    @field:Json(name = "summary") val summary: String
)

data class CompanyAddress(
    @field:Json(name = "address") val address: String,
    @field:Json(name = "city") val city: String,
    @field:Json(name = "state") val state: String
)