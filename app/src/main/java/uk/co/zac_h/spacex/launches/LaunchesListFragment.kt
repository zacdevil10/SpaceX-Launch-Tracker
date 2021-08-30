package uk.co.zac_h.spacex.launches

import android.os.Bundle
import android.view.*
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
import uk.co.zac_h.spacex.dto.spacex.Launch
import uk.co.zac_h.spacex.launches.adapters.LaunchesAdapter
import java.util.*

@AndroidEntryPoint
class LaunchesListFragment : BaseFragment() {

    override val title: String by lazy { type.typeString }

    private val viewModel: LaunchesViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private val flowViewModel: FlowTypeViewModel by viewModels()

    private lateinit var type: LaunchType

    private lateinit var binding: FragmentLaunchesListBinding

    private lateinit var launchesAdapter: LaunchesAdapter

    companion object {
        fun newInstance(type: LaunchType) = LaunchesListFragment().apply {
            this.type = type
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        if (::type.isInitialized) flowViewModel.type = type
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLaunchesListBinding.inflate(inflater, container, false).apply {
        binding = this
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
                ApiResult.Status.PENDING -> showProgress()
                ApiResult.Status.SUCCESS -> result.data?.let {
                    toggleSwipeRefresh(false)
                    when (flowViewModel.type) {
                        LaunchType.UPCOMING -> update(it[flowViewModel.type]?.sortedBy { launch -> launch.flightNumber })
                        LaunchType.PAST -> update(it[flowViewModel.type]?.sortedByDescending { launch -> launch.flightNumber })
                    }

                }
                ApiResult.Status.FAILURE -> showError(result.error?.message)
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getLaunches(CachePolicy.REFRESH)
        }
    }

    fun update(response: List<Launch>?) {
        if (response == null) return
        hideProgress()

        launchesAdapter.update(response)
    }

    fun showProgress() {
        binding.progress.show()
    }

    fun hideProgress() {
        binding.progress.hide()
    }

    fun toggleSwipeRefresh(isRefreshing: Boolean) {
        binding.swipeRefresh.isRefreshing = isRefreshing
    }

    fun showError(error: String?) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        viewModel.getLaunches()
    }
}
