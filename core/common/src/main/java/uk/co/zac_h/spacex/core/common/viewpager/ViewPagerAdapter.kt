package uk.co.zac_h.spacex.core.common.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    private val fragments: List<ViewPagerFragment>
) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = fragments[position] as Fragment

    override fun getCount(): Int = fragments.size

    override fun getPageTitle(position: Int): CharSequence = fragments[position].title
}
