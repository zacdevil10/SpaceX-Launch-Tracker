package uk.co.zac_h.spacex.statistics.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import uk.co.zac_h.spacex.statistics.graphs.ui.TotalLaunchesChartFragment

class StatisticsPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment? =
        when (position) {
            0 -> TotalLaunchesChartFragment()
            else -> null
        }

    override fun getCount(): Int = 1
}