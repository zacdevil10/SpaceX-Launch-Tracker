package uk.co.zac_h.spacex.feature.vehicles.rockets.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.utils.metricFormat
import uk.co.zac_h.spacex.core.common.utils.setupCollapsingToolbar
import uk.co.zac_h.spacex.core.common.viewpager.ViewPagerFragment
import uk.co.zac_h.spacex.core.ui.databinding.CollapsingToolbarBinding
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

    private fun update(rocket: LauncherItem?) {
        with(binding) {
            rocket?.let {
                rocketDetailsCoordinator.transitionName = it.id

                Glide.with(requireContext())
                    .load(it.imageUrl)
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .into(toolbarBinding.header)

                rocketDetailsText.text = it.description

                rocketDetailsSuccessText.text =
                    getString(R.string.percentage, it.successRate?.metricFormat())
                rocketDetailsFirstFlightText.text = it.maidenFlight
                rocketDetailsStagesText.text = it.stages.toString()

                it.length?.let { height ->
                    rocketDetailsHeightText.text = getString(
                        R.string.measurements,
                        height.metricFormat(),
                    )
                }
                it.diameter?.let { diameter ->
                    rocketDetailsDiameterText.text = getString(
                        R.string.measurements,
                        diameter.metricFormat(),
                    )
                }
                it.launchMass?.let { mass ->
                    rocketDetailsMassText.text = getString(
                        R.string.mass_formatted,
                        mass
                    )
                }
                it.toThrust?.let { thrust ->
                    rocketDetailsThrustSeaText.text = getString(
                        R.string.thrust,
                        thrust.metricFormat(),
                    )
                }

                rocketPayloadAdapter.submitList(
                    listOf(
                        PayloadWeights(
                            "GTO",
                            it.gtoCapacity
                        ),
                        PayloadWeights(
                            "LEO",
                            it.leoCapacity
                        )
                    )
                )
            }
        }
    }
}
