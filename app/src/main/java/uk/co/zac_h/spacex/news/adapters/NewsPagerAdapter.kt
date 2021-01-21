package uk.co.zac_h.spacex.news.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import uk.co.zac_h.spacex.news.reddit.RedditFeedFragment
import uk.co.zac_h.spacex.news.twitter.TwitterFeedFragment

class NewsPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> TwitterFeedFragment()
        1 -> RedditFeedFragment()
        else -> throw IllegalArgumentException("Invalid position: $position")
    }

    override fun getItemCount(): Int = 2

}