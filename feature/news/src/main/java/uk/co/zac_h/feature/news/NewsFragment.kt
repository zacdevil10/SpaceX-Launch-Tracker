package uk.co.zac_h.feature.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.transition.MaterialSharedAxis
import uk.co.zac_h.feature.news.articles.ArticlesFragment
import uk.co.zac_h.feature.news.databinding.FragmentNewsBinding
import uk.co.zac_h.feature.news.reddit.RedditFeedFragment
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.viewpager.ViewPagerAdapter

class NewsFragment : BaseFragment() {

    private lateinit var binding: FragmentNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
    }

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
            listOf(ArticlesFragment(), RedditFeedFragment())
        )

        val tabIcons = listOf(R.drawable.ic_newspaper, R.drawable.reddit)

        binding.tabLayout.apply {
            setupWithViewPager(binding.newsViewPager)
            for (position in 0..tabCount) {
                getTabAt(position)?.setIcon(tabIcons[position])
            }
        }
    }
}
