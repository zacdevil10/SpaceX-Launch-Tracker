package uk.co.zac_h.spacex.launches.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import uk.co.zac_h.spacex.launches.LaunchesListFragment

class LaunchesPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val fragments: List<Fragment>
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment = fragments[position]

    override fun getItemCount(): Int = 2

}