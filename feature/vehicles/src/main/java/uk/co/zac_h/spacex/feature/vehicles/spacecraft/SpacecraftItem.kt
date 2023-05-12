package uk.co.zac_h.spacex.feature.vehicles.spacecraft

import uk.co.zac_h.spacex.network.dto.spacex.SpacecraftResponse

data class SpacecraftItem(
    val id: Int,
    val name: String?,
    val serialNumber: String?,
    val status: String?,
    val description: String?,
    val imageUrl: String?
) {

    constructor(response: SpacecraftResponse) : this(
        id = response.id,
        name = response.name,
        serialNumber = response.serialNumber,
        status = response.status?.name,
        description = response.description,
        imageUrl = response.spacecraftConfig?.imageUrl
    )
}
