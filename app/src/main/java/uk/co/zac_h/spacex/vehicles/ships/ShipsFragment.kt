package uk.co.zac_h.spacex.vehicles.ships

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.databinding.FragmentShipsBinding
import uk.co.zac_h.spacex.model.spacex.Ship
import uk.co.zac_h.spacex.utils.animateLayoutFromBottom
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener
import uk.co.zac_h.spacex.vehicles.FragmentTitleInterface
import uk.co.zac_h.spacex.vehicles.adapters.ShipsAdapter

class ShipsFragment : Fragment(), NetworkInterface.View<List<Ship>>,
    OnNetworkStateChangeListener.NetworkStateReceiverListener, FragmentTitleInterface {

    private var binding: FragmentShipsBinding? = null

    private var presenter: NetworkInterface.Presenter<Nothing>? = null

    private lateinit var shipsAdapter: ShipsAdapter
    private lateinit var shipsArray: ArrayList<Ship>

    override var title: String = "Ships"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        shipsArray = savedInstanceState?.getParcelableArrayList("ships") ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentShipsBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideProgress()

        presenter = ShipsPresenterImpl(this, ShipsInteractorImpl())

        shipsAdapter = ShipsAdapter(shipsArray)

        binding?.recycler?.apply {
            layoutManager = LinearLayoutManager(this@ShipsFragment.context)
            setHasFixedSize(true)
            adapter = shipsAdapter
        }

        binding?.swipeRefresh?.setOnRefreshListener {
            presenter?.get()
        }

        if (shipsArray.isEmpty()) presenter?.get()
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
        binding = null
    }

    override fun update(response: List<Ship>) {
        shipsArray.clear()
        shipsArray.addAll(response)

        binding?.recycler?.layoutAnimation = animateLayoutFromBottom(context)
        shipsAdapter.notifyDataSetChanged()
        binding?.recycler?.scheduleLayoutAnimation()
    }

    override fun showProgress() {
        binding?.progressIndicator?.show()
    }

    override fun hideProgress() {
        binding?.progressIndicator?.hide()
    }

    override fun toggleSwipeRefresh(isRefreshing: Boolean) {
        binding?.swipeRefresh?.isRefreshing = isRefreshing
    }

    override fun showError(error: String) {

    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            binding?.let {
                if (shipsArray.isEmpty() || it.progressIndicator.isShown) presenter?.get()
            }
        }
    }
}