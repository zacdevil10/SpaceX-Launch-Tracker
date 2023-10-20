package uk.co.zac_h.spacex.feature.assets.vehicles.spacecraft

import uk.co.zac_h.spacex.feature.assets.vehicles.VehicleItem
import uk.co.zac_h.spacex.network.dto.spacex.SpacecraftResponse

data class SpacecraftItem(
    override val id: Int,
    val name: String?,
    override val title: String?,
    val serialNumber: String?,
    val status: String?,
    override val description: String?,
    override val longDescription: String?,
    override val imageUrl: String? = null,
    val type: String?
) : VehicleItem {

    constructor(response: SpacecraftResponse) : this(
        id = response.id,
        name = response.name,
        title = response.serialNumber?.let {
            if (response.name?.contains(it) == false) {
                "${response.name} - ${response.serialNumber}"
            } else {
                response.name
            }
        } ?: response.name,
        serialNumber = response.serialNumber,
        status = response.status?.name,
        description = response.description,
        longDescription = response.description,
        type = response.spacecraftConfig?.name
    )
}
