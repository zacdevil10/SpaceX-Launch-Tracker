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

    private lateinit var presenter: RocketPresenter

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



        println("Hello $rocketsArray")

        rocketsAdapter = RocketsAdapter(rocketsArray)

        rocket_recycler.apply {
            layoutManager = LinearLayoutManager(this@RocketFragment.context)
            setHasFixedSize(true)
            adapter = rocketsAdapter
        }

        if (rocketsArray.isEmpty()) presenter.getRockets()
    }

    override fun onStart() {
        super.onStart()
        (context?.applicationContext as App).networkStateChangeListener.addListener(this)
    }

    override fun onPause() {
        super.onPause()
        (context?.applicationContext as App).networkStateChangeListener.removeListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("rockets", rocketsArray)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        println("Could have been me")
        presenter.cancelRequest()
    }

    override fun onDestroy() {
        super.onDestroy()
        println("Did someone call me?")
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

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            if (rocketsArray.isEmpty()) presenter.getRockets()
        }
    }
}
