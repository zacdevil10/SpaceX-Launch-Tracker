package uk.co.zac_h.spacex.launches.details.crew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navGraphViewModels
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.crew.adapters.CrewAdapter
import uk.co.zac_h.spacex.databinding.FragmentLaunchDetailsCrewBinding
import uk.co.zac_h.spacex.dto.spacex.Crew
import uk.co.zac_h.spacex.launches.details.LaunchDetailsContainerViewModel

class LaunchDetailsCrewFragment : BaseFragment() {

    private val viewModel: LaunchDetailsContainerViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var binding: FragmentLaunchDetailsCrewBinding

    private lateinit var crewAdapter: CrewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLaunchDetailsCrewBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        crewAdapter = CrewAdapter()

        binding.launchDetailsCrewRecycler.apply {
            setHasFixedSize(true)
            adapter = crewAdapter
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getLaunch(CachePolicy.REFRESH)
        }

        viewModel.launch.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                ApiResult.Status.PENDING -> {
                }
                ApiResult.Status.SUCCESS -> response.data?.crew?.let { update(it) }.also {
                    binding.swipeRefresh.isRefreshing = false
                }
                ApiResult.Status.FAILURE -> binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    fun update(response: List<Crew>) {
        /*if (apiState != ApiResult.Status.SUCCESS) binding.launchDetailsCrewRecycler.layoutAnimation =
            animateLayoutFromBottom(requireContext())
        */
        crewAdapter.update(response)
        binding.launchDetailsCrewRecycler.scheduleLayoutAnimation()
    }
}