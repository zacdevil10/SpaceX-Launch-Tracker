package uk.co.zac_h.spacex.news.reddit

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.REDDIT_PARAM_ORDER_HOT
import uk.co.zac_h.spacex.REDDIT_PARAM_ORDER_NEW
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentRedditFeedBinding
import uk.co.zac_h.spacex.news.adapters.RedditAdapter
import uk.co.zac_h.spacex.utils.openWebLink

@AndroidEntryPoint
class RedditFeedFragment : BaseFragment() {

    override var title: String = "Reddit"

    private val viewModel: RedditFeedViewModel by viewModels()

    private lateinit var binding: FragmentRedditFeedBinding

    private lateinit var redditAdapter: RedditAdapter

    private var order: String = REDDIT_PARAM_ORDER_HOT
    private var orderPos: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentRedditFeedBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        redditAdapter = RedditAdapter(openLink = ::openWebLink)

        binding.redditRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = redditAdapter
        }

        binding.swipeRefresh.setOnRefreshListener {
            redditAdapter.refresh()
        }

        viewModel.redditFeed.observe(viewLifecycleOwner) {
            redditAdapter.submitData(lifecycle, it)
        }
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
                            viewModel.setOrder(order)
                            redditAdapter.refresh()
                        }
                        1 -> {
                            order = REDDIT_PARAM_ORDER_NEW
                            viewModel.setOrder(order)
                            redditAdapter.refresh()
                        }
                    }
                    orderPos = position
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }
    }

    fun showError(error: String) {

    }

    override fun networkAvailable() {
        /*when (apiState) {
            ApiResult.Status.PENDING, ApiResult.Status.FAILURE -> presenter?.getPosts(order = order)
            ApiResult.Status.SUCCESS -> Log.i(title, "Network available and data loaded")
        }
        if (isLoading) presenter?.getPosts(id = posts.last().name, order = order)*/

        //redditAdapter.retry()
    }
}
