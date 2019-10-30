package uk.co.zac_h.spacex.about.history

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_history.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.about.adapter.HistoryAdapter
import uk.co.zac_h.spacex.utils.HeaderItemDecoration
import uk.co.zac_h.spacex.utils.HeaderItemDecorationHorizontal
import uk.co.zac_h.spacex.utils.HistoryHeaderModel

class HistoryFragment : Fragment(), HistoryView {

    private lateinit var presenter: HistoryPresenter

    private val history = ArrayList<HistoryHeaderModel>()
    private lateinit var historyAdapter: HistoryAdapter

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
            setHasFixedSize(true)
            adapter = historyAdapter
            addItemDecoration(isTabletLand?.let {
                if (isTabletLand) HeaderItemDecorationHorizontal(
                    this,
                    historyAdapter.isHeader()
                ) else HeaderItemDecoration(this, historyAdapter.isHeader())
            } ?: HeaderItemDecoration(this, historyAdapter.isHeader()))
        }

        if (history.isEmpty()) presenter.getHistory()

        history_swipe_refresh.setOnRefreshListener {
            presenter.getHistory()
        }
    }

    override fun updateRecycler(history: ArrayList<HistoryHeaderModel>) {
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

}
