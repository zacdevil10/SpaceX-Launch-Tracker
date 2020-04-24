package uk.co.zac_h.spacex.vehicles.dragon.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.FragmentDragonDetailsBinding
import uk.co.zac_h.spacex.model.spacex.DragonModel
import uk.co.zac_h.spacex.utils.metricFormat
import uk.co.zac_h.spacex.utils.setImageAndTint
import uk.co.zac_h.spacex.vehicles.adapters.DragonThrusterAdapter

class DragonDetailsFragment : Fragment() {

    private var _binding: FragmentDragonDetailsBinding? = null
    private val binding get() = _binding!!

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
    ): View? {
        _binding = FragmentDragonDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dragon?.let {
            binding.dragonDetailsCoordinator.transitionName = it.id

            binding.dragonDetailsAppbarImage.setImageResource(
                when (it.id) {
                    "dragon1" -> R.drawable.dragon1
                    "dragon2" -> R.drawable.dragon2
                    else -> R.drawable.dragon2 //TODO: Add error image.
                }
            )

            binding.dragonDetailsNameText.text = it.name
            binding.dragonDetailsText.text = it.description

            when (it.active) {
                true -> binding.dragonDetailsStatusImage.setImageAndTint(
                    R.drawable.ic_check_circle_black_24dp,
                    R.color.success
                )
                false -> binding.dragonDetailsStatusImage.setImageAndTint(
                    R.drawable.ic_remove_circle_black_24dp,
                    R.color.failed
                )
            }

            binding.dragonDetailsCrewCapacityText.text = it.crewCapacity.toString()
            binding.dragonDetailsFirstFlightText.text = it.firstFlight
            binding.dragonDetailsDryMassText.text = context?.getString(
                R.string.mass_formatted,
                it.dryMassKg.metricFormat(),
                it.dryMassLb.metricFormat()
            )
            binding.dragonDetailsHeightText.text = context?.getString(
                R.string.measurements,
                it.heightWithTrunk.meters.metricFormat(),
                it.heightWithTrunk.feet.metricFormat()
            )
            binding.dragonDetailsDiameterText.text = context?.getString(
                R.string.measurements,
                it.diameter.meters.metricFormat(),
                it.diameter.feet.metricFormat()
            )

            binding.dragonDetailsShieldMaterialText.text = it.heatShield.material
            binding.dragonDetailsShieldSizeText.text =
                it.heatShield.size.toString() //TODO: Format with units
            binding.dragonDetailsShieldTempText.text =
                it.heatShield.temp.toString() //TODO: Format with units

            binding.dragonDetailsThrusterRecycler.apply {
                layoutManager = LinearLayoutManager(this@DragonDetailsFragment.context)
                setHasFixedSize(true)
                adapter = DragonThrusterAdapter(this@DragonDetailsFragment.context, it.thrusters)
            }

            binding.dragonDetailsLaunchMassText.text = context?.getString(
                R.string.mass_formatted,
                it.launchPayloadMass.kg.metricFormat(),
                it.launchPayloadMass.lb.metricFormat()
            )

            binding.dragonDetailsReturnMassText.text = context?.getString(
                R.string.mass_formatted,
                it.returnPayloadMass.kg.metricFormat(),
                it.returnPayloadMass.lb.metricFormat()
            )

            binding.dragonDetailsLaunchVolText.text = context?.getString(
                R.string.volume_formatted,
                it.launchPayloadVolume.cubicMeters.metricFormat(),
                it.launchPayloadVolume.cubicFeet.metricFormat()
            )

            binding.dragonDetailsReturnVolText.text = context?.getString(
                R.string.volume_formatted,
                it.returnPayloadVol.cubicMeters.metricFormat(),
                it.returnPayloadVol.cubicFeet.metricFormat()
            )

            binding.dragonDetailsPressurizedVolText.text = context?.getString(
                R.string.volume_formatted,
                it.pressurizedCapsule.payloadVolume.cubicMeters.metricFormat(),
                it.pressurizedCapsule.payloadVolume.cubicFeet.metricFormat()
            )

            binding.dragonDetailsTrunkVolText.text = context?.getString(
                R.string.volume_formatted,
                it.trunk.trunkVolume.cubicMeters.metricFormat(),
                it.trunk.trunkVolume.cubicFeet.metricFormat()
            )

            binding.dragonDetailsSolarArrayText.text = it.trunk.cargo.solarArray.toString()

            when (it.trunk.cargo.unpressurizedCargo) {
                true -> binding.dragonDetailsUnpressurizedCargoImage.setImageAndTint(
                    R.drawable.ic_check_circle_black_24dp,
                    R.color.success
                )
                false -> binding.dragonDetailsUnpressurizedCargoImage.setImageAndTint(
                    R.drawable.ic_remove_circle_black_24dp,
                    R.color.failed
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
