package uk.co.zac_h.spacex.vehicles.dragon.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.databinding.FragmentDragonDetailsBinding
import uk.co.zac_h.spacex.model.spacex.Dragon
import uk.co.zac_h.spacex.utils.metricFormat
import uk.co.zac_h.spacex.utils.setImageAndTint
import uk.co.zac_h.spacex.vehicles.adapters.DragonThrusterAdapter

class DragonDetailsFragment : Fragment() {

    private var binding: FragmentDragonDetailsBinding? = null

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
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = NavHostFragment.findNavController(this)
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        val appBarConfig =
            AppBarConfiguration.Builder((context?.applicationContext as App).startDestinations)
                .setOpenableLayout(drawerLayout).build()

        binding?.apply {
            NavigationUI.setupWithNavController(
                toolbarLayout,
                toolbar,
                navController,
                appBarConfig
            )

            dragon?.let {
                dragonDetailsCoordinator.transitionName = it.id

                toolbar.title = it.name

                Glide.with(view)
                    .load(it.flickr?.random())
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .into(header)

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
                dragonDetailsDryMassText.text = context?.getString(
                    R.string.mass_formatted,
                    it.dryMass?.kg,
                    it.dryMass?.lb
                )
                it.heightWithTrunk?.let { heightWithTrunk ->
                    dragonDetailsHeightText.text = context?.getString(
                        R.string.measurements,
                        heightWithTrunk.meters?.metricFormat(),
                        heightWithTrunk.feet?.metricFormat()
                    )
                }
                it.diameter?.let { diameter ->
                    dragonDetailsDiameterText.text = context?.getString(
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
                    layoutManager = LinearLayoutManager(this@DragonDetailsFragment.context)
                    setHasFixedSize(true)
                    adapter = it.thrusters?.let { thrusters ->
                        DragonThrusterAdapter(this@DragonDetailsFragment.context, thrusters)
                    }
                }

                it.launchPayloadMass?.let { launchPayloadMass ->
                    dragonDetailsLaunchMassText.text = context?.getString(
                        R.string.mass_formatted,
                        launchPayloadMass.kg,
                        launchPayloadMass.lb
                    )
                }

                it.returnPayloadMass?.let { returnPayloadMass ->
                    dragonDetailsReturnMassText.text = context?.getString(
                        R.string.mass_formatted,
                        returnPayloadMass.kg,
                        returnPayloadMass.lb
                    )
                }

                it.launchPayloadVolume?.let { launchPayloadVolume ->
                    dragonDetailsLaunchVolText.text = context?.getString(
                        R.string.volume_formatted,
                        launchPayloadVolume.cubicMeters?.metricFormat(),
                        launchPayloadVolume.cubicFeet?.metricFormat()
                    )
                }

                it.returnPayloadVol?.let { returnPayloadVol ->
                    dragonDetailsReturnVolText.text = context?.getString(
                        R.string.volume_formatted,
                        returnPayloadVol.cubicMeters?.metricFormat(),
                        returnPayloadVol.cubicFeet?.metricFormat()
                    )
                }

                it.pressurizedCapsule?.payloadVolume?.let { payloadVolume ->
                    dragonDetailsPressurizedVolText.text = context?.getString(
                        R.string.volume_formatted,
                        payloadVolume.cubicMeters?.metricFormat(),
                        payloadVolume.cubicFeet?.metricFormat()
                    )
                }

                it.trunk?.trunkVolume?.let { trunkVolume ->
                    dragonDetailsTrunkVolText.text = context?.getString(
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}
