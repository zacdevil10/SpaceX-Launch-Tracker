package uk.co.zac_h.spacex.launches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.base.MainActivity
import uk.co.zac_h.spacex.databinding.FragmentLaunchesBinding
import uk.co.zac_h.spacex.launches.adapters.LaunchesPagerAdapter

class LaunchesFragment : Fragment() {

    private var binding: FragmentLaunchesBinding? = null

    private val fragments: List<Fragment> = listOf(
        LaunchesListFragment.newInstance("upcoming"),
        LaunchesListFragment.newInstance("past")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLaunchesBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()

        (activity as MainActivity).setSupportActionBar(binding?.toolbar)

        val navController = NavHostFragment.findNavController(this)
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        val appBarConfig =
            AppBarConfiguration.Builder((context?.applicationContext as App).startDestinations)
                .setOpenableLayout(drawerLayout).build()

        binding?.toolbar?.setupWithNavController(navController, appBarConfig)

        binding?.launchesViewPager?.adapter = LaunchesPagerAdapter(childFragmentManager, fragments)

        val tabIcons = listOf(
            R.drawable.ic_baseline_schedule_24,
            R.drawable.ic_history_black_24dp
        )

        binding?.launchesTabLayout?.apply {
            setupWithViewPager(binding?.launchesViewPager)
            for (position in 0..tabCount) {
                getTabAt(position)?.setIcon(tabIcons[position])
            }
        }

        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
