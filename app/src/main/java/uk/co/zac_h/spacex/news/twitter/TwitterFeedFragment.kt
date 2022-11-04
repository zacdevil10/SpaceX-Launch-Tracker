package uk.co.zac_h.spacex.news.twitter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.viewpager.ViewPagerFragment
import uk.co.zac_h.spacex.databinding.FragmentTwitterFeedBinding
import uk.co.zac_h.spacex.news.adapters.TwitterFeedAdapter
import uk.co.zac_h.spacex.utils.PaginationScrollListener
import uk.co.zac_h.spacex.utils.animateEnterFromTop
import uk.co.zac_h.spacex.utils.animateExitToTop

class TwitterFeedFragment : BaseFragment(), ViewPagerFragment {

    override var title: String = "Twitter"

    private val viewModel: TwitterFeedViewModel by viewModels()

    private lateinit var binding: FragmentTwitterFeedBinding

    private lateinit var twitterAdapter: TwitterFeedAdapter

    private var isFabVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentTwitterFeedBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        twitterAdapter = TwitterFeedAdapter(requireContext(), ::openWebLink)

        val layout = LinearLayoutManager(requireContext())

        binding.twitterFeedRecycler.apply {
            layoutManager = layout
            setHasFixedSize(true)
            adapter = twitterAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

            addOnScrollListener(object : PaginationScrollListener(layout) {

                override fun isScrollUpVisible(): Boolean = isFabVisible

                override fun onScrollTop() {
                    if (isFabVisible) showScrollUp(false)
                }

                override fun onScrolledDown() {
                    showScrollUp(true)
                }
            })
        }

        binding.swipeRefresh.setOnRefreshListener {
            twitterAdapter.refresh()
        }

        binding.twitterFeedScrollUp.setOnClickListener {
            binding.twitterFeedRecycler.smoothScrollToPosition(0)
        }

        viewModel.twitterFeed.observe(viewLifecycleOwner) {
            twitterAdapter.submitData(lifecycle, it)
        }

        twitterAdapter.addLoadStateListener {
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

    private fun openWebLink(link: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }

    fun showScrollUp(visible: Boolean) {
        isFabVisible = visible
        binding.twitterFeedScrollUp.apply {
            startAnimation(if (visible) animateEnterFromTop(context) else animateExitToTop(context))
            visibility = if (visible) View.VISIBLE else View.INVISIBLE
        }
    }

    fun showError(error: String?) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        twitterAdapter.retry()
    }
}
