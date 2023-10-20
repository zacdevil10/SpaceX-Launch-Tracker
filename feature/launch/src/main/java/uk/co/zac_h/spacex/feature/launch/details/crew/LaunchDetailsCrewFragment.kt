package uk.co.zac_h.spacex.feature.launch.details.crew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navGraphViewModels
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.viewpager.ViewPagerFragment
import uk.co.zac_h.spacex.feature.launch.CrewItem
import uk.co.zac_h.spacex.feature.launch.LaunchesViewModel
import uk.co.zac_h.spacex.feature.launch.R
import uk.co.zac_h.spacex.feature.launch.adapters.LaunchCrewAdapter
import uk.co.zac_h.spacex.feature.launch.databinding.FragmentLaunchDetailsCrewBinding

class LaunchDetailsCrewFragment : BaseFragment(), ViewPagerFragment {

    override var title: String = "Crew"

    private val viewModel: LaunchesViewModel by navGraphViewModels(R.id.launch_nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var binding: FragmentLaunchDetailsCrewBinding

    private lateinit var crewAdapter: LaunchCrewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLaunchDetailsCrewBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        crewAdapter = LaunchCrewAdapter()
        crewAdapter.setHasStableIds(true)

        binding.launchDetailsCrewRecycler.apply {
            setHasFixedSize(false)
            adapter = crewAdapter
        }

        viewModel.launch?.crew?.let { update(it) }
    }

    private fun update(response: List<CrewItem>) {
        crewAdapter.submitList(response)
        binding.launchDetailsCrewRecycler.scheduleLayoutAnimation()
    }
}
