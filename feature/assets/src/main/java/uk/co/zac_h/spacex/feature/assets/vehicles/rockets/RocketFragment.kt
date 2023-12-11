package uk.co.zac_h.spacex.feature.assets.vehicles.rockets

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.core.common.apiLimitReached
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.utils.orUnknown
import uk.co.zac_h.spacex.core.common.viewpager.ViewPagerFragment
import uk.co.zac_h.spacex.core.ui.databinding.FragmentVerticalRecyclerviewBinding
import uk.co.zac_h.spacex.feature.assets.R
import uk.co.zac_h.spacex.feature.assets.vehicles.VehicleDetailsViewModel
import uk.co.zac_h.spacex.feature.assets.vehicles.adapters.VehiclesAdapter
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.CachePolicy
import uk.co.zac_h.spacex.network.TooManyRequestsException

class RocketFragment : BaseFragment(), ViewPagerFragment {

    override var title: String = "Rockets"

    private lateinit var binding: FragmentVerticalRecyclerviewBinding

    private val viewModel: RocketViewModel by navGraphViewModels(R.id.assets_nav_graph) {
        defaultViewModelProviderFactory
    }

    private val vehicleDetailsViewModel: VehicleDetailsViewModel by navGraphViewModels(R.id.assets_nav_graph) {
        defaultViewModelProviderFactory
    }

    private var shouldScroll: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentVerticalRecyclerviewBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shouldScroll = false

        val rocketsAdapter = VehiclesAdapter { vehicleDetailsViewModel.vehicle = it }

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = rocketsAdapter
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getRockets(CachePolicy.REFRESH)
        }

        viewModel.rockets.observe(viewLifecycleOwner) {
            binding.banner.apiLimitReached((it as? ApiResult.Failure)?.exception as? TooManyRequestsException)
            when (it) {
                is ApiResult.Pending -> binding.progress.hide()
                is ApiResult.Success -> {
                    binding.progress.hide()
                    binding.swipeRefresh.isRefreshing = false
                    rocketsAdapter.submitList(it.result) {
                        if (shouldScroll) binding.recycler.smoothScrollToPosition(0)
                    }
                }

                is ApiResult.Failure -> {
                    binding.progress.hide()
                    binding.swipeRefresh.isRefreshing = false
                    showError(it.exception)
                }
            }
        }

        viewModel.getRockets()

        viewModel.filter.observe(viewLifecycleOwner) {
            shouldScroll = true
        }
    }

    override fun onResume() {
        super.onResume()

        shouldScroll = false
    }

    private fun showError(error: Throwable) {
        if (error !is TooManyRequestsException) {
            Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
        }
        Log.e("RocketFragment", error.message.orUnknown())
    }

    override fun networkAvailable() {
        viewModel.getRockets()
    }
}
