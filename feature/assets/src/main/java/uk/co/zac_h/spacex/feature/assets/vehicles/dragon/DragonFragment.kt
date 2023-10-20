package uk.co.zac_h.spacex.feature.assets.vehicles.dragon

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.utils.orUnknown
import uk.co.zac_h.spacex.core.common.viewpager.ViewPagerFragment
import uk.co.zac_h.spacex.core.ui.databinding.FragmentVerticalRecyclerviewBinding
import uk.co.zac_h.spacex.feature.assets.R
import uk.co.zac_h.spacex.feature.assets.vehicles.VehicleDetailsViewModel
import uk.co.zac_h.spacex.feature.assets.vehicles.adapters.VehiclesAdapter
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.CachePolicy

class DragonFragment : BaseFragment(), ViewPagerFragment {

    override var title: String = "Second stage"

    private lateinit var binding: FragmentVerticalRecyclerviewBinding

    private val viewModel: DragonViewModel by viewModels()

    private val vehicleDetailsViewModel: VehicleDetailsViewModel by navGraphViewModels(R.id.assets_nav_graph) {
        defaultViewModelProviderFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentVerticalRecyclerviewBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dragonAdapter = VehiclesAdapter { vehicleDetailsViewModel.vehicle = it }

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = dragonAdapter
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getDragons(CachePolicy.REFRESH)
        }

        viewModel.dragons.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResult.Pending -> binding.progress.show()
                is ApiResult.Success -> {
                    binding.progress.hide()
                    binding.swipeRefresh.isRefreshing = false
                    dragonAdapter.submitList(it.result) {
                        with(viewModel) {
                            if (hasOrderChanged) binding.recycler.smoothScrollToPosition(0)
                            hasOrderChanged = false
                        }
                    }
                }

                is ApiResult.Failure -> {
                    binding.progress.hide()
                    binding.swipeRefresh.isRefreshing = false
                    showError(it.exception)
                }
            }
        }

        viewModel.getDragons()
    }

    private fun showError(error: Throwable) {
        Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
        Log.e("DragonFragment", error.message.orUnknown())
    }

    override fun networkAvailable() {
        viewModel.getDragons()
    }
}
