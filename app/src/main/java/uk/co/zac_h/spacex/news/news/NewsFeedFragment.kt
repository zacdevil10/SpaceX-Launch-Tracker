package uk.co.zac_h.spacex.news.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_news_feed.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.news.ArticleModel
import uk.co.zac_h.spacex.news.adapters.NewsFeedAdapter
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class NewsFeedFragment : Fragment(), NewsFeedView,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private lateinit var presenter: NewsFeedPresenter

    private lateinit var networkStateChangeListener: OnNetworkStateChangeListener

    private lateinit var newsAdapter: NewsFeedAdapter
    private val articlesList = ArrayList<ArticleModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_news_feed, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = NewsFeedPresenterImpl(this, NewsFeedInteractorImpl())

        networkStateChangeListener = OnNetworkStateChangeListener(context)
            .apply {
                addListener(this@NewsFeedFragment)
                registerReceiver()
            }

        newsAdapter = NewsFeedAdapter(articlesList, this)

        news_feed_recycler.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(
                    this.context,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = newsAdapter
        }

        news_feed_swipe_refresh.setOnRefreshListener {
            presenter.getNews()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.cancelRequest()
        networkStateChangeListener.removeListener(this)
        networkStateChangeListener.unregisterReceiver()
    }

    override fun updateList(articles: List<ArticleModel>) {
        articlesList.clear()
        articlesList.addAll(articles)

        newsAdapter.notifyDataSetChanged()
    }

    override fun openWebLink(link: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }

    override fun toggleSwipeRefresh(isRefreshing: Boolean) {
        news_feed_swipe_refresh.isRefreshing = isRefreshing
    }

    override fun showProgress() {
        news_feed_progress_bar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        news_feed_progress_bar.visibility = View.GONE
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            presenter.getNews()
        }
    }
}
