package uk.co.zac_h.spacex.news.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.fragment.openWebLink
import uk.co.zac_h.spacex.core.common.viewpager.ViewPagerFragment
import uk.co.zac_h.spacex.core.ui.databinding.FragmentVerticalRecyclerviewBinding
import uk.co.zac_h.spacex.news.adapters.ArticlesAdapter

class ArticlesFragment : BaseFragment(), ViewPagerFragment {

    override val title: String = "Articles"

    private val viewModel: ArticlesViewModel by viewModels()

    private lateinit var binding: FragmentVerticalRecyclerviewBinding

    private lateinit var articlesAdapter: ArticlesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentVerticalRecyclerviewBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        articlesAdapter = ArticlesAdapter { openWebLink(it) }

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = articlesAdapter
        }

        binding.swipeRefresh.setOnRefreshListener {
            articlesAdapter.refresh()
        }

        viewModel.articlesLiveData.observe(viewLifecycleOwner) { data ->
            articlesAdapter.submitData(lifecycle, data.map { ArticleItem(it) })
        }

        articlesAdapter.addLoadStateListener {
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
        articlesAdapter.retry()
    }
}