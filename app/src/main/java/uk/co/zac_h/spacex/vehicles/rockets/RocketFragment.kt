package uk.co.zac_h.spacex.vehicles.rockets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.databinding.FragmentRocketBinding
import uk.co.zac_h.spacex.model.spacex.RocketsModel
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener
import uk.co.zac_h.spacex.vehicles.adapters.RocketsAdapter

class RocketFragment : Fragment(), RocketContract.RocketView,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var _binding: FragmentRocketBinding? = null
    private val binding get() = _binding!!

    private var presenter: RocketContract.RocketPresenter? = null

    private lateinit var rocketsAdapter: RocketsAdapter
    private lateinit var rocketsArray: ArrayList<RocketsModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rocketsArray = savedInstanceState?.getParcelableArrayList("rockets") ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRocketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = RocketPresenterImpl(this, RocketInteractorImpl())

        rocketsAdapter = RocketsAdapter(rocketsArray)

        binding.rocketRecycler.apply {
            layoutManager = LinearLayoutManager(this@RocketFragment.context)
            setHasFixedSize(true)
            adapter = rocketsAdapter
        }

        binding.rocketSwipeRefresh.setOnRefreshListener {
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
        _binding = null
    }

    override fun updateRockets(rockets: List<RocketsModel>) {
        rocketsArray.clear()
        rocketsArray.addAll(rockets)

        binding.rocketRecycler.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom)
        rocketsAdapter.notifyDataSetChanged()
        binding.rocketRecycler.scheduleLayoutAnimation()
    }

    override fun showProgress() {
        binding.rocketProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.rocketProgressBar.visibility = View.GONE
    }

    override fun toggleSwipeRefresh(refreshing: Boolean) {
        binding.rocketSwipeRefresh.isRefreshing = refreshing
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            if (rocketsArray.isEmpty() || binding.rocketProgressBar.visibility == View.VISIBLE)
                presenter?.getRockets()
        }
    }
}
