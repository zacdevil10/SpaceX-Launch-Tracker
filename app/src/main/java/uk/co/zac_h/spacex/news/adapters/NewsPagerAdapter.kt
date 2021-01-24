package uk.co.zac_h.spacex.news.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import uk.co.zac_h.spacex.news.reddit.RedditFeedFragment
import uk.co.zac_h.spacex.news.twitter.TwitterFeedFragment
import uk.co.zac_h.spacex.utils.FragmentTitleInterface

class NewsPagerAdapter(fragmentManager: FragmentManager, private val fragments: List<Fragment>) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size

    override fun getPageTitle(position: Int): CharSequence =
        (fragments[position] as FragmentTitleInterface).title

}