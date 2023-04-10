package uk.co.zac_h.spacex.feature.vehicles.launcher

import uk.co.zac_h.spacex.core.common.recyclerview.RecyclerViewItem
import uk.co.zac_h.spacex.core.common.utils.orUnknown
import uk.co.zac_h.spacex.network.dto.spacex.LauncherResponse

data class LauncherItem(
    val id: String,
    val serial: String,
    val status: String?,
    val details: String?,
    val imageUrl: String?,
    val flights: Int?,
    val lastLaunchDate: String?,
    val firstLaunchDate: String?
) : RecyclerViewItem {

    constructor(
        response: LauncherResponse
    ) : this(
        id = response.id.toString(),
        serial = response.serialNumber.orUnknown(),
        status = response.status,
        details = if (response.details?.isEmpty() == false) {
            response.details
        } else {
            response.launcherConfig?.fullName
        },
        imageUrl = response.imageUrl,
        flights = response.flights,
        lastLaunchDate = response.lastLaunchDate,
        firstLaunchDate = response.firstLaunchDate
    )
}
