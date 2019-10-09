package uk.co.zac_h.spacex.about.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_history.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.about.adapter.HistoryAdapter
import uk.co.zac_h.spacex.utils.HeaderItemDecoration
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

        historyAdapter = HistoryAdapter(history)

        history_recycler.apply {
            layoutManager = LinearLayoutManager(this@HistoryFragment.context)
            setHasFixedSize(true)
            adapter = historyAdapter
            addItemDecoration(HeaderItemDecoration(this, historyAdapter.isHeader()))
        }

        presenter.getHistory()
    }

    override fun updateRecycler(history: ArrayList<HistoryHeaderModel>) {
        this.history.addAll(history)

        historyAdapter.notifyDataSetChanged()
    }

}
