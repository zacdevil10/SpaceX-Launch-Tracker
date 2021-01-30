package uk.co.zac_h.spacex.crew.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class CrewPagerAdapter(fragment: FragmentManager, val crew: List<Fragment>) :
    FragmentStatePagerAdapter(fragment, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = crew[position]

    override fun getCount(): Int = crew.size

}