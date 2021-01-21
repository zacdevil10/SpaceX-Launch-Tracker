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
import com.google.android.material.tabs.TabLayoutMediator
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.databinding.FragmentNewsBinding
import uk.co.zac_h.spacex.news.adapters.NewsPagerAdapter

class NewsFragment : Fragment() {

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

        val navController = NavHostFragment.findNavController(this)
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        val appBarConfig =
            AppBarConfiguration.Builder((context?.applicationContext as App).startDestinations)
                .setOpenableLayout(drawerLayout).build()

        binding?.toolbar?.setupWithNavController(navController, appBarConfig)

        binding?.newsViewPager?.adapter = NewsPagerAdapter(childFragmentManager, lifecycle)

        val tabIcons = listOf(
            R.drawable.ic_twitter,
            R.drawable.reddit
        )

        binding?.let {
            TabLayoutMediator(it.newsTabLayout, it.newsViewPager) { tab, position ->
                tab.text = when(position) {
                    0 -> "Twitter"
                    1 -> "Reddit"
                    else -> "Page $position"
                }
            }.attach()
        }

        binding?.newsTabLayout?.apply {
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
