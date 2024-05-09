package uk.co.zac_h.spacex.feature.settings.company

import uk.co.zac_h.spacex.network.dto.spacex.AgencyResponse

data class Company(
    val id: Int,
    val name: String?,
    val description: String?,
    val administrator: String?,
    val foundingYear: String?,
    val totalLaunchCount: Int?,
    val website: String?,
    val wiki: String?
) {

    constructor(
        response: AgencyResponse
    ) : this(
        id = response.id,
        name = response.name,
        description = response.description,
        administrator = response.administrator,
        foundingYear = response.foundingYear,
        totalLaunchCount = response.totalLaunchCount,
        website = response.infoUrl,
        wiki = response.wikiUrl
    )
}
