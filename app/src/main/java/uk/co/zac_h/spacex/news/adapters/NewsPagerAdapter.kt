package uk.co.zac_h.spacex.news.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import uk.co.zac_h.spacex.news.reddit.RedditFeedFragment
import uk.co.zac_h.spacex.news.twitter.TwitterFeedFragment

class NewsPagerAdapter(fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment =
        when (position) {
            0 -> TwitterFeedFragment()
            1 -> RedditFeedFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? =
        when (position) {
            0 -> "Twitter"
            1 -> "Reddit"
            else -> super.getPageTitle(position)
        }
}