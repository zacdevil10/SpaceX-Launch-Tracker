package uk.co.zac_h.feature.news.reddit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uk.co.zac_h.feature.news.adapters.RedditAdapter
import uk.co.zac_h.feature.news.databinding.FragmentRedditFeedBinding
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.fragment.openWebLink
import uk.co.zac_h.spacex.core.common.viewpager.ViewPagerFragment

@AndroidEntryPoint
class RedditFeedFragment : BaseFragment(), ViewPagerFragment {

    override var title: String = "Reddit"

    private val viewModel: RedditFeedViewModel by viewModels()

    private lateinit var binding: FragmentRedditFeedBinding

    private lateinit var redditAdapter: RedditAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentRedditFeedBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        redditAdapter = RedditAdapter {
            openWebLink(it)
        }

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

    private fun showError(error: String?) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        redditAdapter.retry()
    }
}

