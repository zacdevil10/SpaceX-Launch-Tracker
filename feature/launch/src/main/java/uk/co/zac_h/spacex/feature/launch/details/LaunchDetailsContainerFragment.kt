package uk.co.zac_h.spacex.feature.launch.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navGraphViewModels
import com.google.android.material.transition.MaterialSharedAxis
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.viewpager.ViewPagerAdapter
import uk.co.zac_h.spacex.feature.launch.LaunchesViewModel
import uk.co.zac_h.spacex.feature.launch.R
import uk.co.zac_h.spacex.feature.launch.databinding.FragmentLaunchDetailsContainerBinding
import uk.co.zac_h.spacex.feature.launch.details.cores.LaunchDetailsCoresFragment
import uk.co.zac_h.spacex.feature.launch.details.crew.LaunchDetailsCrewFragment
import uk.co.zac_h.spacex.feature.launch.details.details.LaunchDetailsFragment

class LaunchDetailsContainerFragment : BaseFragment() {

    private val viewModel: LaunchesViewModel by navGraphViewModels(R.id.launch_nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var binding: FragmentLaunchDetailsContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLaunchDetailsContainerBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = ViewPagerAdapter(
            childFragmentManager,
            listOfNotNull(
                LaunchDetailsFragment(),
                if (!viewModel.launch?.firstStage.isNullOrEmpty()) LaunchDetailsCoresFragment() else null,
                if (!viewModel.launch?.crew.isNullOrEmpty()) LaunchDetailsCrewFragment() else null
            )
        )
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }
}
