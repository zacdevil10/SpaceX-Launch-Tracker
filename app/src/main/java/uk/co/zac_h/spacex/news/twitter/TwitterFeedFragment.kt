package uk.co.zac_h.spacex.news.twitter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_twitter_feed.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.twitter.TimelineTweetModel
import uk.co.zac_h.spacex.news.adapters.TwitterFeedAdapter

class TwitterFeedFragment : Fragment(),
    TwitterFeedView {

    private lateinit var presenter: TwitterFeedPresenter
    private lateinit var twitterAdapter: TwitterFeedAdapter
    private var tweetsList = ArrayList<TimelineTweetModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_twitter_feed, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = TwitterFeedPresenterImpl(
            this,
            TwitterFeedInteractorImpl()
        )

        twitterAdapter = TwitterFeedAdapter(tweetsList, this)

        twitter_feed_recycler.apply {
            layoutManager = LinearLayoutManager(this@TwitterFeedFragment.context)
            setHasFixedSize(true)
            adapter = twitterAdapter
        }

        presenter.getTweets()
    }

    override fun updateRecycler(tweets: List<TimelineTweetModel>) {
        tweetsList.addAll(tweets)
        twitterAdapter.notifyDataSetChanged()
    }

    override fun openWebLink(link: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }
}
