package uk.co.zac_h.spacex.core.model

/** SpaceX as a Launch Library agency. */
data class Company(
    val id: Int,
    val name: String?,
    val description: String?,
    val administrator: String?,
    val foundingYear: Int?,
    val totalLaunchCount: Int?,
    val websiteUrl: String?,
    val wikiUrl: String?,
)
