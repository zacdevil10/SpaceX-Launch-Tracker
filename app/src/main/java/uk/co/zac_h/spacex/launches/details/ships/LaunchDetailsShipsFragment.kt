package uk.co.zac_h.spacex.launches.details.ships

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentVerticalRecyclerviewBinding
import uk.co.zac_h.spacex.launches.adapters.LaunchDetailsShipsAdapter
import uk.co.zac_h.spacex.launches.details.LaunchDetailsContainerViewModel
import uk.co.zac_h.spacex.vehicles.ships.Ship

class LaunchDetailsShipsFragment : BaseFragment() {

    private val viewModel: LaunchDetailsContainerViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var binding: FragmentVerticalRecyclerviewBinding

    private lateinit var shipsAdapter: LaunchDetailsShipsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentVerticalRecyclerviewBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progress.hide()

        shipsAdapter = LaunchDetailsShipsAdapter()

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = shipsAdapter
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getLaunch(CachePolicy.REFRESH)
        }

        viewModel.launch.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResult.Pending -> {}
                is ApiResult.Success -> {
                    binding.swipeRefresh.isRefreshing = false
                    update(response.data?.ships)
                }
                is ApiResult.Failure -> binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun update(response: List<Ship>?) {
        shipsAdapter.submitList(response)
    }
}