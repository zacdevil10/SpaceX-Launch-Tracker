package uk.co.zac_h.spacex.vehicles.rockets.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.image.setImageAndTint
import uk.co.zac_h.spacex.core.common.utils.metricFormat
import uk.co.zac_h.spacex.core.common.utils.setupCollapsingToolbar
import uk.co.zac_h.spacex.core.common.viewpager.ViewPagerFragment
import uk.co.zac_h.spacex.databinding.CollapsingToolbarBinding
import uk.co.zac_h.spacex.databinding.FragmentRocketDetailsBinding
import uk.co.zac_h.spacex.vehicles.adapters.RocketPayloadAdapter
import uk.co.zac_h.spacex.vehicles.rockets.Rocket
import uk.co.zac_h.spacex.vehicles.rockets.RocketViewModel

class RocketDetailsFragment : BaseFragment(), ViewPagerFragment {

    override val title: String by lazy { navArgs.label ?: title }

    private val navArgs: RocketDetailsFragmentArgs by navArgs()

    private val viewModel: RocketViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var binding: FragmentRocketDetailsBinding
    private lateinit var toolbarBinding: CollapsingToolbarBinding

    private lateinit var rocketPayloadAdapter: RocketPayloadAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentRocketDetailsBinding.inflate(inflater, container, false).apply {
        toolbarBinding = CollapsingToolbarBinding.bind(this.root)
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarBinding.toolbar.setupCollapsingToolbar(title)

        rocketPayloadAdapter = RocketPayloadAdapter(requireContext())

        binding.rocketDetailsPayloadRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = rocketPayloadAdapter
        }

        viewModel.rockets.observe(viewLifecycleOwner) { result ->
            result.data?.first { it.id == viewModel.selectedId }?.let { update(it) }
        }
    }

    private fun update(rocket: Rocket?) {
        with(binding) {
            rocket?.let {
                rocketDetailsCoordinator.transitionName = it.id

                Glide.with(requireContext())
                    .load(it.flickr?.random())
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .into(toolbarBinding.header)

                rocketDetailsText.text = it.description

                if (it.isActive == true) rocketDetailsStatusImage.setImageAndTint(
                    R.drawable.ic_check_circle_black_24dp,
                    R.color.success
                ) else rocketDetailsStatusImage.setImageAndTint(
                    R.drawable.ic_remove_circle_black_24dp,
                    R.color.failed
                )

                rocketDetailsCostText.text = it.costPerLaunch
                rocketDetailsSuccessText.text =
                    getString(R.string.percentage, it.successRate)
                rocketDetailsFirstFlightText.text = it.firstFlight
                rocketDetailsStagesText.text = it.stages.toString()

                it.height?.let { height ->
                    rocketDetailsHeightText.text = getString(
                        R.string.measurements,
                        height.meters?.metricFormat(),
                        height.feet?.metricFormat()
                    )
                }
                it.diameter?.let { diameter ->
                    rocketDetailsDiameterText.text = getString(
                        R.string.measurements,
                        diameter.meters?.metricFormat(),
                        diameter.feet?.metricFormat()
                    )
                }
                it.mass?.let { mass ->
                    rocketDetailsMassText.text = getString(
                        R.string.mass_formatted,
                        mass.kg,
                        mass.lb
                    )
                }

                it.firstStage?.let { firstStage ->
                    if (firstStage.reusable == true) rocketDetailsReusableImage.setImageAndTint(
                        R.drawable.ic_check_circle_black_24dp,
                        R.color.success
                    ) else rocketDetailsReusableImage.setImageAndTint(
                        R.drawable.ic_remove_circle_black_24dp,
                        R.color.failed
                    )

                    rocketDetailsEnginesFirstText.text = firstStage.engines.toString()
                    rocketDetailsFuelFirstText.text = getString(
                        R.string.ton_format,
                        firstStage.fuelAmountTons?.metricFormat()
                    )
                    rocketDetailsBurnFirstText.text = getString(
                        R.string.seconds_format,
                        firstStage.burnTimeSec ?: 0
                    )
                    rocketDetailsThrustSeaText.text = getString(
                        R.string.thrust,
                        firstStage.thrustSeaLevel?.kN?.metricFormat(),
                        firstStage.thrustSeaLevel?.lbf?.metricFormat()
                    )
                    rocketDetailsThrustVacText.text = getString(
                        R.string.thrust,
                        firstStage.thrustVacuum?.kN?.metricFormat(),
                        firstStage.thrustVacuum?.lbf?.metricFormat()
                    )
                }

                it.secondStage?.let { secondStage ->
                    rocketDetailsEnginesSecondText.text = secondStage.engines.toString()
                    rocketDetailsFuelSecondText.text = getString(
                        R.string.ton_format,
                        secondStage.fuelAmountTons?.metricFormat()
                    )
                    rocketDetailsBurnSecondText.text = getString(
                        R.string.seconds_format,
                        secondStage.burnTimeSec ?: 0
                    )
                    rocketDetailsThrustSecondText.text = getString(
                        R.string.thrust,
                        secondStage.thrust?.kN?.metricFormat(),
                        secondStage.thrust?.lbf?.metricFormat()
                    )
                }

                rocketPayloadAdapter.submitList(it.payloadWeights)
            }
        }
    }
}
