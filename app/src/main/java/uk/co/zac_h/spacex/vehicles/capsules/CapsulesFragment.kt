package uk.co.zac_h.spacex.vehicles.capsules

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_capsules.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.model.spacex.CapsulesModel
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener
import uk.co.zac_h.spacex.vehicles.adapters.CapsulesAdapter

class CapsulesFragment : Fragment(), CapsulesView,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var presenter: CapsulesPresenter? = null

    private lateinit var capsulesAdapter: CapsulesAdapter
    private lateinit var capsulesArray: ArrayList<CapsulesModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        capsulesArray =
            savedInstanceState?.getParcelableArrayList<CapsulesModel>("capsules") ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_capsules, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = CapsulesPresenterImpl(this, CapsulesInteractorImpl())

        capsulesAdapter = CapsulesAdapter(capsulesArray)

        capsules_recycler.apply {
            layoutManager = LinearLayoutManager(this@CapsulesFragment.context)
            setHasFixedSize(true)
            adapter = capsulesAdapter
        }

        capsules_swipe_refresh.setOnRefreshListener {
            presenter?.getCapsules()
        }

        if (capsulesArray.isEmpty()) presenter?.getCapsules()
    }

    override fun onStart() {
        super.onStart()
        (context?.applicationContext as App).networkStateChangeListener.addListener(this)
    }

    override fun onStop() {
        super.onStop()
        (context?.applicationContext as App).networkStateChangeListener.removeListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("capsules", capsulesArray)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequests()
    }

    override fun updateCapsules(capsules: List<CapsulesModel>) {
        capsulesArray.clear()
        capsulesArray.addAll(capsules)

        capsulesAdapter.notifyDataSetChanged()
    }

    override fun showProgress() {
        capsules_progress_bar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        capsules_progress_bar.visibility = View.GONE
    }

    override fun toggleSwipeRefresh(refreshing: Boolean) {
        capsules_swipe_refresh.isRefreshing = refreshing
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            if (capsulesArray.isEmpty() || capsules_progress_bar.visibility == View.VISIBLE) presenter?.getCapsules()
        }
    }
}
