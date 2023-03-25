package uk.co.zac_h.spacex.feature.vehicles.rockets.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.transition.MaterialSharedAxis
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.utils.metricFormat
import uk.co.zac_h.spacex.core.common.viewpager.ViewPagerFragment
import uk.co.zac_h.spacex.feature.vehicles.R
import uk.co.zac_h.spacex.feature.vehicles.adapters.RocketPayloadAdapter
import uk.co.zac_h.spacex.feature.vehicles.databinding.FragmentRocketDetailsBinding
import uk.co.zac_h.spacex.feature.vehicles.rockets.LauncherItem
import uk.co.zac_h.spacex.feature.vehicles.rockets.PayloadWeights
import uk.co.zac_h.spacex.feature.vehicles.rockets.RocketViewModel

class RocketDetailsFragment : BaseFragment(), ViewPagerFragment {

    override val title: String by lazy { navArgs.label ?: title }

    private val navArgs: RocketDetailsFragmentArgs by navArgs()

    private val viewModel: RocketViewModel by navGraphViewModels(R.id.vehicles_nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var binding: FragmentRocketDetailsBinding

    private val rocketPayloadAdapter: RocketPayloadAdapter = RocketPayloadAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentRocketDetailsBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rocketDetailsPayloadRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = rocketPayloadAdapter
        }

        viewModel.rockets.observe(viewLifecycleOwner) { result ->
            result.data?.first { it.id == viewModel.selectedId }?.let { update(it) }
        }

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun update(rocket: LauncherItem?) {
        with(binding) {
            rocket?.let {
                Glide.with(requireContext())
                    .load(it.imageUrl)
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .into(rocketImage)

                toolbar.title = rocket.fullName

                rocketDetailsText.text = it.description

                rocketDetailsSuccessRate.text = it.successRate?.let { successRate ->
                    getString(R.string.percentage, successRate.metricFormat())
                }

                rocketDetailsFirstFlight.text = it.maidenFlight
                rocketDetailsStages.text = it.stages.toString()

                rocketDetailsHeight.text = it.length?.let { height ->
                    getString(R.string.measurements, height.metricFormat())
                }
                rocketDetailsDiameter.text = it.diameter?.let { diameter ->
                    getString(R.string.measurements, diameter.metricFormat())
                }
                rocketDetailsMass.text = it.launchMass?.let { mass ->
                    getString(R.string.mass_formatted, mass)
                }
                rocketDetailsThrustSea.text = it.toThrust?.let { thrust ->
                    getString(R.string.thrust, thrust.metricFormat())
                }

                rocketDetailsMassOrbitLabel.isVisible =
                    it.gtoCapacity != null || it.leoCapacity != null

                rocketPayloadAdapter.submitList(
                    listOfNotNull(
                        it.gtoCapacity?.let { gtoCapacity ->
                            PayloadWeights("GTO", gtoCapacity)
                        },
                        it.leoCapacity?.let { leoCapacity ->
                            PayloadWeights("LEO", leoCapacity)
                        }
                    )
                )
            }
        }
    }
}
