package uk.co.zac_h.spacex.news.reddit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_reddit_feed.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.model.reddit.SubredditModel
import uk.co.zac_h.spacex.model.reddit.SubredditPostModel
import uk.co.zac_h.spacex.news.adapters.RedditAdapter
import uk.co.zac_h.spacex.utils.PaginationScrollListener
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class RedditFeedFragment : Fragment(), RedditFeedView,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var presenter: RedditFeedPresenter? = null

    private lateinit var redditAdapter: RedditAdapter
    private lateinit var posts: ArrayList<SubredditPostModel>

    private var isLastPage = false
    private var isLoading = false

    private var order: String = "hot"
    private var orderPos: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        order = savedInstanceState?.getString("order") ?: "hot"
        orderPos = savedInstanceState?.getInt("orderPos") ?: 0

        posts = savedInstanceState?.let {
            it.getParcelableArrayList<SubredditPostModel>("posts") as ArrayList<SubredditPostModel>
        } ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_reddit_feed, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = RedditFeedPresenterImpl(this, RedditFeedInteractorImpl())

        redditAdapter = RedditAdapter(this, posts)

        val layout = LinearLayoutManager(this@RedditFeedFragment.context)

        reddit_recycler.apply {
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

        reddit_swipe_refresh.setOnRefreshListener {
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
                            order = "hot"
                            presenter?.getSub(order)
                        }
                        1 -> {
                            order = "new"
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
        reddit_recycler.scrollToPosition(0)
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
        reddit_progress_bar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        reddit_progress_bar.visibility = View.GONE
    }

    override fun toggleSwipeRefresh(refreshing: Boolean) {
        reddit_swipe_refresh.isRefreshing = refreshing
    }

    override fun showPagingProgress() {
        reddit_feed_paging_progress_bar.visibility = View.VISIBLE
    }

    override fun hidePagingProgress() {
        reddit_feed_paging_progress_bar.visibility = View.GONE
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            if (posts.isEmpty() || reddit_progress_bar.visibility == View.VISIBLE) presenter?.getSub(
                order
            )
            if (isLoading) presenter?.getNextPage(posts[posts.size - 1].data.name, order)
        }
    }
}
