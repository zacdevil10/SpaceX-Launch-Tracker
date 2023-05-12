package uk.co.zac_h.spacex.feature.vehicles.rockets.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navGraphViewModels
import androidx.paging.map
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialSharedAxis
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.utils.metricFormat
import uk.co.zac_h.spacex.feature.vehicles.R
import uk.co.zac_h.spacex.feature.vehicles.adapters.HeaderAdapter
import uk.co.zac_h.spacex.feature.vehicles.adapters.HeaderItem
import uk.co.zac_h.spacex.feature.vehicles.adapters.LauncherAdapter
import uk.co.zac_h.spacex.feature.vehicles.adapters.SpecsAdapter
import uk.co.zac_h.spacex.feature.vehicles.adapters.SpecsItem
import uk.co.zac_h.spacex.feature.vehicles.databinding.FragmentRocketDetailsBinding
import uk.co.zac_h.spacex.feature.vehicles.rockets.LauncherItem
import uk.co.zac_h.spacex.feature.vehicles.rockets.RocketViewModel

class RocketDetailsFragment : BaseFragment() {

    private val viewModel: RocketViewModel by navGraphViewModels(R.id.vehicles_nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var binding: FragmentRocketDetailsBinding

    private val headerAdapter = HeaderAdapter()
    private val specsAdapter = SpecsAdapter()
    private val launcherAdapter = LauncherAdapter {}

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

        val concatAdapter = ConcatAdapter(headerAdapter, specsAdapter, launcherAdapter)

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = concatAdapter
        }

        viewModel.selectedLauncher?.let { update(it) }

        viewModel.launcherLiveData.observe(viewLifecycleOwner) { pagingData ->
            launcherAdapter.submitData(
                lifecycle,
                pagingData.map { uk.co.zac_h.spacex.feature.vehicles.launcher.LauncherItem(it) })
        }

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun update(rocket: LauncherItem) {
        binding.toolbar.title = rocket.fullName

        headerAdapter.submitList(
            listOf(
                HeaderItem(
                    rocket.imageUrl,
                    rocket.description
                )
            )
        )

        specsAdapter.submitList(
            listOfNotNull(
                rocket.successRate?.let {
                    SpecsItem(
                        "Launch Success Rate",
                        getString(R.string.percentage, it.metricFormat())
                    )
                },
                rocket.maidenFlight?.let {
                    SpecsItem(
                        getString(R.string.first_flight_label),
                        it
                    )
                },
                rocket.stages?.let {
                    SpecsItem(
                        getString(R.string.stages_label),
                        it.toString()
                    )
                },
                rocket.length?.let {
                    SpecsItem(
                        getString(R.string.height_label),
                        getString(R.string.measurements, it.metricFormat())
                    )
                },
                rocket.diameter?.let {
                    SpecsItem(
                        getString(R.string.diameter_label),
                        getString(R.string.measurements, it.metricFormat())
                    )
                },
                rocket.launchMass?.let {
                    SpecsItem(
                        "Mass",
                        getString(R.string.mass_formatted, it)
                    )
                },
                rocket.toThrust?.let {
                    SpecsItem(
                        "Thrust at Liftoff",
                        getString(R.string.thrust, it.metricFormat())
                    )
                },
                rocket.leoCapacity?.let {
                    SpecsItem(
                        "Mass to LEO",
                        getString(R.string.mass, it.metricFormat())
                    )
                },
                rocket.gtoCapacity?.let {
                    SpecsItem(
                        "Mass to GTO",
                        getString(R.string.mass, it.metricFormat())
                    )
                },
            )
        )
    }
}
