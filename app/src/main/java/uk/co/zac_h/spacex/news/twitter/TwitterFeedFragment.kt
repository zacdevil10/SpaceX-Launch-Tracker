package uk.co.zac_h.spacex.news.twitter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentTwitterFeedBinding
import uk.co.zac_h.spacex.model.twitter.TimelineTweetModel
import uk.co.zac_h.spacex.news.adapters.TwitterFeedAdapter
import uk.co.zac_h.spacex.utils.*

class TwitterFeedFragment : BaseFragment(), TwitterFeedContract.TwitterFeedView {

    override var title: String = "Twitter"

    private lateinit var binding: FragmentTwitterFeedBinding

    private var presenter: TwitterFeedContract.TwitterFeedPresenter? = null
    private lateinit var twitterAdapter: TwitterFeedAdapter

    private lateinit var tweetsList: ArrayList<TimelineTweetModel>

    private var isLastPage = false
    private var isLoading = false
    private var isFabVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tweetsList = savedInstanceState?.getParcelableArrayList("timeline") ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentTwitterFeedBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hidePagingProgress()

        presenter = TwitterFeedPresenterImpl(this, TwitterFeedInteractorImpl())

        twitterAdapter = TwitterFeedAdapter(requireContext(), tweetsList, this)
        val layout = LinearLayoutManager(requireContext())

        binding.twitterFeedRecycler.apply {
            layoutManager = layout
            setHasFixedSize(true)
            adapter = twitterAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

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

        binding.swipeRefresh.setOnRefreshListener {
            apiState = ApiResult.Status.PENDING
            presenter?.getTweets()
        }

        binding.twitterFeedScrollUp.setOnClickListener {
            binding.twitterFeedRecycler.smoothScrollToPosition(0)
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
    }

    override fun updateRecycler(tweets: List<TimelineTweetModel>) {
        apiState = ApiResult.Status.SUCCESS

        tweetsList.clearAndAdd(tweets)
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
        binding.twitterFeedScrollUp.apply {
            startAnimation(animateEnterFromTop(context))
            visibility = View.VISIBLE
        }

    }

    override fun hideScrollUp() {
        isFabVisible = false
        binding.twitterFeedScrollUp.apply {
            startAnimation(animateExitToTop(context))
            visibility = View.INVISIBLE
        }
    }

    override fun showProgress() {

    }

    override fun showPagingProgress() {
        binding.pagingProgressIndicator.show()
    }

    override fun hideProgress() {

    }

    override fun hidePagingProgress() {
        binding.pagingProgressIndicator.hide()
    }

    override fun toggleSwipeProgress(isRefreshing: Boolean) {
        binding.swipeRefresh.isRefreshing = isRefreshing
    }

    override fun showError(error: String) {
        apiState = ApiResult.Status.FAILURE
    }

    override fun networkAvailable() {
        when (apiState) {
            ApiResult.Status.PENDING, ApiResult.Status.FAILURE -> presenter?.getTweets()
            ApiResult.Status.SUCCESS -> {
            }
        }
        if (isLoading) presenter?.getTweets(tweetsList[tweetsList.size - 1].id)
    }
}
