package uk.co.zac_h.spacex.feature.assets.vehicles.launcher

import uk.co.zac_h.spacex.core.common.utils.TextResource
import uk.co.zac_h.spacex.core.model.Launcher
import uk.co.zac_h.spacex.core.common.utils.orUnknown
import uk.co.zac_h.spacex.feature.assets.R
import uk.co.zac_h.spacex.feature.assets.vehicles.SpecsItem
import uk.co.zac_h.spacex.feature.assets.vehicles.VehicleItem
import uk.co.zac_h.spacex.feature.assets.vehicles.formatLocal

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

    constructor(launcher: Launcher) : this(
        id = launcher.id.toString(),
        title = launcher.serial.orUnknown(),
        serial = launcher.serial.orUnknown(),
        status = launcher.status,
        description = launcher.details?.ifEmpty { null } ?: launcher.configuration,
        longDescription = launcher.details?.ifEmpty { null } ?: launcher.configuration,
        imageUrl = launcher.imageUrl,
        flights = launcher.flights,
        lastLaunchDate = launcher.lastLaunch?.formatLocal("dd MMM yyyy"),
        firstLaunchDate = launcher.firstLaunch?.formatLocal("dd MMM yyyy")
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
}
