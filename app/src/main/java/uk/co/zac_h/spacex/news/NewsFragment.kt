package uk.co.zac_h.spacex.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentNewsBinding
import uk.co.zac_h.spacex.news.adapters.NewsPagerAdapter
import uk.co.zac_h.spacex.news.reddit.RedditFeedFragment
import uk.co.zac_h.spacex.news.twitter.TwitterFeedFragment

class NewsFragment : BaseFragment() {

    override var title: String = ""

    private var binding: FragmentNewsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.toolbar?.setupWithNavController(navController, appBarConfig)

        binding?.newsViewPager?.adapter = NewsPagerAdapter(
            childFragmentManager,
            listOf(TwitterFeedFragment(), RedditFeedFragment())
        )

        val tabIcons = listOf(
            R.drawable.ic_twitter,
            R.drawable.reddit
        )

        binding?.newsTabLayout?.apply {
            setupWithViewPager(binding?.newsViewPager)
            for (position in 0..tabCount) {
                getTabAt(position)?.setIcon(tabIcons[position])
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
