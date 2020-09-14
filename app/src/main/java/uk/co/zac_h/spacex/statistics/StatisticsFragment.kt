package uk.co.zac_h.spacex.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.base.MainActivity
import uk.co.zac_h.spacex.databinding.FragmentStatisticsBinding
import uk.co.zac_h.spacex.statistics.adapters.StatisticsAdapter

class StatisticsFragment : Fragment() {

    private var binding: FragmentStatisticsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setSupportActionBar(binding?.toolbar)

        val navController = NavHostFragment.findNavController(this)
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        val appBarConfig =
            AppBarConfiguration.Builder((context?.applicationContext as App).startDestinations)
                .setOpenableLayout(drawerLayout).build()

        binding?.toolbar?.setupWithNavController(navController, appBarConfig)

        binding?.statisticsRecycler?.apply {
            layoutManager = LinearLayoutManager(this@StatisticsFragment.context)
            adapter = StatisticsAdapter()
        }

        /*binding?.statisticsViewPager?.apply {
            adapter = StatisticsAdapter(childFragmentManager)
            offscreenPageLimit = 2
        }

        val tabIcons = listOf(
            R.drawable.ic_history_black_24dp,
            R.drawable.ic_baseline_bar_chart_24,
            R.drawable.ic_baseline_import_export_24
        )

        binding?.statisticsTabLayout?.apply {
            setupWithViewPager(binding?.statisticsViewPager)
            for (position in 0..tabCount) {
                getTabAt(position)?.setIcon(tabIcons[position])
            }
        }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
