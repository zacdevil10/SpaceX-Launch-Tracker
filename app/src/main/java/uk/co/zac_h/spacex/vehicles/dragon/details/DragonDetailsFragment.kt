package uk.co.zac_h.spacex.vehicles.dragon.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.android.synthetic.main.fragment_dragon_details.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.spacex.DragonModel
import uk.co.zac_h.spacex.utils.metricFormat
import uk.co.zac_h.spacex.utils.setImageAndTint
import uk.co.zac_h.spacex.vehicles.adapters.DragonThrusterAdapter

class DragonDetailsFragment : Fragment() {

    private var dragon: DragonModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = context?.let { MaterialContainerTransform(it) }

        dragon = arguments?.getParcelable("dragon") as DragonModel?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_dragon_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dragon?.let {
            dragon_details_coordinator.transitionName = it.id

            dragon_details_appbar_image.setImageResource(
                when (it.id) {
                    "dragon1" -> R.drawable.dragon1
                    "dragon2" -> R.drawable.dragon2
                    else -> R.drawable.dragon2 //TODO: Add error image.
                }
            )

            dragon_details_name_text.text = it.name
            dragon_details_text.text = it.description

            when (it.active) {
                true -> dragon_details_status_image.setImageAndTint(
                    R.drawable.ic_check_circle_black_24dp,
                    R.color.success
                )
                false -> dragon_details_status_image.setImageAndTint(
                    R.drawable.ic_remove_circle_black_24dp,
                    R.color.failed
                )
            }

            dragon_details_crew_capacity_text.text = it.crewCapacity.toString()
            dragon_details_first_flight_text.text = it.firstFlight
            dragon_details_dry_mass_text.text = context?.getString(
                R.string.mass_formatted,
                it.dryMassKg.metricFormat(),
                it.dryMassLb.metricFormat()
            )
            dragon_details_height_text.text = context?.getString(
                R.string.measurements,
                it.heightWithTrunk.meters.metricFormat(),
                it.heightWithTrunk.feet.metricFormat()
            )
            dragon_details_diameter_text.text = context?.getString(
                R.string.measurements,
                it.diameter.meters.metricFormat(),
                it.diameter.feet.metricFormat()
            )

            dragon_details_shield_material_text.text = it.heatShield.material
            dragon_details_shield_size_text.text =
                it.heatShield.size.toString() //TODO: Format with units
            dragon_details_shield_temp_text.text =
                it.heatShield.temp.toString() //TODO: Format with units

            dragon_details_thruster_recycler.apply {
                layoutManager = LinearLayoutManager(this@DragonDetailsFragment.context)
                setHasFixedSize(true)
                adapter = DragonThrusterAdapter(this@DragonDetailsFragment.context, it.thrusters)
            }

            dragon_details_launch_mass_text.text = context?.getString(
                R.string.mass_formatted,
                it.launchPayloadMass.kg.metricFormat(),
                it.launchPayloadMass.lb.metricFormat()
            )

            dragon_details_return_mass_text.text = context?.getString(
                R.string.mass_formatted,
                it.returnPayloadMass.kg.metricFormat(),
                it.returnPayloadMass.lb.metricFormat()
            )

            dragon_details_launch_vol_text.text = context?.getString(
                R.string.volume_formatted,
                it.launchPayloadVolume.cubicMeters.metricFormat(),
                it.launchPayloadVolume.cubicFeet.metricFormat()
            )

            dragon_details_return_vol_text.text = context?.getString(
                R.string.volume_formatted,
                it.returnPayloadVol.cubicMeters.metricFormat(),
                it.returnPayloadVol.cubicFeet.metricFormat()
            )

            dragon_details_pressurized_vol_text.text = context?.getString(
                R.string.volume_formatted,
                it.pressurizedCapsule.payloadVolume.cubicMeters.metricFormat(),
                it.pressurizedCapsule.payloadVolume.cubicFeet.metricFormat()
            )

            dragon_details_trunk_vol_text.text = context?.getString(
                R.string.volume_formatted,
                it.trunk.trunkVolume.cubicMeters.metricFormat(),
                it.trunk.trunkVolume.cubicFeet.metricFormat()
            )

            dragon_details_solar_array_text.text = it.trunk.cargo.solarArray.toString()

            when (it.trunk.cargo.unpressurizedCargo) {
                true -> dragon_details_unpressurized_cargo_image.setImageAndTint(
                    R.drawable.ic_check_circle_black_24dp,
                    R.color.success
                )
                false -> dragon_details_unpressurized_cargo_image.setImageAndTint(
                    R.drawable.ic_remove_circle_black_24dp,
                    R.color.failed
                )
            }
        }
    }

}
