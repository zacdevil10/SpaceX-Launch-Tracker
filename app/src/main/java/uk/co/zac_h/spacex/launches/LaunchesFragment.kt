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
import uk.co.zac_h.spacex.databinding.FragmentLaunchesBinding
import uk.co.zac_h.spacex.launches.adapters.LaunchesPagerAdapter

class LaunchesFragment : Fragment() {

    private var _binding: FragmentLaunchesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLaunchesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = NavHostFragment.findNavController(this)
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        val appBarConfig =
            AppBarConfiguration.Builder((context?.applicationContext as App).startDestinations)
                .setDrawerLayout(drawerLayout).build()

        binding.toolbar.setupWithNavController(navController, appBarConfig)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        binding.launchesViewPager.adapter = LaunchesPagerAdapter(childFragmentManager)
        binding.launchesTabLayout.setupWithViewPager(binding.launchesViewPager)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
