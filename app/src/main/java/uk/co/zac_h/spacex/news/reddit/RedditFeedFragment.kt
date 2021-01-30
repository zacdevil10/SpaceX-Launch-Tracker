package uk.co.zac_h.spacex.news.reddit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentRedditFeedBinding
import uk.co.zac_h.spacex.model.reddit.SubredditModel
import uk.co.zac_h.spacex.model.reddit.SubredditPostModel
import uk.co.zac_h.spacex.news.adapters.RedditAdapter
import uk.co.zac_h.spacex.utils.PaginationScrollListener
import uk.co.zac_h.spacex.utils.REDDIT_PARAM_ORDER_HOT
import uk.co.zac_h.spacex.utils.REDDIT_PARAM_ORDER_NEW

class RedditFeedFragment : BaseFragment(), RedditFeedContract.RedditFeedView {

    override var title: String = "Reddit"

    private var binding: FragmentRedditFeedBinding? = null

    private var presenter: RedditFeedContract.RedditFeedPresenter? = null
    private lateinit var redditAdapter: RedditAdapter

    private lateinit var posts: ArrayList<SubredditPostModel>

    private var isLastPage = false
    private var isLoading = false

    private var order: String = REDDIT_PARAM_ORDER_HOT
    private var orderPos: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        order = savedInstanceState?.getString("order") ?: REDDIT_PARAM_ORDER_HOT
        orderPos = savedInstanceState?.getInt("orderPos") ?: 0

        posts = savedInstanceState?.let {
            it.getParcelableArrayList<SubredditPostModel>("posts") as ArrayList<SubredditPostModel>
        } ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRedditFeedBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideProgress()
        hidePagingProgress()

        presenter = RedditFeedPresenterImpl(this, RedditFeedInteractorImpl())

        redditAdapter = RedditAdapter(this, posts)

        val layout = LinearLayoutManager(this@RedditFeedFragment.context)

        binding?.redditRecycler?.apply {
            layoutManager = layout
            setHasFixedSize(true)
            adapter = redditAdapter

            addOnScrollListener(object : PaginationScrollListener(layout) {
                override fun isLastPage(): Boolean = isLastPage

                override fun isLoading(): Boolean = isLoading

                override fun isScrollUpVisible(): Boolean = false

                override fun loadItems() {
                    isLoading = true
                    presenter?.getNextPage(posts[posts.size - 1].data.name, order)
                }

                override fun onScrollTop() {

                }

                override fun onScrolledDown() {

                }
            })
        }

        binding?.swipeRefresh?.setOnRefreshListener {
            presenter?.getSub(order)
        }
    }

    override fun onResume() {
        super.onResume()
        if (posts.isEmpty()) presenter?.getSub(order)
        (context?.applicationContext as App).networkStateChangeListener.addListener(this)
    }

    override fun onPause() {
        super.onPause()
        (context?.applicationContext as App).networkStateChangeListener.removeListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("posts", posts)
        outState.putString("order", order)
        outState.putInt("orderPos", orderPos)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
        binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_reddit, menu)

        val item = menu.findItem(R.id.reddit_order).actionView as Spinner
        item.apply {
            adapter = ArrayAdapter.createFromResource(
                context,
                R.array.order,
                android.R.layout.simple_spinner_item
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            setSelection(orderPos)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    if (orderPos != position) when (position) {
                        0 -> {
                            order = REDDIT_PARAM_ORDER_HOT
                            presenter?.getSub(order)
                        }
                        1 -> {
                            order = REDDIT_PARAM_ORDER_NEW
                            presenter?.getSub(order)
                        }
                    }
                    orderPos = position
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun updateRecycler(subredditData: SubredditModel) {
        posts.clear()
        posts.addAll(subredditData.data.children)

        redditAdapter.notifyDataSetChanged()
        binding?.redditRecycler?.scrollToPosition(0)
    }

    override fun addPagedData(subredditData: SubredditModel) {
        isLoading = false

        posts.addAll(subredditData.data.children)
        redditAdapter.notifyDataSetChanged()
    }

    override fun openWebLink(link: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }

    override fun showProgress() {
        binding?.progress?.show()
    }

    override fun hideProgress() {
        binding?.progress?.hide()
    }

    override fun toggleSwipeRefresh(refreshing: Boolean) {
        binding?.swipeRefresh?.isRefreshing = refreshing
    }

    override fun showPagingProgress() {
        binding?.pagingProgressIndicator?.show()
    }

    override fun hidePagingProgress() {
        binding?.pagingProgressIndicator?.hide()
    }

    override fun showError(error: String) {

    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            binding?.let {
                if (posts.isEmpty() || it.progress.isShown)
                    presenter?.getSub(order)
            }

            if (isLoading) presenter?.getNextPage(posts[posts.size - 1].data.name, order)
        }
    }
}
