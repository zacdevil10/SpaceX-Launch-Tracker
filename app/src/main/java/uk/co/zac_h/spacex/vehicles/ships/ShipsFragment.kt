package uk.co.zac_h.spacex.vehicles.ships

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
import uk.co.zac_h.spacex.databinding.FragmentShipsBinding
import uk.co.zac_h.spacex.model.spacex.ShipExtendedModel
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener
import uk.co.zac_h.spacex.vehicles.VehiclesContract
import uk.co.zac_h.spacex.vehicles.adapters.ShipsAdapter

class ShipsFragment : Fragment(), VehiclesContract.View<ShipExtendedModel>,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var _binding: FragmentShipsBinding? = null
    private val binding get() = _binding!!

    private var presenter: VehiclesContract.Presenter? = null

    private lateinit var shipsAdapter: ShipsAdapter
    private lateinit var shipsArray: ArrayList<ShipExtendedModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        shipsArray = savedInstanceState?.getParcelableArrayList("ships") ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShipsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideProgress()

        presenter = ShipsPresenterImpl(this, ShipsInteractorImpl())

        shipsAdapter = ShipsAdapter(shipsArray)

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(this@ShipsFragment.context)
            setHasFixedSize(true)
            adapter = shipsAdapter
        }

        binding.swipeRefresh.setOnRefreshListener {
            presenter?.getVehicles()
        }

        if (shipsArray.isEmpty()) presenter?.getVehicles()
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
        outState.putParcelableArrayList("ships", shipsArray)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
        _binding = null
    }

    override fun updateVehicles(vehicles: List<ShipExtendedModel>) {
        shipsArray.clear()
        shipsArray.addAll(vehicles)

        binding.recycler.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom)
        shipsAdapter.notifyDataSetChanged()
        binding.recycler.scheduleLayoutAnimation()
    }

    override fun showProgress() {
        binding.progressIndicator.show()
    }

    override fun hideProgress() {
        binding.progressIndicator.hide()
    }

    override fun toggleSwipeRefresh(refreshing: Boolean) {
        binding.swipeRefresh.isRefreshing = refreshing
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            if (shipsArray.isEmpty() || binding.progressIndicator.isShown) presenter?.getVehicles()
        }
    }
}