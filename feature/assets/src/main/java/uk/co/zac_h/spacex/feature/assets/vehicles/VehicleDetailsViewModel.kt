package uk.co.zac_h.spacex.feature.assets.vehicles

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uk.co.zac_h.spacex.core.common.utils.TextResource.Companion.string
import uk.co.zac_h.spacex.core.common.utils.metricFormat
import uk.co.zac_h.spacex.feature.assets.R
import uk.co.zac_h.spacex.feature.assets.vehicles.adapters.HeaderItem
import uk.co.zac_h.spacex.feature.assets.vehicles.adapters.SpecsItem
import uk.co.zac_h.spacex.feature.assets.vehicles.dragon.SecondStageItem
import uk.co.zac_h.spacex.feature.assets.vehicles.launcher.CoreItem
import uk.co.zac_h.spacex.feature.assets.vehicles.rockets.RocketItem
import uk.co.zac_h.spacex.feature.assets.vehicles.rockets.RocketRepository
import uk.co.zac_h.spacex.feature.assets.vehicles.spacecraft.SpacecraftItem
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
                string(R.string.rocket_details_success_rate_label),
                string(R.string.percentage, it.metricFormat())
            )
        },
        item.maidenFlight?.let {
            SpecsItem(
                string(R.string.rocket_details_first_flight_label),
                string(it)
            )
        },
        item.stages?.let {
            SpecsItem(
                string(R.string.rocket_details_stages_label),
                string(it.toString())
            )
        },
        item.length?.let {
            SpecsItem(
                string(R.string.rocket_details_height_label),
                string(R.string.measurement_meters, it.metricFormat())
            )
        },
        item.diameter?.let {
            SpecsItem(
                string(R.string.rocket_details_diameter_label),
                string(R.string.measurement_meters, it.metricFormat())
            )
        },
        item.launchMass?.let {
            SpecsItem(
                string(R.string.rocket_details_mass_label),
                string(R.string.mass_tonne, it)
            )
        },
        item.toThrust?.let {
            SpecsItem(
                string(R.string.rocket_details_thrust_at_liftoff_label),
                string(R.string.thrust_kn, it.metricFormat())
            )
        },
        item.leoCapacity?.let {
            SpecsItem(
                string(R.string.rocket_details_mass_to_leo_label),
                string(R.string.mass_kg, it.metricFormat())
            )
        },
        item.gtoCapacity?.let {
            SpecsItem(
                string(R.string.rocket_details_mass_to_gto_label),
                string(R.string.mass_kg, it.metricFormat())
            )
        }
    )

    private fun convertSecondStageItem(item: SecondStageItem): List<SpecsItem> = listOfNotNull(
        SpecsItem(
            string(R.string.second_stage_details_active_label),
            string(
                if (item.inUse == true) {
                    R.string.second_stage_details_active
                } else {
                    R.string.second_stage_details_inactive
                }
            )
        ),
        item.crewCapacity?.let {
            SpecsItem(
                string(R.string.second_stage_details_crew_capacity_label),
                string(it.toString())
            )
        },
        item.maidenFlight?.let {
            SpecsItem(
                string(R.string.second_stage_details_first_flight_label),
                string(it)
            )
        },
        item.height?.let {
            SpecsItem(
                string(R.string.second_stage_details_height_label),
                string(R.string.measurement_meters, it.metricFormat())
            )
        },
        item.diameter?.let {
            SpecsItem(
                string(R.string.second_stage_details_diameter_label),
                string(R.string.measurement_meters, it.metricFormat())
            )
        },
        item.payloadCapacity?.let {
            SpecsItem(
                string(R.string.second_stage_details_payload_capacity_label),
                string(R.string.mass_kg, it.metricFormat())
            )
        }
    )

    private fun convertCoreItem(item: CoreItem): List<SpecsItem> = listOfNotNull(
        item.status?.let {
            SpecsItem(
                string(R.string.core_details_status_label),
                string(it.replaceFirstChar(Char::uppercase))
            )
        },
        item.flights?.let {
            SpecsItem(
                string(R.string.core_details_flights_label),
                string(it.toString())
            )
        },
        item.lastLaunchDate?.let {
            SpecsItem(
                string(R.string.core_details_last_launch_label),
                string(it)
            )
        },
        item.firstLaunchDate?.let {
            SpecsItem(
                string(R.string.core_details_first_launch_label),
                string(it)
            )
        }
    )

    private fun convertSpacecraftItem(item: SpacecraftItem): List<SpecsItem> = listOfNotNull(
        item.status?.let {
            SpecsItem(
                string(R.string.capsule_details_status_label),
                string(it.replaceFirstChar(Char::uppercase))
            )
        },
        item.type?.let {
            SpecsItem(
                string(R.string.capsule_details_type_label),
                string(it)
            )
        }
    )
}