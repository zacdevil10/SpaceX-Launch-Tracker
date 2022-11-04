package uk.co.zac_h.spacex.vehicles.capsules

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.types.Order
import uk.co.zac_h.spacex.core.common.viewpager.ViewPagerFragment
import uk.co.zac_h.spacex.core.ui.databinding.FragmentVerticalRecyclerviewBinding
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.CachePolicy
import uk.co.zac_h.spacex.vehicles.VehiclesFilterViewModel
import uk.co.zac_h.spacex.vehicles.VehiclesPage
import uk.co.zac_h.spacex.vehicles.adapters.CapsulesAdapter

class CapsulesFragment : BaseFragment(), ViewPagerFragment {

    override var title: String = "Capsules"

    private lateinit var binding: FragmentVerticalRecyclerviewBinding

    private val viewModel: CapsulesViewModel by viewModels()

    private val filterViewModel: VehiclesFilterViewModel by activityViewModels()

    private lateinit var capsulesAdapter: CapsulesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentVerticalRecyclerviewBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        capsulesAdapter = CapsulesAdapter()

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = capsulesAdapter
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.get(CachePolicy.REFRESH)
        }

        viewModel.capsules.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResult.Pending -> showProgress()
                is ApiResult.Success -> {
                    hideProgress()
                    binding.swipeRefresh.isRefreshing = false
                    result.data?.let { data ->
                        capsulesAdapter.submitList(data) {
                            binding.recycler.scrollToPosition(0)
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
            viewModel.order = it[VehiclesPage.CAPSULES] ?: Order.ASCENDING
            viewModel.get()
        }

        viewModel.get()
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
        viewModel.get()
    }
}
