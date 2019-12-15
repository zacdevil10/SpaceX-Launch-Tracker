package uk.co.zac_h.spacex.about.history

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_history.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.about.adapter.HistoryAdapter
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.utils.HeaderItemDecoration
import uk.co.zac_h.spacex.utils.models.HistoryHeaderModel
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class HistoryFragment : Fragment(), HistoryView,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private lateinit var presenter: HistoryPresenter

    private lateinit var history: ArrayList<HistoryHeaderModel>
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        history = savedInstanceState?.let {
            it.getParcelableArrayList<HistoryHeaderModel>("history") as ArrayList<HistoryHeaderModel>
        } ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_history, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = HistoryPresenterImpl(this, HistoryInteractorImpl())

        historyAdapter = HistoryAdapter(history, this)

        val isTabletLand = context?.resources?.getBoolean(R.bool.isTabletLand)

        history_recycler.apply {
            layoutManager = isTabletLand?.let {
                if (isTabletLand) {
                    LinearLayoutManager(
                        this@HistoryFragment.context,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                } else {
                    LinearLayoutManager(this@HistoryFragment.context)
                }
            } ?: LinearLayoutManager(this@HistoryFragment.context)
            setHasFixedSize(true)
            adapter = historyAdapter
            addItemDecoration(
                HeaderItemDecoration(
                    this,
                    historyAdapter.isHeader(),
                    isTabletLand ?: false
                )
            )
        }

        history_swipe_refresh.setOnRefreshListener {
            presenter.getHistory()
        }

        if (history.isEmpty()) {
            presenter.getHistory()
        }
    }

    override fun onResume() {
        super.onResume()
        (context?.applicationContext as App).networkStateChangeListener.addListener(this)
    }

    override fun onPause() {
        super.onPause()
        (context?.applicationContext as App).networkStateChangeListener.removeListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("history", history)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.cancelRequest()
    }

    override fun addHistory(history: ArrayList<HistoryHeaderModel>) {
        this.history.clear()
        this.history.addAll(history)

        historyAdapter.notifyDataSetChanged()
    }

    override fun openWebLink(link: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }

    override fun showProgress() {
        history_progress_bar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        history_progress_bar.visibility = View.GONE
    }

    override fun toggleSwipeProgress(isRefreshing: Boolean) {
        history_swipe_refresh?.isRefreshing = isRefreshing
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            if (history.isEmpty() || history_progress_bar.visibility == View.VISIBLE) presenter.getHistory()
        }
    }

}
