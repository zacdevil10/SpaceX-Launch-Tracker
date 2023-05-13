package uk.co.zac_h.spacex.feature.vehicles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.navigation.ui.setupWithNavController
import androidx.paging.map
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.feature.vehicles.adapters.HeaderAdapter
import uk.co.zac_h.spacex.feature.vehicles.adapters.SpecsAdapter
import uk.co.zac_h.spacex.feature.vehicles.adapters.VehiclesPagingAdapter
import uk.co.zac_h.spacex.feature.vehicles.databinding.FragmentVehicleDetailsBinding
import uk.co.zac_h.spacex.feature.vehicles.launcher.CoreItem

class VehicleDetailsFragment : Fragment() {

    private val viewModel: VehicleDetailsViewModel by navGraphViewModels(R.id.vehicles_nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var binding: FragmentVehicleDetailsBinding

    private val headerAdapter = HeaderAdapter()
    private val specsAdapter = SpecsAdapter()
    private val launcherAdapter = VehiclesPagingAdapter {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentVehicleDetailsBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val concatAdapter = ConcatAdapter(headerAdapter, specsAdapter, launcherAdapter)

        binding.toolbar.title = viewModel.vehicle?.title

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = concatAdapter
        }

        headerAdapter.submitList(listOf(viewModel.header))

        specsAdapter.submitList(viewModel.generateSpecsList(viewModel.vehicle))

        viewModel.launcherLiveData?.observe(viewLifecycleOwner) { pagingData ->
            launcherAdapter.submitData(
                lifecycle,
                pagingData.map { CoreItem(it) })
        }

        binding.toolbar.setupWithNavController(findNavController())
    }
}