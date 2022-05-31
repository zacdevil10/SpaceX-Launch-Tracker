package uk.co.zac_h.spacex.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentNewsBinding
import uk.co.zac_h.spacex.news.reddit.RedditFeedFragment
import uk.co.zac_h.spacex.news.twitter.TwitterFeedFragment
import uk.co.zac_h.spacex.utils.ViewPagerAdapter

class NewsFragment : BaseFragment() {

    private lateinit var binding: FragmentNewsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentNewsBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newsViewPager.adapter = ViewPagerAdapter(
            childFragmentManager,
            listOf(TwitterFeedFragment(), RedditFeedFragment())
        )

        val tabIcons = listOf(R.drawable.ic_twitter, R.drawable.reddit)

        binding.tabLayout.apply {
            setupWithViewPager(binding.newsViewPager)
            for (position in 0..tabCount) {
                getTabAt(position)?.setIcon(tabIcons[position])
            }
        }
    }
}
