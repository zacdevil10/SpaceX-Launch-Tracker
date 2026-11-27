package uk.co.zac_h.spacex.feature.assets.vehicles.spacecraft

import uk.co.zac_h.spacex.core.common.utils.TextResource
import uk.co.zac_h.spacex.core.model.Spacecraft
import uk.co.zac_h.spacex.feature.assets.R
import uk.co.zac_h.spacex.feature.assets.vehicles.SpecsItem
import uk.co.zac_h.spacex.feature.assets.vehicles.VehicleItem

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

    // The list has never shown spacecraft images, so imageUrl keeps its null default.
    constructor(spacecraft: Spacecraft) : this(
        id = spacecraft.id,
        name = spacecraft.name,
        title = spacecraft.serial?.let {
            if (spacecraft.name?.contains(it) == false) {
                "${spacecraft.name} - ${spacecraft.serial}"
            } else {
                spacecraft.name
            }
        } ?: spacecraft.name,
        serialNumber = spacecraft.serial,
        status = spacecraft.status,
        description = spacecraft.description,
        longDescription = spacecraft.description,
        type = spacecraft.configuration
    )

    override val specs: List<SpecsItem>
        get() = listOfNotNull(
            status?.let {
                SpecsItem(
                    TextResource.string(R.string.capsule_details_status_label),
                    TextResource.Companion.string(it.replaceFirstChar(Char::uppercase))
                )
            },
            type?.let {
                SpecsItem(
                    TextResource.string(R.string.capsule_details_type_label),
                    TextResource.Companion.string(it)
                )
            }
        )
}
