package uk.co.zac_h.spacex.feature.launch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.navGraphViewModels
import dagger.hilt.android.AndroidEntryPoint
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.viewpager.ViewPagerFragment
import uk.co.zac_h.spacex.core.ui.SpaceXTheme

@AndroidEntryPoint
class UpcomingLaunchesListFragment : BaseFragment(), ViewPagerFragment {

    override val title: String by lazy { "Upcoming" }

    private val viewModel: LaunchesViewModel by navGraphViewModels(R.id.launch_nav_graph) {
        defaultViewModelProviderFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            viewModel.getUpcomingLaunches()

            SpaceXTheme {

            }
        }
    }

    override fun networkAvailable() {
        viewModel.getUpcomingLaunches()
    }
}
