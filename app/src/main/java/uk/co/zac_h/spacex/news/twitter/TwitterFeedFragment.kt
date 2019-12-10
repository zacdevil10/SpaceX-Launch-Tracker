package uk.co.zac_h.spacex.news.twitter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_twitter_feed.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.twitter.TimelineTweetModel
import uk.co.zac_h.spacex.news.adapters.TwitterFeedAdapter
import uk.co.zac_h.spacex.utils.PaginationScrollListener
import uk.co.zac_h.spacex.utils.addAllExcludingPosition
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class TwitterFeedFragment : Fragment(), TwitterFeedView,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private lateinit var presenter: TwitterFeedPresenter

    private lateinit var networkStateChangeListener: OnNetworkStateChangeListener

    private lateinit var twitterAdapter: TwitterFeedAdapter
    private var tweetsList = ArrayList<TimelineTweetModel?>()

    private var isLastPage = false
    private var isLoading = false
    private var isFabVisible = false

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

        networkStateChangeListener = OnNetworkStateChangeListener(context)
            .apply {
                addListener(this@TwitterFeedFragment)
                registerReceiver()
            }


        twitterAdapter = TwitterFeedAdapter(context, tweetsList, this)
        val layout = LinearLayoutManager(this@TwitterFeedFragment.context)

        twitter_feed_recycler.apply {
            layoutManager = layout
            setHasFixedSize(true)
            adapter = twitterAdapter

            addOnScrollListener(object : PaginationScrollListener(layout) {
                override fun isLastPage(): Boolean = isLastPage

                override fun isLoading(): Boolean = isLoading

                override fun isScrollUpVisible(): Boolean = isFabVisible

                override fun loadItems() {
                    isLoading = true
                    presenter.getTweets(
                        tweetsList[tweetsList.size - 1]?.id ?: tweetsList[tweetsList.size - 2]!!.id
                    )
                }

                override fun onScrollTop() {
                    presenter.toggleScrollUp(false)
                }

                override fun onScrolledDown() {
                    presenter.toggleScrollUp(true)
                }
            })
        }

        twitter_feed_swipe_refresh.setOnRefreshListener {
            presenter.getTweets()
        }

        twitter_feed_scroll_up.setOnClickListener {
            twitter_feed_recycler.smoothScrollToPosition(0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.cancelRequests()
        networkStateChangeListener.removeListener(this)
        networkStateChangeListener.unregisterReceiver()
    }

    override fun updateRecycler(tweets: List<TimelineTweetModel>) {
        tweetsList.clear()
        tweetsList.addAll(tweets)
        twitterAdapter.notifyDataSetChanged()
    }

    override fun addPagedData(tweets: List<TimelineTweetModel>) {
        isLoading = false
        twitterAdapter.removeNullData()

        tweetsList.addAllExcludingPosition(tweets, 0)

        twitterAdapter.notifyDataSetChanged()
    }

    override fun openWebLink(link: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }

    override fun showRecyclerLoading() {
        activity?.runOnUiThread {
            twitterAdapter.addNullData()
        }
    }

    override fun showScrollUp() {
        isFabVisible = true
        val animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_top)
        twitter_feed_scroll_up.apply {
            startAnimation(animation)
            visibility = View.VISIBLE
        }

    }

    override fun hideScrollUp() {
        isFabVisible = false
        val animation = AnimationUtils.loadAnimation(context, R.anim.slide_out_top)
        twitter_feed_scroll_up.apply {
            startAnimation(animation)
            visibility = View.INVISIBLE
        }
    }

    override fun showProgress() {
        twitter_feed_progress_bar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        twitter_feed_progress_bar.visibility = View.GONE
    }

    override fun toggleSwipeProgress(isRefreshing: Boolean) {
        twitter_feed_swipe_refresh.isRefreshing = isRefreshing
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            presenter.getTweets()
        }
    }
}
