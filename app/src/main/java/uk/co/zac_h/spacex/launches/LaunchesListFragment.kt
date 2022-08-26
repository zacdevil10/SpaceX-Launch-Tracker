package uk.co.zac_h.spacex.launches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentLaunchesListBinding
import uk.co.zac_h.spacex.launches.adapters.LaunchesAdapter
import uk.co.zac_h.spacex.types.LaunchType

@AndroidEntryPoint
class LaunchesListFragment : BaseFragment() {

    override val title: String by lazy { type.typeString }

    private val viewModel: LaunchesViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private val flowViewModel: FlowTypeViewModel by viewModels()

    private var searchText: String = ""

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

        launchesAdapter = LaunchesAdapter(requireContext())

        binding.launchesRecycler.apply {
            layoutManager = LinearLayoutManager(this@LaunchesListFragment.context)
            setHasFixedSize(true)
            adapter = launchesAdapter
        }

        viewModel.launchesLiveData.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                ApiResult.Status.PENDING -> if (launchesAdapter.itemCount == 0) binding.progress.show()
                ApiResult.Status.SUCCESS -> result.data?.let {
                    binding.swipeRefresh.isRefreshing = false
                    binding.progress.hide()
                    when (flowViewModel.type) {
                        LaunchType.UPCOMING -> update(it[flowViewModel.type]?.sortedBy { launch -> launch.flightNumber })
                        LaunchType.PAST -> update(it[flowViewModel.type]?.sortedByDescending { launch -> launch.flightNumber })
                    }

                }
                ApiResult.Status.FAILURE -> {
                    showError(result.error?.message)
                    binding.swipeRefresh.isRefreshing = false
                }
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getLaunches(CachePolicy.REFRESH)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun update(response: List<Launch>?) {
        launchesAdapter.submitList(response?.filter {
            it.missionName?.lowercase()?.contains(searchText) ?: true
        })
    }

    private fun showError(error: String?) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        viewModel.getLaunches()
    }
}
