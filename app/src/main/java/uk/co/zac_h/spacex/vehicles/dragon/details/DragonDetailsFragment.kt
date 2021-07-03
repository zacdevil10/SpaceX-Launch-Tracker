package uk.co.zac_h.spacex.vehicles.dragon.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.CollapsingToolbarBinding
import uk.co.zac_h.spacex.databinding.FragmentDragonDetailsBinding
import uk.co.zac_h.spacex.model.spacex.Dragon
import uk.co.zac_h.spacex.utils.metricFormat
import uk.co.zac_h.spacex.utils.setImageAndTint
import uk.co.zac_h.spacex.vehicles.adapters.DragonThrusterAdapter

class DragonDetailsFragment : BaseFragment() {

    override var title: String = "Dragon"

    private lateinit var binding: FragmentDragonDetailsBinding

    private lateinit var toolbarBinding: CollapsingToolbarBinding

    private var dragon: Dragon? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform()

        dragon = arguments?.getParcelable("dragon") as Dragon?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentDragonDetailsBinding.inflate(inflater, container, false).apply {
        binding = this
        toolbarBinding = CollapsingToolbarBinding.bind(this.root)
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setup(toolbarBinding.toolbar, toolbarBinding.toolbarLayout)

        title = dragon?.name ?: title

        with(binding) {
            dragon?.let {
                dragonDetailsCoordinator.transitionName = it.id

                Glide.with(view)
                    .load(it.flickr?.random())
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .into(toolbarBinding.header)

                dragonDetailsText.text = it.description

                when (it.active) {
                    true -> dragonDetailsStatusImage.setImageAndTint(
                        R.drawable.ic_check_circle_black_24dp,
                        R.color.success
                    )
                    false -> dragonDetailsStatusImage.setImageAndTint(
                        R.drawable.ic_remove_circle_black_24dp,
                        R.color.failed
                    )
                }

                dragonDetailsCrewCapacityText.text = it.crewCapacity.toString()
                dragonDetailsFirstFlightText.text = it.firstFlight
                dragonDetailsDryMassText.text = getString(
                    R.string.mass_formatted,
                    it.dryMass?.kg,
                    it.dryMass?.lb
                )
                it.heightWithTrunk?.let { heightWithTrunk ->
                    dragonDetailsHeightText.text = getString(
                        R.string.measurements,
                        heightWithTrunk.meters?.metricFormat(),
                        heightWithTrunk.feet?.metricFormat()
                    )
                }
                it.diameter?.let { diameter ->
                    dragonDetailsDiameterText.text = getString(
                        R.string.measurements,
                        diameter.meters?.metricFormat(),
                        diameter.feet?.metricFormat()
                    )
                }

                dragonDetailsShieldMaterialText.text = it.heatShield?.material
                dragonDetailsShieldSizeText.text =
                    it.heatShield?.size.toString() //TODO: Format with units
                dragonDetailsShieldTempText.text =
                    it.heatShield?.temp.toString() //TODO: Format with units

                dragonDetailsThrusterRecycler.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    setHasFixedSize(true)
                    adapter = it.thrusters?.let { thrusters ->
                        DragonThrusterAdapter(requireContext(), thrusters)
                    }
                }

                it.launchPayloadMass?.let { launchPayloadMass ->
                    dragonDetailsLaunchMassText.text = getString(
                        R.string.mass_formatted,
                        launchPayloadMass.kg,
                        launchPayloadMass.lb
                    )
                }

                it.returnPayloadMass?.let { returnPayloadMass ->
                    dragonDetailsReturnMassText.text = getString(
                        R.string.mass_formatted,
                        returnPayloadMass.kg,
                        returnPayloadMass.lb
                    )
                }

                it.launchPayloadVolume?.let { launchPayloadVolume ->
                    dragonDetailsLaunchVolText.text = getString(
                        R.string.volume_formatted,
                        launchPayloadVolume.cubicMeters?.metricFormat(),
                        launchPayloadVolume.cubicFeet?.metricFormat()
                    )
                }

                it.returnPayloadVol?.let { returnPayloadVol ->
                    dragonDetailsReturnVolText.text = getString(
                        R.string.volume_formatted,
                        returnPayloadVol.cubicMeters?.metricFormat(),
                        returnPayloadVol.cubicFeet?.metricFormat()
                    )
                }

                it.pressurizedCapsule?.payloadVolume?.let { payloadVolume ->
                    dragonDetailsPressurizedVolText.text = getString(
                        R.string.volume_formatted,
                        payloadVolume.cubicMeters?.metricFormat(),
                        payloadVolume.cubicFeet?.metricFormat()
                    )
                }

                it.trunk?.trunkVolume?.let { trunkVolume ->
                    dragonDetailsTrunkVolText.text = getString(
                        R.string.volume_formatted,
                        trunkVolume.cubicMeters?.metricFormat(),
                        trunkVolume.cubicFeet?.metricFormat()
                    )
                }

                dragonDetailsSolarArrayText.text = (it.trunk?.cargo?.solarArray ?: 0).toString()

                when (it.trunk?.cargo?.unpressurizedCargo) {
                    true -> dragonDetailsUnpressurizedCargoImage.setImageAndTint(
                        R.drawable.ic_check_circle_black_24dp,
                        R.color.success
                    )
                    false -> dragonDetailsUnpressurizedCargoImage.setImageAndTint(
                        R.drawable.ic_remove_circle_black_24dp,
                        R.color.failed
                    )
                }
            }
        }
    }

}
