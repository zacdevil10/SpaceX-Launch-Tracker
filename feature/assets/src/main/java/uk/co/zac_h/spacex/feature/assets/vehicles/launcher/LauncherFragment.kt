package uk.co.zac_h.spacex.feature.assets.vehicles.launcher

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.recyclerview.PagingLoadStateAdapter
import uk.co.zac_h.spacex.core.common.utils.orUnknown
import uk.co.zac_h.spacex.core.common.viewpager.ViewPagerFragment
import uk.co.zac_h.spacex.core.ui.databinding.FragmentVerticalRecyclerviewBinding
import uk.co.zac_h.spacex.feature.assets.R
import uk.co.zac_h.spacex.feature.assets.vehicles.VehicleDetailsViewModel
import uk.co.zac_h.spacex.feature.assets.vehicles.adapters.VehiclesPagingAdapter

class LauncherFragment : BaseFragment(), ViewPagerFragment {

    override var title: String = "Cores"

    private lateinit var binding: FragmentVerticalRecyclerviewBinding

    private val viewModel: LauncherViewModel by navGraphViewModels(R.id.assets_nav_graph) {
        defaultViewModelProviderFactory
    }

    private val vehicleDetailsViewModel: VehicleDetailsViewModel by navGraphViewModels(R.id.assets_nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var launcherAdapter: VehiclesPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentVerticalRecyclerviewBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launcherAdapter = VehiclesPagingAdapter { vehicleDetailsViewModel.vehicle = it }

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = launcherAdapter.withLoadStateFooter(
                footer = PagingLoadStateAdapter(launcherAdapter::retry)
            )
        }

        viewModel.launcherLiveData.observe(viewLifecycleOwner) { pagingData ->
            launcherAdapter.submitData(lifecycle, pagingData.map { CoreItem(it) })
        }

        binding.swipeRefresh.setOnRefreshListener {
            launcherAdapter.refresh()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            launcherAdapter.loadStateFlow.collectLatest {
                if (it.refresh is LoadState.Loading) {
                    binding.progress.show()
                } else {
                    binding.swipeRefresh.isRefreshing = false
                    binding.progress.hide()

                    val error = when {
                        it.prepend is LoadState.Error -> it.prepend as LoadState.Error
                        it.append is LoadState.Error -> it.append as LoadState.Error
                        it.refresh is LoadState.Error -> it.refresh as LoadState.Error
                        else -> null
                    }

                    error?.error?.let { message -> showError(message) }
                }
            }
        }
    }

    private fun showError(error: Throwable) {
        Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
        Log.e("LauncherFragment", error.message.orUnknown())
    }

    override fun networkAvailable() {
        launcherAdapter.retry()
    }
}
