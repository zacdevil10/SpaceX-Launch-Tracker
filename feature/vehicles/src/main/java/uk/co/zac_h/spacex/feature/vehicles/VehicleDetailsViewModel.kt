package uk.co.zac_h.spacex.feature.vehicles

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uk.co.zac_h.spacex.core.common.utils.TextResource.Companion.string
import uk.co.zac_h.spacex.core.common.utils.metricFormat
import uk.co.zac_h.spacex.core.common.utils.orUnknown
import uk.co.zac_h.spacex.feature.vehicles.adapters.HeaderItem
import uk.co.zac_h.spacex.feature.vehicles.adapters.SpecsItem
import uk.co.zac_h.spacex.feature.vehicles.dragon.SecondStageItem
import uk.co.zac_h.spacex.feature.vehicles.launcher.CoreItem
import uk.co.zac_h.spacex.feature.vehicles.rockets.RocketItem
import uk.co.zac_h.spacex.feature.vehicles.rockets.RocketRepository
import uk.co.zac_h.spacex.feature.vehicles.spacecraft.SpacecraftItem
import javax.inject.Inject

@HiltViewModel
class VehicleDetailsViewModel @Inject constructor(
    private val repository: RocketRepository
) : ViewModel() {

    var vehicle: VehicleItem? = null
        set(value) {
            field = value
            repository.launcherConfigId = (value as? RocketItem)?.id
        }

    val header: HeaderItem
        get() = HeaderItem(
            image = vehicle?.imageUrl,
            description = vehicle?.longDescription
        )

    fun generateSpecsList(item: VehicleItem?) = when (item) {
        is RocketItem -> convertRocketItem(item)
        is SecondStageItem -> convertSecondStageItem(item)
        is CoreItem -> convertCoreItem(item)
        is SpacecraftItem -> convertSpacecraftItem(item)
        else -> emptyList()
    }


    private fun convertRocketItem(item: RocketItem): List<SpecsItem> = listOfNotNull(
        item.successRate?.let {
            SpecsItem(
                string("Launch Success Rate"),
                string(R.string.percentage, it.metricFormat())
            )
        },
        item.maidenFlight?.let {
            SpecsItem(
                string(R.string.first_flight_label),
                string(it)
            )
        },
        item.stages?.let {
            SpecsItem(
                string(R.string.stages_label),
                string(it.toString())
            )
        },
        item.length?.let {
            SpecsItem(
                string(R.string.height_label),
                string(R.string.measurements, it.metricFormat())
            )
        },
        item.diameter?.let {
            SpecsItem(
                string(R.string.diameter_label),
                string(R.string.measurements, it.metricFormat())
            )
        },
        item.launchMass?.let {
            SpecsItem(
                string("Mass"),
                string(R.string.mass_formatted, it)
            )
        },
        item.toThrust?.let {
            SpecsItem(
                string("Thrust at Liftoff"),
                string(R.string.thrust, it.metricFormat())
            )
        },
        item.leoCapacity?.let {
            SpecsItem(
                string("Mass to LEO"),
                string(R.string.mass, it.metricFormat())
            )
        },
        item.gtoCapacity?.let {
            SpecsItem(
                string("Mass to GTO"),
                string(R.string.mass, it.metricFormat())
            )
        }
    )

    private fun convertSecondStageItem(item: SecondStageItem): List<SpecsItem> = listOfNotNull(
        SpecsItem(
            string(R.string.active_label),
            string(item.inUse.toString().orUnknown())
        ),
        item.crewCapacity?.let {
            SpecsItem(
                string(R.string.crew_capacity_label),
                string(it.toString())
            )
        },
        item.maidenFlight?.let {
            SpecsItem(
                string(R.string.first_flight_label),
                string(it)
            )
        },
        item.height?.let {
            SpecsItem(
                string(R.string.height_label),
                string(R.string.measurements, it.metricFormat())
            )
        },
        item.diameter?.let {
            SpecsItem(
                string(R.string.diameter_label),
                string(R.string.measurements, it.metricFormat())
            )
        },
        item.payloadCapacity?.let {
            SpecsItem(
                string("Payload Capacity"),
                string(R.string.mass, it.metricFormat())
            )
        }
    )

    private fun convertCoreItem(item: CoreItem): List<SpecsItem> = listOfNotNull()

    private fun convertSpacecraftItem(item: SpacecraftItem): List<SpecsItem> = listOfNotNull()
}