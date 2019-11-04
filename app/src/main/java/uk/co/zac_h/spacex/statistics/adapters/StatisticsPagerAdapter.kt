package uk.co.zac_h.spacex.statistics.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import uk.co.zac_h.spacex.statistics.graphs.launchhistory.LaunchHistoryFragment
import uk.co.zac_h.spacex.statistics.graphs.launchrate.LaunchRateFragment
import uk.co.zac_h.spacex.statistics.graphs.padstats.PadStatsFragment

class StatisticsPagerAdapter(fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment =
        when (position) {
            0 -> LaunchHistoryFragment()
            1 -> LaunchRateFragment()
            2 -> PadStatsFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }

    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence? = when (position) {
        0 -> "Launch History"
        1 -> "Launch Rate"
        2 -> "Launch/Landing Pads"
        else -> super.getPageTitle(position)
    }


}