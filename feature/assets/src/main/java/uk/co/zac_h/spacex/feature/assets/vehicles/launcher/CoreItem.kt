package uk.co.zac_h.spacex.feature.assets.vehicles.launcher

import uk.co.zac_h.spacex.core.common.utils.TextResource
import uk.co.zac_h.spacex.core.common.utils.orUnknown
import uk.co.zac_h.spacex.feature.assets.R
import uk.co.zac_h.spacex.feature.assets.vehicles.SpecsItem
import uk.co.zac_h.spacex.feature.assets.vehicles.VehicleItem
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

    override val specs: List<SpecsItem>
        get() = listOfNotNull(
            status?.let {
                SpecsItem(
                    TextResource.string(R.string.core_details_status_label),
                    TextResource.Companion.string(it.replaceFirstChar(Char::uppercase))
                )
            },
            flights?.let {
                SpecsItem(
                    TextResource.string(R.string.core_details_flights_label),
                    TextResource.Companion.string(it.toString())
                )
            },
            lastLaunchDate?.let {
                SpecsItem(
                    TextResource.string(R.string.core_details_last_launch_label),
                    TextResource.Companion.string(it)
                )
            },
            firstLaunchDate?.let {
                SpecsItem(
                    TextResource.string(R.string.core_details_first_launch_label),
                    TextResource.Companion.string(it)
                )
            }
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
