package uk.co.zac_h.spacex.vehicles.rockets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_rocket.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.model.spacex.RocketsModel
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener
import uk.co.zac_h.spacex.vehicles.adapters.RocketsAdapter

class RocketFragment : Fragment(), RocketView,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var presenter: RocketPresenter? = null

    private lateinit var rocketsAdapter: RocketsAdapter
    private lateinit var rocketsArray: ArrayList<RocketsModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rocketsArray =
            savedInstanceState?.getParcelableArrayList<RocketsModel>("rockets") ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_rocket, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = RocketPresenterImpl(this, RocketInteractorImpl())

        rocketsAdapter = RocketsAdapter(rocketsArray)

        rocket_recycler.apply {
            layoutManager = LinearLayoutManager(this@RocketFragment.context)
            setHasFixedSize(true)
            adapter = rocketsAdapter
        }

        rocket_swipe_refresh.setOnRefreshListener {
            presenter?.getRockets()
        }

        if (rocketsArray.isEmpty()) presenter?.getRockets()
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
        outState.putParcelableArrayList("rockets", rocketsArray)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
    }

    override fun updateRockets(rockets: List<RocketsModel>) {
        rocketsArray.clear()
        rocketsArray.addAll(rockets)

        rocketsAdapter.notifyDataSetChanged()
    }

    override fun showProgress() {
        rocket_progress_bar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        rocket_progress_bar.visibility = View.GONE
    }

    override fun toggleSwipeRefresh(refreshing: Boolean) {
        rocket_swipe_refresh.isRefreshing = refreshing
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            if (rocketsArray.isEmpty() || rocket_progress_bar.visibility == View.VISIBLE) presenter?.getRockets()
        }
    }
}
