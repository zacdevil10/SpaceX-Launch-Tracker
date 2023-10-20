package uk.co.zac_h.spacex.feature.launch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.recyclerview.PagingLoadStateAdapter
import uk.co.zac_h.spacex.core.common.utils.orUnknown
import uk.co.zac_h.spacex.core.common.viewpager.ViewPagerFragment
import uk.co.zac_h.spacex.core.ui.databinding.FragmentVerticalRecyclerviewBinding
import uk.co.zac_h.spacex.feature.launch.adapters.PaginatedLaunchesAdapter

@AndroidEntryPoint
class PreviousLaunchesListFragment : BaseFragment(), ViewPagerFragment {

    override val title: String by lazy { "Past" }

    private val viewModel: LaunchesViewModel by navGraphViewModels(R.id.launch_nav_graph) {
        defaultViewModelProviderFactory
    }

    private var _binding: FragmentVerticalRecyclerviewBinding? = null
    private val binding get() = checkNotNull(_binding) { "Binding is null" }

    private lateinit var launchesAdapter: PaginatedLaunchesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentVerticalRecyclerviewBinding.inflate(inflater, container, false).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launchesAdapter = PaginatedLaunchesAdapter { launch -> onItemClick(launch) }

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = launchesAdapter.withLoadStateFooter(
                footer = PagingLoadStateAdapter(launchesAdapter::retry)
            )
        }

        viewModel.previousLaunchesLiveData.observe(viewLifecycleOwner) { pagingData ->
            launchesAdapter.submitData(lifecycle, pagingData.map { LaunchItem(it) })
        }

        binding.swipeRefresh.setOnRefreshListener {
            launchesAdapter.refresh()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            launchesAdapter.loadStateFlow.collectLatest {
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
        launchesAdapter.retry()
    }
}
