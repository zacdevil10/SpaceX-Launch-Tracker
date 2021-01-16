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
import com.bumptech.glide.Glide
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.databinding.FragmentRocketDetailsBinding
import uk.co.zac_h.spacex.model.spacex.Rocket
import uk.co.zac_h.spacex.utils.metricFormat
import uk.co.zac_h.spacex.utils.setImageAndTint
import uk.co.zac_h.spacex.vehicles.adapters.RocketPayloadAdapter

class RocketDetailsFragment : Fragment() {

    private var binding: FragmentRocketDetailsBinding? = null

    private var rocket: Rocket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform()

        rocket = arguments?.getParcelable("rocket") as Rocket?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRocketDetailsBinding.inflate(inflater, container, false)
        return binding?.root
    }

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

            rocket?.let {
                rocketDetailsCoordinator.transitionName = it.id

                toolbar.title = it.name

                Glide.with(view)
                    .load(it.flickr?.random())
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .into(header)

                rocketDetailsText.text = it.description

                when (it.isActive) {
                    true -> rocketDetailsStatusImage.setImageAndTint(
                        R.drawable.ic_check_circle_black_24dp,
                        R.color.success
                    )
                    false -> rocketDetailsStatusImage.setImageAndTint(
                        R.drawable.ic_remove_circle_black_24dp,
                        R.color.failed
                    )
                }

                rocketDetailsCostText.text = it.costPerLaunch
                rocketDetailsSuccessText.text =
                    context?.getString(R.string.percentage, it.successRate)
                rocketDetailsFirstFlightText.text = it.firstFlight
                rocketDetailsStagesText.text = it.stages.toString()

                it.height?.let { height ->
                    rocketDetailsHeightText.text = context?.getString(
                        R.string.measurements,
                        height.meters?.metricFormat(),
                        height.feet?.metricFormat()
                    )
                }
                it.diameter?.let { diameter ->
                    rocketDetailsDiameterText.text = context?.getString(
                        R.string.measurements,
                        diameter.meters?.metricFormat(),
                        diameter.feet?.metricFormat()
                    )
                }
                it.mass?.let { mass ->
                    rocketDetailsMassText.text = context?.getString(
                        R.string.mass_formatted,
                        mass.kg,
                        mass.lb
                    )
                }

                it.firstStage?.let { firstStage ->
                    when (firstStage.reusable) {
                        true -> rocketDetailsReusableImage.setImageAndTint(
                            R.drawable.ic_check_circle_black_24dp,
                            R.color.success
                        )
                        false -> rocketDetailsReusableImage.setImageAndTint(
                            R.drawable.ic_remove_circle_black_24dp,
                            R.color.failed
                        )
                    }

                    rocketDetailsEnginesFirstText.text = firstStage.engines.toString()
                    rocketDetailsFuelFirstText.text = context?.getString(
                        R.string.ton_format,
                        firstStage.fuelAmountTons?.metricFormat()
                    )
                    rocketDetailsBurnFirstText.text =
                        context?.getString(R.string.seconds_format, firstStage.burnTimeSec ?: 0)
                    rocketDetailsThrustSeaText.text = context?.getString(
                        R.string.thrust,
                        firstStage.thrustSeaLevel?.kN?.metricFormat(),
                        firstStage.thrustSeaLevel?.lbf?.metricFormat()
                    )
                    rocketDetailsThrustVacText.text = context?.getString(
                        R.string.thrust,
                        firstStage.thrustVacuum?.kN?.metricFormat(),
                        firstStage.thrustVacuum?.lbf?.metricFormat()
                    )
                }

                it.secondStage?.let { secondStage ->
                    rocketDetailsEnginesSecondText.text = secondStage.engines.toString()
                    rocketDetailsFuelSecondText.text =
                        context?.getString(
                            R.string.ton_format,
                            secondStage.fuelAmountTons?.metricFormat()
                        )
                    rocketDetailsBurnSecondText.text =
                        context?.getString(R.string.seconds_format, secondStage.burnTimeSec ?: 0)
                    rocketDetailsThrustSecondText.text = context?.getString(
                        R.string.thrust,
                        secondStage.thrust?.kN?.metricFormat(),
                        secondStage.thrust?.lbf?.metricFormat()
                    )
                }

                rocketDetailsPayloadRecycler.apply {
                    layoutManager = LinearLayoutManager(this@RocketDetailsFragment.context)
                    setHasFixedSize(true)
                    adapter =
                        it.payloadWeights?.let { payloadWeights ->
                            RocketPayloadAdapter(
                                this@RocketDetailsFragment.context,
                                payloadWeights
                            )
                        }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
