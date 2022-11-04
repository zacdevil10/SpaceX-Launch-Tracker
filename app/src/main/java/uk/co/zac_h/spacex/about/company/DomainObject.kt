package uk.co.zac_h.spacex.about.company

import uk.co.zac_h.spacex.core.common.utils.orUnknown
import uk.co.zac_h.spacex.network.dto.spacex.CompanyAddress
import uk.co.zac_h.spacex.network.dto.spacex.CompanyResponse
import java.text.DecimalFormat

data class Company(
    val name: String,
    val founder: String,
    val founded: String,
    val employees: String,
    val vehicles: String,
    val launchSites: String,
    val testSites: String,
    val ceo: String,
    val cto: String,
    val coo: String,
    val ctoPropulsion: String,
    val valuation: String,
    val headquarters: CompanyAddress?,
    val website: String?,
    val flickr: String?,
    val twitter: String?,
    val summary: String,
    val id: String
) {

    constructor(
        response: CompanyResponse
    ) : this(
        name = response.name.orUnknown(),
        founder = response.founder.orUnknown(),
        founded = response.founded.toString().orUnknown(),
        employees = response.employees?.toString().orUnknown(),
        vehicles = response.vehicles?.toString().orUnknown(),
        launchSites = response.launchSites?.toString().orUnknown(),
        testSites = response.testSites?.toString().orUnknown(),
        ceo = response.ceo.orUnknown(),
        cto = response.cto.orUnknown(),
        coo = response.coo.orUnknown(),
        ctoPropulsion = response.ctoPropulsion.orUnknown(),
        valuation = DecimalFormat("$#,###.00").format(response.valuation),
        headquarters = response.headquarters,
        website = response.links?.website,
        flickr = response.links?.flickr,
        twitter = response.links?.twitter,
        summary = response.summary.orEmpty(),
        id = response.id
    )
}
