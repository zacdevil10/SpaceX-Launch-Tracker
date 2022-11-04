package uk.co.zac_h.spacex.vehicles.ships

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.core.databinding.FragmentVerticalRecyclerviewBinding
import uk.co.zac_h.spacex.core.fragment.BaseFragment
import uk.co.zac_h.spacex.core.viewpager.ViewPagerFragment
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.CachePolicy
import uk.co.zac_h.spacex.vehicles.VehiclesFilterViewModel
import uk.co.zac_h.spacex.vehicles.VehiclesPage
import uk.co.zac_h.spacex.vehicles.adapters.ShipsAdapter

class ShipsFragment : BaseFragment(), ViewPagerFragment {

    override var title: String = "Ships"

    private lateinit var binding: FragmentVerticalRecyclerviewBinding

    private val viewModel: ShipsViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private val filterViewModel: VehiclesFilterViewModel by activityViewModels()

    private lateinit var shipsAdapter: ShipsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentVerticalRecyclerviewBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shipsAdapter = ShipsAdapter()

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(this@ShipsFragment.context)
            setHasFixedSize(true)
            adapter = shipsAdapter
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getShips(CachePolicy.REFRESH)
        }

        viewModel.ships.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResult.Pending -> showProgress()
                is ApiResult.Success -> {
                    hideProgress()
                    binding.swipeRefresh.isRefreshing = false
                    result.data?.let { data ->
                        shipsAdapter.submitList(data) {
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
            viewModel.setOrder(it[VehiclesPage.SHIPS])
            viewModel.getShips()
        }

        viewModel.getShips()
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
        viewModel.getShips()
    }
}
