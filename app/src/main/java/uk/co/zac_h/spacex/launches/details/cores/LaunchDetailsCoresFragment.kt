package uk.co.zac_h.spacex.launches.details.cores

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
import uk.co.zac_h.spacex.dto.spacex.LaunchCore
import uk.co.zac_h.spacex.launches.adapters.FirstStageAdapter
import uk.co.zac_h.spacex.launches.details.LaunchDetailsContainerViewModel

class LaunchDetailsCoresFragment : BaseFragment() {

    private val viewModel: LaunchDetailsContainerViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var binding: FragmentVerticalRecyclerviewBinding

    private lateinit var coresAdapter: FirstStageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentVerticalRecyclerviewBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progress.hide()

        coresAdapter = FirstStageAdapter()

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = coresAdapter
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getLaunch(CachePolicy.REFRESH)
        }

        viewModel.launch.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                ApiResult.Status.PENDING -> {
                }
                ApiResult.Status.SUCCESS -> {
                    binding.swipeRefresh.isRefreshing = false
                    update(response.data?.cores)
                }
                ApiResult.Status.FAILURE -> binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun update(response: List<LaunchCore>?) {
        coresAdapter.submitList(response)
    }
}