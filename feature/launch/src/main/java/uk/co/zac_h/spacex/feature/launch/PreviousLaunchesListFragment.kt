package uk.co.zac_h.spacex.feature.launch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.utils.orUnknown
import uk.co.zac_h.spacex.core.common.viewpager.ViewPagerFragment
import uk.co.zac_h.spacex.feature.launch.adapters.PaginatedLaunchesAdapter
import uk.co.zac_h.spacex.feature.launch.databinding.FragmentLaunchesListBinding

@AndroidEntryPoint
class PreviousLaunchesListFragment : BaseFragment(), ViewPagerFragment {

    override val title: String by lazy { "Past" }

    private val viewModel: LaunchesViewModel by navGraphViewModels(R.id.launch_nav_graph) {
        defaultViewModelProviderFactory
    }

    private var _binding: FragmentLaunchesListBinding? = null
    private val binding get() = checkNotNull(_binding) { "Binding is null" }

    private lateinit var launchesAdapter: PaginatedLaunchesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLaunchesListBinding.inflate(inflater, container, false).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launchesAdapter = PaginatedLaunchesAdapter { launch, root -> onItemClick(launch, root) }

        binding.launchesRecycler.apply {
            layoutManager = LinearLayoutManager(this@PreviousLaunchesListFragment.context)
            setHasFixedSize(true)
            adapter = launchesAdapter
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

                    if (it.refresh is LoadState.NotLoading) binding.progress.hide()

                    val error = when {
                        it.prepend is LoadState.Error -> it.prepend as LoadState.Error
                        it.append is LoadState.Error -> it.append as LoadState.Error
                        it.refresh is LoadState.Error -> it.refresh as LoadState.Error
                        else -> null
                    }

                    error?.error?.message?.let { message -> showError(message) }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onItemClick(launch: LaunchItem, root: View) {
        viewModel.launch = launch

        findNavController().navigate(
            LaunchesFragmentDirections.actionLaunchesToLaunchDetails(),
            FragmentNavigatorExtras(root to launch.id)
        )
    }

    private fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        Log.e("Launches Network Error", error.orUnknown())
    }

    override fun networkAvailable() {
        launchesAdapter.retry()
    }
}
