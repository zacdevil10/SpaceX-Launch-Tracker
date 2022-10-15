package uk.co.zac_h.spacex.launches

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
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
import uk.co.zac_h.spacex.NavGraphDirections
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentLaunchesListBinding
import uk.co.zac_h.spacex.launches.adapters.LaunchesAdapter
import uk.co.zac_h.spacex.types.LaunchType
import uk.co.zac_h.spacex.utils.orUnknown

@AndroidEntryPoint
class LaunchesListFragment : BaseFragment() {

    override val title: String by lazy { type.typeString }

    private val viewModel: LaunchesViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private val flowViewModel: FlowTypeViewModel by viewModels()

    private lateinit var type: LaunchType

    private var _binding: FragmentLaunchesListBinding? = null
    private val binding get() = checkNotNull(_binding) { "Binding is null" }

    private lateinit var launchesAdapter: LaunchesAdapter

    companion object {
        fun newInstance(type: LaunchType) = LaunchesListFragment().apply {
            this.type = type
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (::type.isInitialized) flowViewModel.type = type
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLaunchesListBinding.inflate(inflater, container, false).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launchesAdapter = LaunchesAdapter { launch, root -> onItemClick(launch, root) }

        binding.launchesRecycler.apply {
            layoutManager = LinearLayoutManager(this@LaunchesListFragment.context)
            setHasFixedSize(true)
            adapter = launchesAdapter
        }

        when (type) {
            LaunchType.UPCOMING -> viewModel.upcomingLaunchesLiveData.observe(viewLifecycleOwner) { pagingData ->
                launchesAdapter.submitData(lifecycle, pagingData.map { Launch(it) })
            }
            LaunchType.PAST -> viewModel.previousLaunchesLiveData.observe(viewLifecycleOwner) { pagingData ->
                launchesAdapter.submitData(lifecycle, pagingData.map { Launch(it) })
            }
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

    private fun onItemClick(launch: Launch, root: View) {
        viewModel.launch = launch

        findNavController().navigate(
            NavGraphDirections.actionLaunchItemToLaunchDetailsContainer(),
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
