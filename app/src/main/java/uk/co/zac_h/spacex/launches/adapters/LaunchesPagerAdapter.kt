package uk.co.zac_h.spacex.launches.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import uk.co.zac_h.spacex.utils.FragmentTitleInterface

class LaunchesPagerAdapter(
    fragmentManager: FragmentManager,
    private val fragments: List<Fragment>
) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size

    override fun getPageTitle(position: Int): CharSequence =
        (fragments[position] as FragmentTitleInterface).title

}