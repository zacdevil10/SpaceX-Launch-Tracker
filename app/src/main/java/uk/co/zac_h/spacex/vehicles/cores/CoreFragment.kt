package uk.co.zac_h.spacex.vehicles.cores

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentVerticalRecyclerviewBinding
import uk.co.zac_h.spacex.types.Order
import uk.co.zac_h.spacex.vehicles.VehiclesFilterViewModel
import uk.co.zac_h.spacex.vehicles.VehiclesPage
import uk.co.zac_h.spacex.vehicles.adapters.CoreAdapter

class CoreFragment : BaseFragment() {

    override var title: String = "Cores"

    private lateinit var binding: FragmentVerticalRecyclerviewBinding

    private val viewModel: CoreViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private val filterViewModel: VehiclesFilterViewModel by activityViewModels()

    private lateinit var coreAdapter: CoreAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentVerticalRecyclerviewBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        coreAdapter = CoreAdapter()

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = coreAdapter
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getCores(CachePolicy.REFRESH)
        }

        viewModel.cores.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                ApiResult.Status.PENDING -> showProgress()
                ApiResult.Status.SUCCESS -> {
                    hideProgress()
                    binding.swipeRefresh.isRefreshing = false
                    result.data?.let { data ->
                        coreAdapter.submitList(data) {
                            binding.recycler.scrollToPosition(0)
                        }
                    }
                }
                ApiResult.Status.FAILURE -> {
                    binding.swipeRefresh.isRefreshing = false
                    showError(result.error?.message)
                }
            }
        }

        filterViewModel.order.observe(viewLifecycleOwner) {
            viewModel.order = it[VehiclesPage.CORES] ?: Order.ASCENDING
            viewModel.getCores()
        }

        viewModel.getCores()
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
        viewModel.getCores()
    }
}
