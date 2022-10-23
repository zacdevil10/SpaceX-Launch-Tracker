package uk.co.zac_h.spacex.launches.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.navigation.navGraphViewModels
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentLaunchDetailsContainerBinding
import uk.co.zac_h.spacex.launches.LaunchesViewModel
import uk.co.zac_h.spacex.launches.details.cores.LaunchDetailsCoresFragment
import uk.co.zac_h.spacex.launches.details.crew.LaunchDetailsCrewFragment
import uk.co.zac_h.spacex.launches.details.details.LaunchDetailsFragment
import uk.co.zac_h.spacex.utils.ViewPagerAdapter

class LaunchDetailsContainerFragment : BaseFragment() {

    private val viewModel: LaunchesViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var binding: FragmentLaunchDetailsContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host
        }

        exitTransition = MaterialElevationScale(false)
        reenterTransition = MaterialElevationScale(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLaunchDetailsContainerBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        binding.fragmentLaunchDetailsContainer.transitionName = viewModel.launch?.id

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
