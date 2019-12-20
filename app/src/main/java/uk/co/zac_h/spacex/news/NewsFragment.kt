package uk.co.zac_h.spacex.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_news.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.news.adapters.NewsPagerAdapter

class NewsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_news, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        news_view_pager.adapter = NewsPagerAdapter(childFragmentManager)
        news_tab_layout.setupWithViewPager(news_view_pager)
    }
}
