package uk.co.zac_h.spacex.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.base.MainActivity
import uk.co.zac_h.spacex.databinding.FragmentNewsBinding
import uk.co.zac_h.spacex.databinding.ToolbarTabBinding
import uk.co.zac_h.spacex.news.reddit.RedditFeedFragment
import uk.co.zac_h.spacex.news.twitter.TwitterFeedFragment
import uk.co.zac_h.spacex.utils.ViewPagerAdapter

class NewsFragment : BaseFragment() {

    override val title: String by lazy { requireContext().getString(R.string.menu_news) }

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private var _toolbarBinding: ToolbarTabBinding? = null
    private val toolbarBinding get() = _toolbarBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentNewsBinding.inflate(inflater, container, false).apply {
        _toolbarBinding = ToolbarTabBinding.bind(this.root)
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setSupportActionBar(toolbarBinding.toolbar)
        toolbarBinding.toolbar.setup()

        binding.newsViewPager.adapter = ViewPagerAdapter(
            childFragmentManager,
            listOf(TwitterFeedFragment(), RedditFeedFragment())
        )

        val tabIcons = listOf(R.drawable.ic_twitter, R.drawable.reddit)

        toolbarBinding.tabLayout.apply {
            setupWithViewPager(binding.newsViewPager)
            for (position in 0..tabCount) {
                getTabAt(position)?.setIcon(tabIcons[position])
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _toolbarBinding = null
        _binding = null
    }
}
