package uk.co.zac_h.spacex.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_twitter_feed.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.twitter.TimelineTweetModel

class TwitterFeedFragment : Fragment(), TwitterFeedView {

    private lateinit var presenter: TwitterFeedPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_twitter_feed, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = TwitterFeedPresenterImpl(this, TwitterFeedInteractorImpl())

        twitter_feed_recycler.apply {
            layoutManager = LinearLayoutManager(this@TwitterFeedFragment.context)
            setHasFixedSize(true)

        }

        presenter.getTweets()
    }

    override fun updateRecycler(tweets: List<TimelineTweetModel>) {
        println(tweets)
    }
}
