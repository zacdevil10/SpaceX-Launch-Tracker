package uk.co.zac_h.spacex.news.reddit

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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

        redditAdapter.addLoadStateListener {
            if (it.refresh is LoadState.Loading) {
                binding.progress.show()
            } else {
                binding.swipeRefresh.isRefreshing = false

                if (it.refresh is LoadState.NotLoading) binding.progress.hide()

                val error = when {
                    it.prepend is LoadState.Error -> it.prepend as LoadState.Error
                    it.append is LoadState.Error -> it.append as LoadState.Error
                    it.refresh is LoadState.Error -> it.refresh as LoadState.Error
                    else -> null
                }

                error?.let {
                    showError(error.error.message)
                }
            }
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
            setSelection(viewModel.orderPosition)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    if (viewModel.orderPosition != position) when (position) {
                        0 -> {
                            viewModel.setOrder(REDDIT_PARAM_ORDER_HOT, position)
                            redditAdapter.refresh()
                        }
                        1 -> {
                            viewModel.setOrder(REDDIT_PARAM_ORDER_NEW, position)
                            redditAdapter.refresh()
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }
    }

    fun showError(error: String?) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        redditAdapter.retry()
    }
}
