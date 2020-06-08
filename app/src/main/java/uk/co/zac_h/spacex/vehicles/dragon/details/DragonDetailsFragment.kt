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

        sharedElementEnterTransition = MaterialContainerTransform()

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
                    "5e9d058759b1ff74a7ad5f8f" -> R.drawable.dragon1
                    "5e9d058859b1ffd8e2ad5f90" -> R.drawable.dragon2
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
                it.dryMassKg?.metricFormat(),
                it.dryMassLb?.metricFormat()
            )
            it.heightWithTrunk?.let { heightWithTrunk ->
                binding.dragonDetailsHeightText.text = context?.getString(
                    R.string.measurements,
                    heightWithTrunk.meters?.metricFormat(),
                    heightWithTrunk.feet?.metricFormat()
                )
            }
            it.diameter?.let { diameter ->
                binding.dragonDetailsDiameterText.text = context?.getString(
                    R.string.measurements,
                    diameter.meters?.metricFormat(),
                    diameter.feet?.metricFormat()
                )
            }

            binding.dragonDetailsShieldMaterialText.text = it.heatShield?.material
            binding.dragonDetailsShieldSizeText.text =
                it.heatShield?.size.toString() //TODO: Format with units
            binding.dragonDetailsShieldTempText.text =
                it.heatShield?.temp.toString() //TODO: Format with units

            binding.dragonDetailsThrusterRecycler.apply {
                layoutManager = LinearLayoutManager(this@DragonDetailsFragment.context)
                setHasFixedSize(true)
                adapter = DragonThrusterAdapter(this@DragonDetailsFragment.context, it.thrusters)
            }

            it.launchPayloadMass?.let { launchPayloadMass ->
                binding.dragonDetailsLaunchMassText.text = context?.getString(
                    R.string.mass_formatted,
                    launchPayloadMass.kg?.metricFormat(),
                    launchPayloadMass.lb?.metricFormat()
                )
            }

            it.returnPayloadMass?.let { returnPayloadMass ->
                binding.dragonDetailsReturnMassText.text = context?.getString(
                    R.string.mass_formatted,
                    returnPayloadMass.kg?.metricFormat(),
                    returnPayloadMass.lb?.metricFormat()
                )
            }

            it.launchPayloadVolume?.let { launchPayloadVolume ->
                binding.dragonDetailsLaunchVolText.text = context?.getString(
                    R.string.volume_formatted,
                    launchPayloadVolume.cubicMeters?.metricFormat(),
                    launchPayloadVolume.cubicFeet?.metricFormat()
                )
            }

            it.returnPayloadVol?.let { returnPayloadVol ->
                binding.dragonDetailsReturnVolText.text = context?.getString(
                    R.string.volume_formatted,
                    returnPayloadVol.cubicMeters?.metricFormat(),
                    returnPayloadVol.cubicFeet?.metricFormat()
                )
            }

            it.pressurizedCapsule?.payloadVolume?.let { payloadVolume ->
                binding.dragonDetailsPressurizedVolText.text = context?.getString(
                    R.string.volume_formatted,
                    payloadVolume.cubicMeters?.metricFormat(),
                    payloadVolume.cubicFeet?.metricFormat()
                )
            }

            it.trunk?.trunkVolume?.let { trunkVolume ->
                binding.dragonDetailsTrunkVolText.text = context?.getString(
                    R.string.volume_formatted,
                    trunkVolume.cubicMeters?.metricFormat(),
                    trunkVolume.cubicFeet?.metricFormat()
                )
            }

            binding.dragonDetailsSolarArrayText.text = (it.trunk?.cargo?.solarArray ?: 0).toString()

            when (it.trunk?.cargo?.unpressurizedCargo) {
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
