package uk.co.zac_h.spacex.feature.launch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.utils.orUnknown
import uk.co.zac_h.spacex.core.common.viewpager.ViewPagerFragment
import uk.co.zac_h.spacex.core.ui.databinding.FragmentVerticalRecyclerviewBinding
import uk.co.zac_h.spacex.feature.launch.adapters.LaunchesAdapter
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.CachePolicy

@AndroidEntryPoint
class UpcomingLaunchesListFragment : BaseFragment(), ViewPagerFragment {

    override val title: String by lazy { "Upcoming" }

    private val viewModel: LaunchesViewModel by navGraphViewModels(R.id.launch_nav_graph) {
        defaultViewModelProviderFactory
    }

    private var _binding: FragmentVerticalRecyclerviewBinding? = null
    private val binding get() = checkNotNull(_binding) { "Binding is null" }

    private lateinit var launchesAdapter: LaunchesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentVerticalRecyclerviewBinding.inflate(inflater, container, false).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launchesAdapter = LaunchesAdapter { launch -> onItemClick(launch) }

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(this@UpcomingLaunchesListFragment.context)
            setHasFixedSize(true)
            adapter = launchesAdapter
        }

        viewModel.upcomingLaunchesLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResult.Pending -> binding.progress.show()
                is ApiResult.Success -> {
                    binding.progress.hide()
                    binding.swipeRefresh.isRefreshing = false

                    launchesAdapter.submitList(it.result)
                }

                is ApiResult.Failure -> {
                    binding.progress.hide()
                    binding.swipeRefresh.isRefreshing = false
                    showError(it.exception)
                }
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getUpcomingLaunches(CachePolicy.REFRESH)
        }

        viewModel.getUpcomingLaunches()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onItemClick(launch: LaunchItem) {
        viewModel.launch = launch

        findNavController().navigate(LaunchesFragmentDirections.actionLaunchesToLaunchDetails())
    }

    private fun showError(error: Throwable) {
        Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
        Log.e("PreviousLaunchesList", error.message.orUnknown())
    }

    override fun networkAvailable() {
        viewModel.getUpcomingLaunches()
    }
}
