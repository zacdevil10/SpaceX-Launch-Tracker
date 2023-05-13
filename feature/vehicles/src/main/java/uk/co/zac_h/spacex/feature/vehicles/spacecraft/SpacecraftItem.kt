package uk.co.zac_h.spacex.feature.vehicles.spacecraft

import uk.co.zac_h.spacex.feature.vehicles.VehicleItem
import uk.co.zac_h.spacex.network.dto.spacex.SpacecraftResponse

data class SpacecraftItem(
    override val id: Int,
    val name: String?,
    override val title: String?,
    val serialNumber: String?,
    val status: String?,
    override val description: String?,
    override val imageUrl: String?
) : VehicleItem {

    constructor(response: SpacecraftResponse) : this(
        id = response.id,
        name = response.name,
        title = response.serialNumber,
        serialNumber = response.serialNumber,
        status = response.status?.name,
        description = response.description,
        imageUrl = response.spacecraftConfig?.imageUrl
    )
}
