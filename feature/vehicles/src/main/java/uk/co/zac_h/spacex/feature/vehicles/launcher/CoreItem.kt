package uk.co.zac_h.spacex.feature.vehicles.launcher

import uk.co.zac_h.spacex.core.common.utils.orUnknown
import uk.co.zac_h.spacex.feature.vehicles.VehicleItem
import uk.co.zac_h.spacex.network.dto.spacex.LauncherResponse
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

data class CoreItem(
    override val id: String,
    override val title: String?,
    val serial: String,
    val status: String?,
    override val description: String?,
    override val longDescription: String?,
    override val imageUrl: String?,
    val flights: Int?,
    val lastLaunchDate: String?,
    val firstLaunchDate: String?
) : VehicleItem {

    constructor(
        response: LauncherResponse
    ) : this(
        id = response.id.toString(),
        title = response.serialNumber.orUnknown(),
        serial = response.serialNumber.orUnknown(),
        status = response.status,
        description = if (response.details?.isEmpty() == false) {
            response.details
        } else {
            response.launcherConfig?.fullName
        },
        longDescription = if (response.details?.isEmpty() == false) {
            response.details
        } else {
            response.launcherConfig?.fullName
        },
        imageUrl = response.imageUrl,
        flights = response.flights,
        lastLaunchDate = response.lastLaunchDate?.formatDate(),
        firstLaunchDate = response.firstLaunchDate?.formatDate()
    )

    companion object {

        fun String.formatDate(): String? = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            Locale.ENGLISH
        ).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.parse(this)?.let {
            SimpleDateFormat(
                "dd MMM yyyy",
                Locale.ENGLISH
            ).apply {
                timeZone = TimeZone.getDefault()
            }.format(it)
        }
    }
}
