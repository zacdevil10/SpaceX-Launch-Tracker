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
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.databinding.FragmentTwitterFeedBinding
import uk.co.zac_h.spacex.model.twitter.TimelineTweetModel
import uk.co.zac_h.spacex.news.adapters.TwitterFeedAdapter
import uk.co.zac_h.spacex.utils.PaginationScrollListener
import uk.co.zac_h.spacex.utils.addAllExcludingPosition
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class TwitterFeedFragment : Fragment(), TwitterFeedContract.TwitterFeedView,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var _binding: FragmentTwitterFeedBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentTwitterFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideProgress()
        hidePagingProgress()

        presenter = TwitterFeedPresenterImpl(this, TwitterFeedInteractorImpl())

        twitterAdapter = TwitterFeedAdapter(context, tweetsList, this)
        val layout = LinearLayoutManager(this@TwitterFeedFragment.context)

        binding.twitterFeedRecycler.apply {
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

        binding.twitterFeedSwipeRefresh.setOnRefreshListener {
            presenter?.getTweets()
        }

        binding.twitterFeedScrollUp.setOnClickListener {
            binding.twitterFeedRecycler.smoothScrollToPosition(0)
        }

        if (tweetsList.isEmpty()) presenter?.getTweets()
    }

    override fun onResume() {
        super.onResume()
        (context?.applicationContext as App).networkStateChangeListener.addListener(this)
    }

    override fun onPause() {
        super.onPause()
        (context?.applicationContext as App).networkStateChangeListener.removeListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("timeline", tweetsList)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequests()
        _binding = null
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
        val animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_top)
        binding.twitterFeedScrollUp.apply {
            startAnimation(animation)
            visibility = View.VISIBLE
        }

    }

    override fun hideScrollUp() {
        isFabVisible = false
        val animation = AnimationUtils.loadAnimation(context, R.anim.slide_out_top)
        binding.twitterFeedScrollUp.apply {
            startAnimation(animation)
            visibility = View.INVISIBLE
        }
    }

    override fun showProgress() {
        binding.progressIndicator.show()
    }

    override fun showPagingProgress() {
        binding.pagingProgressIndicator.show()
    }

    override fun hideProgress() {
        binding.progressIndicator.hide()
    }

    override fun hidePagingProgress() {
        binding.pagingProgressIndicator.hide()
    }

    override fun toggleSwipeProgress(isRefreshing: Boolean) {
        binding.twitterFeedSwipeRefresh.isRefreshing = isRefreshing
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            if (tweetsList.isEmpty() || binding.progressIndicator.isShown)
                presenter?.getTweets()

            if (isLoading) presenter?.getTweets(tweetsList[tweetsList.size - 1].id)
        }
    }
}
