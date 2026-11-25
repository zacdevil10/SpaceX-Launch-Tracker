package uk.co.zac_h.spacex.feature.settings.company

import uk.co.zac_h.spacex.network.launchlibrary.model.AgencyEndpointDetailed

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
        response: AgencyEndpointDetailed
    ) : this(
        id = response.id ?: 0,
        name = response.name,
        description = response.description,
        administrator = response.administrator,
        foundingYear = response.foundingYear?.toString(),
        totalLaunchCount = response.totalLaunchCount,
        website = response.infoUrl,
        wiki = response.wikiUrl
    )
}
