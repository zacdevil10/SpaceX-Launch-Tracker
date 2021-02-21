package uk.co.zac_h.spacex.launches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.tabs.TabLayout
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.base.MainActivity
import uk.co.zac_h.spacex.databinding.FragmentLaunchesBinding
import uk.co.zac_h.spacex.databinding.ToolbarTabBinding
import uk.co.zac_h.spacex.utils.ViewPagerAdapter

class LaunchesFragment : BaseFragment() {

    override var title: String = "Launches"

    private var _binding: FragmentLaunchesBinding? = null
    private val binding get() = _binding!!
    private var _toolbarBinding: ToolbarTabBinding? = null
    private val toolbarBinding get() = _toolbarBinding!!

    private val fragments: List<BaseFragment> = listOf(
        LaunchesListFragment.newInstance("upcoming"),
        LaunchesListFragment.newInstance("past")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLaunchesBinding.inflate(inflater, container, false).apply {
        _toolbarBinding = ToolbarTabBinding.bind(this.root)
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()

        (activity as MainActivity).setSupportActionBar(toolbarBinding.toolbar)
        toolbarBinding.toolbar.setup()

        binding.launchesViewPager.adapter = ViewPagerAdapter(childFragmentManager, fragments)

        val tabIcons = listOf(
            R.drawable.ic_baseline_schedule_24,
            R.drawable.ic_history_black_24dp
        )

        toolbarBinding.tabLayout.apply {
            setupWithViewPager(binding.launchesViewPager)
            for (position in 0..tabCount) {
                getTabAt(position)?.setIcon(tabIcons[position])
            }
        }

        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _toolbarBinding = null
        _binding = null
    }
}
