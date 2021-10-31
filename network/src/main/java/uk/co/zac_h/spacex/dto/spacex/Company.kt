package uk.co.zac_h.spacex.dto.spacex

import com.squareup.moshi.Json
import uk.co.zac_h.spacex.*

data class CompanyResponse(
    @field:Json(name = SPACEX_FIELD_COMPANY_NAME) val name: String?,
    @field:Json(name = SPACEX_FIELD_COMPANY_FOUNDER) val founder: String?,
    @field:Json(name = SPACEX_FIELD_COMPANY_FOUNDED) val founded: Int?,
    @field:Json(name = SPACEX_FIELD_COMPANY_EMPLOYEES) val employees: Int?,
    @field:Json(name = SPACEX_FIELD_COMPANY_VEHICLES) val vehicles: Int?,
    @field:Json(name = SPACEX_FIELD_COMPANY_LAUNCH_SITES) val launchSites: Int?,
    @field:Json(name = SPACEX_FIELD_COMPANY_TEST_SITES) val testSites: Int?,
    @field:Json(name = SPACEX_FIELD_COMPANY_CEO) val ceo: String?,
    @field:Json(name = SPACEX_FIELD_COMPANY_CTO) val cto: String?,
    @field:Json(name = SPACEX_FIELD_COMPANY_COO) val coo: String?,
    @field:Json(name = SPACEX_FIELD_COMPANY_CTO_PROPULSION) val ctoPropulsion: String?,
    @field:Json(name = SPACEX_FIELD_COMPANY_VALUATION) val valuation: Long?,
    @field:Json(name = SPACEX_FIELD_COMPANY_HEADQUATERS) val headquarters: CompanyAddress?,
    @field:Json(name = SPACEX_FIELD_COMPANY_LINKS) val links: CompanyLinksModel?,
    @field:Json(name = SPACEX_FIELD_COMPANY_SUMMARY) val summary: String?,
    @field:Json(name = SPACEX_FIELD_ID) val id: String
)

data class CompanyAddress(
    @field:Json(name = SPACEX_FIELD_COMPANY_ADDRESS) val address: String,
    @field:Json(name = SPACEX_FIELD_COMPANY_CITY) val city: String,
    @field:Json(name = SPACEX_FIELD_COMPANY_STATE) val state: String
)

data class CompanyLinksModel(
    @field:Json(name = SPACEX_FIELD_COMPANY_WEBSITE) val website: String,
    @field:Json(name = SPACEX_FIELD_COMPANY_FLICKR) val flickr: String,
    @field:Json(name = SPACEX_FIELD_COMPANY_TWITTER) val twitter: String,
    @field:Json(name = SPACEX_FIELD_COMPANY_ELON_TWITTER) val elonTwitter: String
)