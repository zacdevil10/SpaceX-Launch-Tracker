package uk.co.zac_h.spacex.news.reddit

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.REDDIT_PARAM_ORDER_HOT
import uk.co.zac_h.spacex.REDDIT_PARAM_ORDER_NEW
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentRedditFeedBinding
import uk.co.zac_h.spacex.dto.reddit.RedditPost
import uk.co.zac_h.spacex.news.adapters.RedditAdapter
import uk.co.zac_h.spacex.utils.PaginationScrollListener
import uk.co.zac_h.spacex.utils.clearAndAdd
import uk.co.zac_h.spacex.utils.openWebLink

class RedditFeedFragment : BaseFragment(), RedditFeedContract.RedditFeedView {

    override var title: String = "Reddit"

    private lateinit var binding: FragmentRedditFeedBinding

    private var presenter: RedditFeedContract.RedditFeedPresenter? = null
    private lateinit var redditAdapter: RedditAdapter

    private lateinit var posts: ArrayList<RedditPost>

    private var isLastPage = false
    private var isLoading = false

    private var order: String = REDDIT_PARAM_ORDER_HOT
    private var orderPos: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        order = savedInstanceState?.getString("order") ?: REDDIT_PARAM_ORDER_HOT
        orderPos = savedInstanceState?.getInt("orderPos") ?: 0

        /*posts = savedInstanceState?.let {
            it.getParcelableArrayList<RedditPost>("posts") as ArrayList<RedditPost>
        } ?: ArrayList()*/
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentRedditFeedBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hidePagingProgress()

        presenter = RedditFeedPresenterImpl(this, RedditFeedInteractorImpl())

        redditAdapter = RedditAdapter(::openWebLink, posts)

        val layout = LinearLayoutManager(context)

        binding.redditRecycler.apply {
            layoutManager = layout
            adapter = redditAdapter

            addOnScrollListener(object : PaginationScrollListener(layout) {
                override fun isLastPage(): Boolean = isLastPage

                override fun isLoading(): Boolean = isLoading

                override fun isScrollUpVisible(): Boolean = false

                override fun loadItems() {
                    isLoading = true
                    presenter?.getPosts(id = posts.last().name, order = order)
                }
            })
        }

        binding.swipeRefresh.setOnRefreshListener {

            presenter?.getPosts(order = order)
        }

        if (posts.isEmpty()) presenter?.getPosts(order = order)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.apply {
            //putParcelableArrayList("posts", posts)
            putString("order", order)
            putInt("orderPos", orderPos)
        }
    }

    override fun onDestroyView() {
        presenter?.cancelRequest()
        super.onDestroyView()
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
                            presenter?.getPosts(order = order)
                        }
                        1 -> {
                            order = REDDIT_PARAM_ORDER_NEW
                            presenter?.getPosts(order = order)
                        }
                    }
                    orderPos = position
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }
    }

    override fun updateRecycler(subredditData: List<RedditPost>) {


        posts.clearAndAdd(subredditData)
        redditAdapter.notifyDataSetChanged()
        binding.redditRecycler.scrollToPosition(0)
    }

    override fun addPagedData(subredditData: List<RedditPost>) {
        isLoading = false

        posts.addAll(subredditData)
        redditAdapter.notifyDataSetChanged()
    }

    override fun showProgress() {

    }

    override fun hideProgress() {

    }

    override fun toggleSwipeRefresh(refreshing: Boolean) {
        binding.swipeRefresh.isRefreshing = refreshing
    }

    override fun showPagingProgress() {
        binding.pagingProgressIndicator.show()
    }

    override fun hidePagingProgress() {
        binding.pagingProgressIndicator.hide()
    }

    override fun showError(error: String) {

    }

    override fun networkAvailable() {
        /*when (apiState) {
            ApiResult.Status.PENDING, ApiResult.Status.FAILURE -> presenter?.getPosts(order = order)
            ApiResult.Status.SUCCESS -> Log.i(title, "Network available and data loaded")
        }
        if (isLoading) presenter?.getPosts(id = posts.last().name, order = order)*/
    }
}
