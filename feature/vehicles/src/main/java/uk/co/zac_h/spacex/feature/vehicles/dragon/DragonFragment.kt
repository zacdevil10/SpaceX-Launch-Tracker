package uk.co.zac_h.spacex.feature.vehicles.dragon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.viewpager.ViewPagerFragment
import uk.co.zac_h.spacex.core.ui.databinding.FragmentVerticalRecyclerviewBinding
import uk.co.zac_h.spacex.feature.vehicles.R
import uk.co.zac_h.spacex.feature.vehicles.VehiclesFilterViewModel
import uk.co.zac_h.spacex.feature.vehicles.VehiclesPage
import uk.co.zac_h.spacex.feature.vehicles.adapters.DragonAdapter
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.CachePolicy

class DragonFragment : BaseFragment(), ViewPagerFragment {

    override var title: String = "Second stage"

    private lateinit var binding: FragmentVerticalRecyclerviewBinding

    private val viewModel: DragonViewModel by navGraphViewModels(R.id.vehicles_nav_graph) {
        defaultViewModelProviderFactory
    }

    private val filterViewModel: VehiclesFilterViewModel by navGraphViewModels(R.id.vehicles_nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var dragonAdapter: DragonAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentVerticalRecyclerviewBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dragonAdapter = DragonAdapter { viewModel.selectedId = it }

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = dragonAdapter
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getDragons(CachePolicy.REFRESH)
        }

        viewModel.dragons.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResult.Pending -> showProgress()
                is ApiResult.Success -> {
                    hideProgress()
                    binding.swipeRefresh.isRefreshing = false
                    result.data?.let { data ->
                        dragonAdapter.submitList(data) {
                            with(viewModel) {
                                if (hasOrderChanged) binding.recycler.smoothScrollToPosition(0)
                                hasOrderChanged = false
                            }
                        }
                    }
                }

                is ApiResult.Failure -> {
                    binding.swipeRefresh.isRefreshing = false
                    showError(result.exception.message)
                }
            }
        }

        filterViewModel.order.observe(viewLifecycleOwner) {
            viewModel.setOrder(it[VehiclesPage.DRAGON])
            viewModel.getDragons()
        }

        viewModel.getDragons()
    }

    private fun showProgress() {
        binding.progress.show()
    }

    private fun hideProgress() {
        binding.progress.hide()
    }

    private fun showError(error: String?) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        viewModel.getDragons()
    }
}
