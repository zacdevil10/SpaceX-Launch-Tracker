package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import uk.co.zac_h.spacex.utils.*
import java.text.DecimalFormat

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

@Parcelize
data class CompanyAddress(
    @field:Json(name = SPACEX_FIELD_COMPANY_ADDRESS) val address: String,
    @field:Json(name = SPACEX_FIELD_COMPANY_CITY) val city: String,
    @field:Json(name = SPACEX_FIELD_COMPANY_STATE) val state: String
) : Parcelable

data class CompanyLinksModel(
    @field:Json(name = SPACEX_FIELD_COMPANY_WEBSITE) val website: String,
    @field:Json(name = SPACEX_FIELD_COMPANY_FLICKR) val flickr: String,
    @field:Json(name = SPACEX_FIELD_COMPANY_TWITTER) val twitter: String,
    @field:Json(name = SPACEX_FIELD_COMPANY_ELON_TWITTER) val elonTwitter: String
)

@Parcelize
data class Company(
    val name: String?,
    val founder: String?,
    val founded: Int?,
    val employees: Int?,
    val vehicles: Int?,
    val launchSites: Int?,
    val testSites: Int?,
    val ceo: String?,
    val cto: String?,
    val coo: String?,
    val ctoPropulsion: String?,
    val valuation: String?,
    val headquarters: CompanyAddress?,
    val website: String?,
    val flickr: String?,
    val twitter: String?,
    val summary: String?,
    val id: String
) : Parcelable {

    constructor(
        response: CompanyResponse
    ) : this(
        name = response.name,
        founder = response.founder,
        founded = response.founded,
        employees = response.employees,
        vehicles = response.vehicles,
        launchSites = response.launchSites,
        testSites = response.testSites,
        ceo = response.ceo,
        cto = response.cto,
        coo = response.coo,
        ctoPropulsion = response.ctoPropulsion,
        valuation = DecimalFormat("$#,###.00").format(response.valuation),
        headquarters = response.headquarters,
        website = response.links?.website,
        flickr = response.links?.flickr,
        twitter = response.links?.twitter,
        summary = response.summary,
        id = response.id
    )
}