package uk.co.zac_h.spacex.news.twitter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentTwitterFeedBinding
import uk.co.zac_h.spacex.model.twitter.TimelineTweetModel
import uk.co.zac_h.spacex.news.adapters.TwitterFeedAdapter
import uk.co.zac_h.spacex.utils.*

class TwitterFeedFragment : BaseFragment(), TwitterFeedContract.TwitterFeedView {

    override var title: String = "Twitter"

    private var binding: FragmentTwitterFeedBinding? = null

    private var presenter: TwitterFeedContract.TwitterFeedPresenter? = null
    private lateinit var twitterAdapter: TwitterFeedAdapter

    private lateinit var tweetsList: ArrayList<TimelineTweetModel>

    private var isLastPage = false
    private var isLoading = false
    private var isFabVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tweetsList = savedInstanceState?.let {
            it.getParcelableArrayList<TimelineTweetModel>("timeline") as ArrayList<TimelineTweetModel>
        } ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTwitterFeedBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideProgress()
        hidePagingProgress()

        presenter = TwitterFeedPresenterImpl(this, TwitterFeedInteractorImpl())

        twitterAdapter = TwitterFeedAdapter(context, tweetsList, this)
        val layout = LinearLayoutManager(this@TwitterFeedFragment.context)

        binding?.twitterFeedRecycler?.apply {
            layoutManager = layout
            setHasFixedSize(true)
            adapter = twitterAdapter

            addOnScrollListener(object : PaginationScrollListener(layout) {
                override fun isLastPage(): Boolean = isLastPage

                override fun isLoading(): Boolean = isLoading

                override fun isScrollUpVisible(): Boolean = isFabVisible

                override fun loadItems() {
                    isLoading = true
                    presenter?.getTweets(tweetsList[tweetsList.size - 1].id)
                }

                override fun onScrollTop() {
                    if (isFabVisible) presenter?.toggleScrollUp(false)
                }

                override fun onScrolledDown() {
                    presenter?.toggleScrollUp(true)
                }
            })
        }

        binding?.swipeRefresh?.setOnRefreshListener {
            presenter?.getTweets()
        }

        binding?.twitterFeedScrollUp?.setOnClickListener {
            binding?.twitterFeedRecycler?.smoothScrollToPosition(0)
        }

        if (tweetsList.isEmpty()) presenter?.getTweets()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("timeline", tweetsList)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequests()
        binding = null
    }

    override fun updateRecycler(tweets: List<TimelineTweetModel>) {
        tweetsList.clear()
        tweetsList.addAll(tweets)
        twitterAdapter.notifyDataSetChanged()
    }

    override fun addPagedData(tweets: List<TimelineTweetModel>) {
        isLoading = false

        tweetsList.addAllExcludingPosition(tweets, 0)

        twitterAdapter.notifyDataSetChanged()
    }

    override fun openWebLink(link: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }

    override fun showScrollUp() {
        isFabVisible = true
        binding?.twitterFeedScrollUp?.apply {
            startAnimation(animateEnterFromTop(context))
            visibility = View.VISIBLE
        }

    }

    override fun hideScrollUp() {
        isFabVisible = false
        binding?.twitterFeedScrollUp?.apply {
            startAnimation(animateExitToTop(context))
            visibility = View.INVISIBLE
        }
    }

    override fun showProgress() {
        binding?.progress?.show()
    }

    override fun showPagingProgress() {
        binding?.pagingProgressIndicator?.show()
    }

    override fun hideProgress() {
        binding?.progress?.hide()
    }

    override fun hidePagingProgress() {
        binding?.pagingProgressIndicator?.hide()
    }

    override fun toggleSwipeProgress(isRefreshing: Boolean) {
        binding?.swipeRefresh?.isRefreshing = isRefreshing
    }

    override fun showError(error: String) {

    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            binding?.let {
                if (tweetsList.isEmpty() || it.progress.isShown)
                    presenter?.getTweets()
            }

            if (isLoading) presenter?.getTweets(tweetsList[tweetsList.size - 1].id)
        }
    }
}
