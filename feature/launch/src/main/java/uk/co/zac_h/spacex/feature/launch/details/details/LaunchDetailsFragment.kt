package uk.co.zac_h.spacex.feature.launch.details.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.navGraphViewModels
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.viewpager.ViewPagerFragment
import uk.co.zac_h.spacex.core.ui.SpaceXTheme
import uk.co.zac_h.spacex.feature.launch.LaunchesViewModel
import uk.co.zac_h.spacex.feature.launch.R

class LaunchDetailsFragment : BaseFragment(), ViewPagerFragment {

    override val title: String = "Details"

    private val viewModel: LaunchesViewModel by navGraphViewModels(R.id.launch_nav_graph) {
        defaultViewModelProviderFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            SpaceXTheme {
                viewModel.launch?.let { LaunchDetailsScreen(launch = it) }
            }
        }
    }
}
