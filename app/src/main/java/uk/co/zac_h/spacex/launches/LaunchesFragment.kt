package uk.co.zac_h.spacex.launches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.navigation.navGraphViewModels
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentLaunchesBinding
import uk.co.zac_h.spacex.utils.ViewPagerAdapter

@AndroidEntryPoint
class LaunchesFragment : BaseFragment() {

    override val title: String by lazy { getString(R.string.menu_launches) }

    private val viewModel: LaunchesViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var binding: FragmentLaunchesBinding

    private val fragments: List<BaseFragment> = listOf(
        LaunchesListFragment.newInstance(LaunchType.UPCOMING),
        LaunchesListFragment.newInstance(LaunchType.PAST)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exitTransition = MaterialElevationScale(false)
        reenterTransition = MaterialElevationScale(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLaunchesBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.doOnPreDraw { startPostponedEnterTransition() }
        postponeEnterTransition()

        binding.launchesViewPager.adapter = ViewPagerAdapter(childFragmentManager, fragments)

        val tabIcons = listOf(
            R.drawable.ic_baseline_schedule_24,
            R.drawable.ic_history_black_24dp
        )

        binding.tabLayout.apply {
            setupWithViewPager(binding.launchesViewPager)
            for (position in 0..tabCount) {
                getTabAt(position)?.setIcon(tabIcons[position])
            }
        }

        viewModel.getLaunches()
    }
}

enum class LaunchType(val typeString: String) {
    UPCOMING("upcoming"), PAST("past")
}