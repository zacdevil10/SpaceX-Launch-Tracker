package uk.co.zac_h.spacex.vehicles.rockets.details

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
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.databinding.FragmentRocketDetailsBinding
import uk.co.zac_h.spacex.model.spacex.RocketsModel
import uk.co.zac_h.spacex.utils.metricFormat
import uk.co.zac_h.spacex.utils.setImageAndTint
import uk.co.zac_h.spacex.vehicles.adapters.RocketPayloadAdapter
import java.text.DecimalFormat

class RocketDetailsFragment : Fragment() {

    private var _binding: FragmentRocketDetailsBinding? = null
    private val binding get() = _binding!!

    private var rocket: RocketsModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform()

        rocket = arguments?.getParcelable("rocket") as RocketsModel?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRocketDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = NavHostFragment.findNavController(this)
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        val appBarConfig =
            AppBarConfiguration.Builder((context?.applicationContext as App).startDestinations)
                .setDrawerLayout(drawerLayout).build()

        NavigationUI.setupWithNavController(
            binding.toolbarLayout,
            binding.toolbar,
            navController,
            appBarConfig
        )

        rocket?.let {
            binding.rocketDetailsCoordinator.transitionName = it.id

            binding.toolbar.title = it.name

            binding.header.setImageResource(
                when (it.id) {
                    "5e9d0d95eda69955f709d1eb" -> R.drawable.falcon1
                    "5e9d0d95eda69973a809d1ec" -> R.drawable.falcon9
                    "5e9d0d95eda69974db09d1ed" -> R.drawable.falconheavy
                    "5e9d0d96eda699382d09d1ee" -> R.drawable.starship
                    else -> R.drawable.starship //TODO: Add error image.
                }
            )

            binding.rocketDetailsText.text = it.description

            when (it.isActive) {
                true -> binding.rocketDetailsStatusImage.setImageAndTint(
                    R.drawable.ic_check_circle_black_24dp,
                    R.color.success
                )
                false -> binding.rocketDetailsStatusImage.setImageAndTint(
                    R.drawable.ic_remove_circle_black_24dp,
                    R.color.failed
                )
            }

            binding.rocketDetailsCostText.text =
                DecimalFormat("$#,###.00").format(it.costPerLaunch).toString()
            binding.rocketDetailsSuccessText.text =
                context?.getString(R.string.percentage, it.successRate)
            binding.rocketDetailsFirstFlightText.text = it.firstFlight
            binding.rocketDetailsStagesText.text = it.stages.toString()

            it.height?.let { height ->
                binding.rocketDetailsHeightText.text = context?.getString(
                    R.string.measurements,
                    height.meters?.metricFormat(),
                    height.feet?.metricFormat()
                )
            }
            it.diameter?.let { diameter ->
                binding.rocketDetailsDiameterText.text = context?.getString(
                    R.string.measurements,
                    diameter.meters?.metricFormat(),
                    diameter.feet?.metricFormat()
                )
            }
            it.mass?.let { mass ->
                binding.rocketDetailsMassText.text = context?.getString(
                    R.string.mass_formatted,
                    mass.kg?.metricFormat(),
                    mass.lb?.metricFormat()
                )
            }

            it.firstStage?.let { firstStage ->
                when (firstStage.reusable) {
                    true -> binding.rocketDetailsReusableImage.setImageAndTint(
                        R.drawable.ic_check_circle_black_24dp,
                        R.color.success
                    )
                    false -> binding.rocketDetailsReusableImage.setImageAndTint(
                        R.drawable.ic_remove_circle_black_24dp,
                        R.color.failed
                    )
                }

                binding.rocketDetailsEnginesFirstText.text = firstStage.engines.toString()
                binding.rocketDetailsFuelFirstText.text = context?.getString(
                    R.string.ton_format,
                    firstStage.fuelAmountTons?.metricFormat()
                )
                binding.rocketDetailsBurnFirstText.text =
                    context?.getString(R.string.seconds_format, firstStage.burnTimeSec ?: 0)
                binding.rocketDetailsThrustSeaText.text = context?.getString(
                    R.string.thrust,
                    firstStage.thrustSeaLevel?.kN?.metricFormat(),
                    firstStage.thrustSeaLevel?.lbf?.metricFormat()
                )
                binding.rocketDetailsThrustVacText.text = context?.getString(
                    R.string.thrust,
                    firstStage.thrustVacuum?.kN?.metricFormat(),
                    firstStage.thrustVacuum?.lbf?.metricFormat()
                )
            }

            it.secondStage?.let { secondStage ->
                binding.rocketDetailsEnginesSecondText.text = secondStage.engines.toString()
                binding.rocketDetailsFuelSecondText.text =
                    context?.getString(
                        R.string.ton_format,
                        secondStage.fuelAmountTons?.metricFormat()
                    )
                binding.rocketDetailsBurnSecondText.text =
                    context?.getString(R.string.seconds_format, secondStage.burnTimeSec ?: 0)
                binding.rocketDetailsThrustSecondText.text = context?.getString(
                    R.string.thrust,
                    secondStage.thrust?.kN?.metricFormat(),
                    secondStage.thrust?.lbf?.metricFormat()
                )
            }

            binding.rocketDetailsPayloadRecycler.apply {
                layoutManager = LinearLayoutManager(this@RocketDetailsFragment.context)
                setHasFixedSize(true)
                adapter =
                    RocketPayloadAdapter(this@RocketDetailsFragment.context, it.payloadWeights)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
