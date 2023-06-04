package uk.co.zac_h.spacex.feature.vehicles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.feature.vehicles.adapters.HeaderAdapter
import uk.co.zac_h.spacex.feature.vehicles.adapters.SpecsAdapter
import uk.co.zac_h.spacex.feature.vehicles.databinding.FragmentVehicleDetailsBinding

class VehicleDetailsFragment : Fragment() {

    private val viewModel: VehicleDetailsViewModel by navGraphViewModels(R.id.vehicles_nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var binding: FragmentVehicleDetailsBinding

    private val headerAdapter = HeaderAdapter()
    private val specsAdapter = SpecsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentVehicleDetailsBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val concatAdapter = ConcatAdapter(headerAdapter, specsAdapter)

        binding.toolbar.title = viewModel.vehicle?.title

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = concatAdapter
        }

        headerAdapter.submitList(listOf(viewModel.header))

        specsAdapter.submitList(viewModel.generateSpecsList(viewModel.vehicle))

        binding.toolbar.setupWithNavController(findNavController())
    }
}